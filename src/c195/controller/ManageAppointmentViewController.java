package c195.controller;

import c195.dao.ContactDAO;
import c195.dao.CustomerDAO;
import c195.model.Contact;
import c195.model.Customer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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
    public DatePicker endDateField;

    @FXML
    public Button saveButton;

    @FXML
    public Button cancelButton;

    private final ObservableList<Contact> contacts = ContactDAO.getAllContacts();

    private final ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerComboField.setItems(customers);
        contactComboField.setItems(contacts);
    }

}
