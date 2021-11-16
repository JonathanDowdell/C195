package c195.controller;

import c195.dao.CustomerDAO;
import c195.model.Appointment;
import c195.model.Customer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    public TableColumn<Customer, Long> customerIDColumn;

    @FXML
    public TableColumn<Customer, String> customerNameColumn;

    @FXML
    public Button addCustomerButton;

    @FXML
    public Button modifyCustomerButton;

    @FXML
    public Button deleteContactButton;

    @FXML
    public TableColumn<Appointment, Long> appointmentIDColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentTitleColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentDescColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentLocationColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentContactColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentStartColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentEndColumn;

    @FXML
    public TableColumn<Appointment, Long> appointmentCustomerIDColumn;

    @FXML
    public TableColumn<Appointment, Long> appointmentUserIDColumn;

    @FXML
    public Button addAppointmentButton;

    @FXML
    public Button modifyAppointmentButton;

    @FXML
    public Button deleteAppointmentButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerDAO.getAllCustomers();
    }
}
