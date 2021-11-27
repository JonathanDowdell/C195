package c195.controller;

import c195.dao.AppointmentDAO;
import c195.dao.ContactDAO;
import c195.dao.CustomerDAO;
import c195.exception.InvalidAppointmentException;
import c195.model.Appointment;
import c195.model.Contact;
import c195.model.Customer;
import c195.util.LocalDateTimeHelper;
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
import java.util.ResourceBundle;

/**
 * @author Jonathan Dowdell
 */
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

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:m a");

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboField.setItems(customers);
        contactComboField.setItems(contacts);
        startPmAmCombo.getItems().addAll("PM", "AM");
        startPmAmCombo.getSelectionModel().select(0);
        endPmAmCombo.getItems().addAll("PM", "AM");
        endPmAmCombo.getSelectionModel().select(0);

        numbersOnlyField(startHourTimeField);
        numbersOnlyField(startMinuteTimeField);
        numbersOnlyField(endHourTimeField);
        numbersOnlyField(endMinuteTimeField);
    }

    private void numbersOnlyField(TextField someTextField) {
        someTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                someTextField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (someTextField.getText().length() >= 2) {
                final String substring = someTextField.getText().substring(0, 2);
                someTextField.setText(substring);
            }
        });
    }

    /**
     * Save Appointment
     * @param event
     */
    @FXML
    private void saveAction(ActionEvent event) {

        try {
            // Start Date Time
            final LocalDateTime startLocalDateTime = createLocalDateTime(startDateField.getValue(), startHourTimeField.getText(),
                    startMinuteTimeField.getText(), startPmAmCombo.getSelectionModel().getSelectedItem());

            // End Date Time
            final LocalDateTime endLocalDateTime = createLocalDateTime(endDateField.getValue(), endHourTimeField.getText(),
                    endMinuteTimeField.getText(), endPmAmCombo.getSelectionModel().getSelectedItem());


            // Appointment
            final Appointment appointment = new Appointment();
            appointment.setTitle(titleTextField.getText());
            appointment.setDescription(descriptionTextField.getText());
            appointment.setLocation(locationTextField.getText());
            appointment.setType(typeTextField.getText());
            appointment.setStart(startLocalDateTime);
            appointment.setEnd(endLocalDateTime);
            appointment.setCustomer(customerComboField.getValue());
            appointment.setContact(contactComboField.getValue());

            appointment.validate();

            AppointmentDAO.addAppointment(appointment);

            cancelAction(event);
        } catch (InvalidAppointmentException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error", "Please Address Error", e.getMessage());
        }
    }

    /**
     * Navigate to Main View
     * @param event
     */
    @FXML
    private void cancelAction(ActionEvent event) {
        try {
            NavigationHelper.mainView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Appointment View Controller using Appointment
     * @param appointment
     */
    public void loadAppointment(Appointment appointment) {
        customerComboField.getSelectionModel().select(appointment.getCustomer());
        contactComboField.getSelectionModel().select(appointment.getContact());
        idTextField.setText(String.valueOf(appointment.getAppointmentID()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());

        startDateField.setValue(appointment.getStart().toLocalDate());
        endDateField.setValue(appointment.getEnd().toLocalDate());

        startHourTimeField.setText(LocalDateTimeHelper.get12Hour(appointment.getStart()));
        startMinuteTimeField.setText(LocalDateTimeHelper.get12Minute(appointment.getStart()));
        startPmAmCombo.getSelectionModel().select(LocalDateTimeHelper.get12AMPM(appointment.getStart()).toUpperCase());

        endHourTimeField.setText(LocalDateTimeHelper.get12Hour(appointment.getEnd()));
        endMinuteTimeField.setText(LocalDateTimeHelper.get12Minute(appointment.getEnd()));
        endPmAmCombo.getSelectionModel().select(LocalDateTimeHelper.get12AMPM(appointment.getEnd()).toUpperCase());
    }

    /**
     * Create LocalDateTime from hour, minute, and pmAM
     * @param localDate
     * @param hour
     * @param minute
     * @param pmAM
     * @return LocalDateTime
     * @throws InvalidAppointmentException
     */
    private LocalDateTime createLocalDateTime(LocalDate localDate, String hour, String minute, String pmAM) throws InvalidAppointmentException {
        try {
            final String localDateTime = localDate.toString() + " " + hour + ":" + minute + " " + pmAM;
            return LocalDateTime.parse(localDateTime, dateTimeFormatter);
        } catch (Exception e) {
            throw new InvalidAppointmentException("Missing Date(s)");
        }
    }
}
