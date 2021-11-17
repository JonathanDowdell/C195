package c195.controller;

import c195.dao.CountryDAO;
import c195.model.Country;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {
    public TextField idTextField;
    public TextField nameTextField;
    public TextField addressTextField;
    public ComboBox<String> cityComboField;
    public ComboBox<Country> countryComboField;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboField.setItems(CountryDAO.getAllCountries());

    }
}
