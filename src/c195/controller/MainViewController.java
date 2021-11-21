package c195.controller;

import c195.dao.AppointmentDAO;
import c195.dao.CustomerDAO;
import c195.model.Appointment;
import c195.model.Customer;
import c195.util.NavigationHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static c195.util.ModalHelper.displayAlert;

public class MainViewController implements Initializable {

    @FXML
    public TableColumn<Customer, Long> customerIDColumn;

    @FXML
    public TableColumn<Customer, String> customerNameColumn;

    @FXML
    public TableColumn<Customer, Long> customerDivisionIDColumn;

    @FXML
    public Button addCustomerButton;

    @FXML
    public Button modifyCustomerButton;

    @FXML
    public Button deleteCustomerButton;

    @FXML
    public TableColumn<Appointment, Long> appointmentIDColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentTitleColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentDescColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentLocationColumn;

    @FXML
    public TableColumn<Appointment, Long> appointmentContactColumn;

    @FXML
    public TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    public TableColumn<Appointment, LocalDateTime> appointmentStartColumn;

    @FXML
    public TableColumn<Appointment, LocalDateTime> appointmentEndColumn;

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

    @FXML
    public TableView<Customer> customerTable;

    @FXML
    public TableView<Appointment> appointmentTable;

    private final ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleCustomerTable();
        handleAppointmentTable();
    }

    @FXML
    private void addCustomerAction(ActionEvent actionEvent) {
        try {
            NavigationHelper.customerView(actionEvent, null);
        } catch (IOException e) {
            // TODO: 11/17/2021 Handle Error
            e.printStackTrace();
        }
    }

    @FXML
    private void updateCustomerAction(ActionEvent actionEvent) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            displayAlert(Alert.AlertType.INFORMATION, "Customer not selected.", "Please select customer.");
        } else {
            try {
                NavigationHelper.customerView(actionEvent, selectedCustomer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteCustomerAction() {
        try {
            Customer customer = Optional.of(customerTable.getSelectionModel().getSelectedItem())
                    .orElseThrow();

            if (!AppointmentDAO.getAppointmentByCustomer(customer).isEmpty()) {
                displayAlert(Alert.AlertType.ERROR, "Deletion Error",
                        "Customer still has Appointment(s)",
                        "Please delete Appointment(s) first.");
                return;
            }

            Alert alert = displayAlert(Alert.AlertType.CONFIRMATION,
                    "Delete Product",
                    "Are you sure you want to delete this product?",
                    "Press OK to delete the product. \nPress Cancel to cancel the deletion.");

            if (alert.getResult() == ButtonType.OK) {
                boolean customerRemoved = CustomerDAO.removeCustomer(customer);
                if (customerRemoved) {
                    customers.remove(customer);
                } else {
                    displayAlert(Alert.AlertType.ERROR,
                            "Product Deletion Error",
                            "The product was NOT deleted.",
                            "Please try again.");
                }
            }

        } catch (Exception e) {
            displayAlert(Alert.AlertType.ERROR,
                    "Product Deletion Error",
                    "The product was NOT deleted.",
                    "Please select a product to delete.");
        }
    }

    @FXML
    public void addAppointmentAction(ActionEvent event) {
        try {
            NavigationHelper.manageAppointmentView(event, null);
        } catch (IOException e) {
            // TODO: 11/21/2021
            e.printStackTrace();
        }
    }

    private void handleAppointmentTable() {
        appointmentTable.setItems(AppointmentDAO.getAllAppointments());
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentCustomerIDColumn.setCellValueFactory(value -> {
            long customerID = value.getValue().getCustomer().getCustomerID();
            return new ReadOnlyObjectWrapper<>(customerID);
        });

        appointmentUserIDColumn.setCellValueFactory(value -> {
            long userID = value.getValue().getUser().getUserID();
            return new ReadOnlyObjectWrapper<>(userID);
        });

        appointmentContactColumn.setCellValueFactory(value -> {
            long contactID = value.getValue().getContact().getContactID();
            return new ReadOnlyObjectWrapper<>(contactID);
        });
    }

    private void handleCustomerTable() {
        customerTable.setItems(customers);
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerDivisionIDColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getDivision().getDivisionID()));
    }

}
