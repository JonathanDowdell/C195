package c195.controller;

import c195.dao.ContactDAO;
import c195.dao.CustomerDAO;
import c195.model.Contact;
import c195.model.Customer;
import c195.util.ModalHelper;
import c195.util.NavigationHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ManageAppointmentViewController implements Initializable {


    @FXML
    public ComboBox<Customer> customerComboField;

    @FXML
    public ComboBox<Contact> contactComboField;

    @FXML
    public TextField idTextField;

    @FXML
    public TextField titleTextField;

    @FXML
    public TextField descriptionTextField;

    @FXML
    public TextField locationTextField;

    @FXML
    public TextField typeTextField;

    @FXML
    public DatePicker startDateField;

    @FXML
    public TextField startHourTimeField;

    @FXML
    public TextField startMinuteTimeField;

    @FXML
    public ComboBox<String> startPmAmCombo;

    @FXML
    public DatePicker endDateField;

    @FXML
    public TextField endHourTimeField;

    @FXML
    public TextField endMinuteTimeField;

    @FXML
    public ComboBox<String> endPmAmCombo;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    private final ObservableList<Contact> contacts = ContactDAO.getAllContacts();

    private final ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboField.setItems(customers);
        contactComboField.setItems(contacts);
        startPmAmCombo.getItems().addAll("PM", "AM");
        startPmAmCombo.getSelectionModel().select(0);
        endPmAmCombo.getItems().addAll("PM", "AM");
        endPmAmCombo.getSelectionModel().select(0);

    }

    @FXML
    private void saveAction(ActionEvent event) {

        if (hasInvalidFields()) { return; }


        // Start Date Time
        final LocalDateTime startLocalDateTime = createLocalDateTime(startDateField.getValue(), startHourTimeField.getText(),
                startMinuteTimeField.getText(), startPmAmCombo.getSelectionModel().getSelectedItem());

        // End Date Time
        final LocalDateTime endLocalDateTime = createLocalDateTime(endDateField.getValue(), endHourTimeField.getText(),
                endMinuteTimeField.getText(), endPmAmCombo.getSelectionModel().getSelectedItem());


    }

    @FXML
    private void cancelAction(ActionEvent event) {
        try {
            NavigationHelper.mainView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LocalDateTime createLocalDateTime(LocalDate localDate, String hour, String minute, String pmAM) {
        final String startDateText = localDate.toString() + " " + hour + ":" + minute + " " + pmAM;
        return LocalDateTime.parse(startDateText, dateTimeFormatter);
    }

    private boolean hasInvalidFields() {
        final ArrayList<String> errorList = new ArrayList<>();

        if (customerComboField.getSelectionModel().getSelectedItem() == null) {
            errorList.add("Customer is not selected");
        }

        if (contactComboField.getSelectionModel().getSelectedItem() == null) {
            errorList.add("Contact is not selected");
        }


        if (titleTextField.getText().isEmpty()) {
            errorList.add("Title is empty");
        }

        if (descriptionTextField.getText().isEmpty()) {
            errorList.add("Description is empty");
        }

        if (locationTextField.getText().isEmpty()) {
            errorList.add("Location is empty");
        }

        if (typeTextField.getText().isEmpty()) {
            errorList.add("Type is empty");
        }

        if (startHourTimeField.getText().isEmpty()) {
            errorList.add("Start Hour is empty");
        }

        if (startMinuteTimeField.getText().isEmpty()) {
            errorList.add("Start Minute is empty");
        }

        if (endHourTimeField.getText().isEmpty()) {
            errorList.add("End Hour is empty");
        }

        if (endMinuteTimeField.getText().isEmpty()) {
            errorList.add("End Minute is empty");
        }

        if (startPmAmCombo.getSelectionModel().getSelectedItem() == null) {
            errorList.add("Start PM / AM not selected");
        }

        if (endPmAmCombo.getSelectionModel().getSelectedItem() == null) {
            errorList.add("End PM / AM not selected");
        }

        if (startDateField.getValue() == null) {
            errorList.add("Start date is empty");
        } else if (startDateField.getValue().isBefore(LocalDate.now())) {
            errorList.add("Start date should be after current date");
        }

        if (endDateField.getValue() == null) {
            errorList.add("Please select end date");
        } else if (endDateField.getValue().isBefore(LocalDate.now())) {
            errorList.add("End date should be after current date");
        }

        if (!errorList.isEmpty()) {
            final String errorMessages = errorList.stream().map(error -> error + "\n").collect(Collectors.joining());
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error", "Please Address Error(s)", errorMessages);
        }

        return !errorList.isEmpty();
    }

}
