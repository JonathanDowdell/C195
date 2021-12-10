package c195.controller;

import c195.dao.AppointmentDAO;
import c195.dao.ContactDAO;
import c195.dao.CustomerDAO;
import c195.dao.UserDAO;
import c195.exception.InvalidAppointmentException;
import c195.model.Appointment;
import c195.model.Contact;
import c195.model.Customer;
import c195.util.LocalDateTimeHelper;
import c195.util.ModalHelper;
import c195.util.NavigationHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * @author Jonathan Dowdell
 */
public class AppointmentViewController implements Initializable {


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

    private ObservableList<Contact> contacts = ContactDAO.getAllContacts();

    private ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:m a");

    private Callback<ListView<Customer>, ListCell<Customer>> customerCellFactory = new Callback<>() {

        @Override
        public ListCell<Customer> call(ListView<Customer> l) {
            return new ListCell<>() {
                @Override
                protected void updateItem(Customer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        setText(item.getCustomerID() + " - " + item.getCustomerName());
                    }
                }
            };
        }
    };

    private Callback<ListView<Contact>, ListCell<Contact>> contactCellFactory = new Callback<>() {

        @Override
        public ListCell<Contact> call(ListView<Contact> l) {
            return new ListCell<>() {

                @Override
                protected void updateItem(Contact item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        setText(item.getContactID() + " - " + item.getContactName());
                    }
                }
            };
        }
    };

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboField.setButtonCell(customerCellFactory.call(null));
        customerComboField.setCellFactory(customerCellFactory);

        contactComboField.setButtonCell(contactCellFactory.call(null));
        contactComboField.setCellFactory(contactCellFactory);

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
     * Save Appointment.
     * @param event
     */
    @FXML
    private void saveAction(ActionEvent event) {
        final boolean updatingAppointment = !idTextField.getText().isEmpty();
        try {
            // Start Date Time
            final LocalDateTime startLocalDateTime = createLocalDateTime(startDateField.getValue(), startHourTimeField.getText(),
                    startMinuteTimeField.getText(), startPmAmCombo.getSelectionModel().getSelectedItem());

            // End Date Time
            final LocalDateTime endLocalDateTime = createLocalDateTime(endDateField.getValue(), endHourTimeField.getText(),
                    endMinuteTimeField.getText(), endPmAmCombo.getSelectionModel().getSelectedItem());


            // Appointment
            Appointment appointment = new Appointment();
            appointment.setTitle(titleTextField.getText());
            appointment.setDescription(descriptionTextField.getText());
            appointment.setLocation(locationTextField.getText());
            appointment.setType(typeTextField.getText());
            appointment.setStart(startLocalDateTime);
            appointment.setEnd(endLocalDateTime);
            appointment.setCustomer(customerComboField.getValue());
            appointment.setContact(contactComboField.getValue());
            appointment.setUser(UserDAO.getCurrentUser());

            if (updatingAppointment) {
                appointment.setAppointmentID(Long.parseLong(idTextField.getText()));
            }

            appointment.validate();


            if (updatingAppointment) {
                AppointmentDAO.updateAppointment(appointment);
            } else {
                AppointmentDAO.addAppointment(appointment);
            }

            cancelAction(event);
        } catch (InvalidAppointmentException e) {
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error", "Please Address Error", e.getMessage());
        }
    }

    /**
     * Navigate to Main View.
     * @param event
     */
    @FXML
    private void cancelAction(ActionEvent event) {
        try {
            NavigationHelper.manageAppointmentView(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Appointment View Controller using Appointment.
     * @param appointment
     */
    public void loadAppointment(Appointment appointment) {
        customerComboField.setButtonCell(customerCellFactory.call(null));
        customerComboField.setCellFactory(customerCellFactory);

        contactComboField.setButtonCell(contactCellFactory.call(null));
        contactComboField.setCellFactory(contactCellFactory);

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
     * Create LocalDateTime from hour, minute, and pmAM.
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
