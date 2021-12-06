package c195.controller;

import c195.dao.CountryDAO;
import c195.dao.CustomerDAO;
import c195.dao.FirstLevelDivisionDAO;
import c195.dao.UserDAO;
import c195.model.Country;
import c195.model.Customer;
import c195.model.FirstLevelDivision;
import c195.util.ModalHelper;
import c195.util.NavigationHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Jonathan Dowdell
 */
public class ManageCustomerViewController implements Initializable {
    public TextField idTextField;
    public TextField nameTextField;
    public TextField addressTextField;
    public ComboBox<Country> countryComboField;
    public ComboBox<FirstLevelDivision> stateProvinceComboField;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;
    private boolean update = false;

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleComboBox();
    }

    /**
     * Load Manage Customer View using Customer.
     * @param customer
     */
    public void loadCustomer(Customer customer) {
        FirstLevelDivision division = customer.getDivision();
        Country country = division.getCountry();
        idTextField.setText(String.valueOf(customer.getCustomerID()));
        nameTextField.setText(customer.getCustomerName());
        addressTextField.setText(customer.getAddress());
        countryComboField.getSelectionModel().select(country);
        stateProvinceComboField.getSelectionModel().select(division);
        postalCodeTextField.setText(customer.getPostalCode());
        phoneNumberTextField.setText(customer.getPhone());
        update = true;
    }

    /**
     * Save Customer
     * @param actionEvent
     */
    @FXML
    private void saveCustomerAction(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        FirstLevelDivision firstLevelDivision = stateProvinceComboField.getSelectionModel().getSelectedItem();
        String postalCode = postalCodeTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        boolean results = false;
        boolean validFields = validFields();

        if (!update && validFields) {
            // Create New
            Customer customer = joinFieldsToCustomer(-1, name, address, firstLevelDivision, postalCode, phoneNumber);
            results = CustomerDAO.addCustomer(customer);
        } else if (validFields) {
            // Update
            long customerID = Long.parseLong(idTextField.getText());
            Customer customer = joinFieldsToCustomer(customerID, name, address, firstLevelDivision, postalCode, phoneNumber);
            results = CustomerDAO.updateCustomer(customer);
        }

        if (!results && validFields) {
            String saveOrUpdate = update ? "Update" : "Save";
            ModalHelper.displayAlert(Alert.AlertType.ERROR,  saveOrUpdate + " Error",
                    "Unable to " + saveOrUpdate + " Customer", "Please check with Developer");
        } else if (results) {
            cancelAction(actionEvent);
        }
    }

    /**
     * Join TextFields to create Customer.
     * @param customerID
     * @param name
     * @param address
     * @param firstLevelDivision
     * @param postalCode
     * @param phoneNumber
     * @return
     */
    private Customer joinFieldsToCustomer(long customerID, String name, String address, FirstLevelDivision firstLevelDivision, String postalCode, String phoneNumber) {
        Customer customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setCustomerName(name);
        customer.setAddress(address);
        customer.setDivision(firstLevelDivision);
        customer.setPostalCode(postalCode);
        customer.setPhone(phoneNumber);
        customer.setLastUpdate(LocalDateTime.now());
        customer.setLastUpdatedBy(UserDAO.getCurrentUser().getUsername());
        return customer;
    }

    /**
     * Navigate to Main View
     * @param actionEvent
     */
    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        try {
            NavigationHelper.mainView(actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error Navigating to Main View");
        }
    }

    /**
     * Validates Input Fields.
     * @return boolean
     */
    private boolean validFields() {
        ArrayList<String> errorFields = new ArrayList<>();
        boolean emptyName = nameTextField.getText().isEmpty();
        if (emptyName) {
            errorFields.add("Name Empty");
        }

        boolean emptyAddress = addressTextField.getText().isEmpty();
        if (emptyAddress) {
            errorFields.add("Address Empty");
        }

        Country country = countryComboField.getSelectionModel().getSelectedItem();
        if (country == null) {
            errorFields.add("Country not selected");
        }

        FirstLevelDivision firstLevelDivision = stateProvinceComboField.getSelectionModel().getSelectedItem();
        if (firstLevelDivision == null) {
            errorFields.add("State / Province not Selected");
        }

        boolean emptyPostalCode = postalCodeTextField.getText().isEmpty();
        if (emptyPostalCode) {
            errorFields.add("Postal Code Empty");
        }

        boolean emptyPhone = phoneNumberTextField.getText().isEmpty();
        if (emptyPhone) {
            errorFields.add("Phone Number Empty");
        }

        if (!errorFields.isEmpty()) {
            String errorMessages = errorFields.stream().map(error -> error + "\n").collect(Collectors.joining());
            ModalHelper.displayAlert(Alert.AlertType.ERROR, "Error", "Please Address Error(s)", errorMessages);
        }

        return errorFields.isEmpty();
    }

    private void handleComboBox() {
        countryComboField.setItems(CountryDAO.getAllCountries());

        countryComboField.setOnAction(actionEvent -> {
            Country selectedCountry = countryComboField.getSelectionModel().getSelectedItem();
            ObservableList<FirstLevelDivision> firstLevelDivisions = FirstLevelDivisionDAO.getByCountryID(selectedCountry.getCountryID());
            stateProvinceComboField.setItems(firstLevelDivisions);
        });
    }


}
