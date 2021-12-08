package c195.controller;

import c195.dao.AppointmentDAO;
import c195.dao.CustomerDAO;
import c195.model.Customer;
import c195.util.NavigationHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static c195.util.ModalHelper.displayAlert;

public class ManageCustomerViewController implements Initializable {

    @FXML
    public TableColumn<Customer, Long> customerIDColumn;

    @FXML
    public TableColumn<Customer, String> customerNameColumn;

    @FXML
    public TableColumn<Customer, Long> customerDivisionIDColumn;

    @FXML
    public TableColumn<Customer, String> customerAddressColumn;

    @FXML
    public TableColumn<Customer, String> customerPostalCodeColumn;

    @FXML
    public TableColumn<Customer, String> customerPhoneColumn;

    @FXML
    public Button addCustomerButton;

    @FXML
    public Button modifyCustomerButton;

    @FXML
    public Button deleteCustomerButton;

    @FXML
    public Button backButton;

    @FXML
    public TableView<Customer> customerTable;

    private final ObservableList<Customer> customers = CustomerDAO.getAllCustomers();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleCustomerTable();
    }

    private void handleCustomerTable() {
        customerTable.setItems(customers);
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerDivisionIDColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getDivision().getDivisionID()));
        customerAddressColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getAddress()));
        customerPhoneColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getPhone()));
        customerPostalCodeColumn.setCellValueFactory(value -> new ReadOnlyObjectWrapper<>(value.getValue().getPostalCode()));
    }

    /**
     * Opens Manage Customer View
     */
    @FXML
    private void addCustomerAction(ActionEvent actionEvent) {
        try {
            NavigationHelper.customerView(actionEvent, null);
        } catch (IOException e) {
            // TODO: 11/17/2021 Handle Error
            e.printStackTrace();
        }
    }

    /**
     * Open Manage Customer View with selected customer
     */
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

    /**
     * Delete customer using selected customer
     */
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
    public void backAction(ActionEvent event) {
        try {
            NavigationHelper.homeView(event);
        } catch (IOException e) {
            // TODO: 11/17/2021 Handle Error
            e.printStackTrace();
        }
    }
}
