package c195.controller;

import c195.util.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    public void navigateToAppointmentView(ActionEvent event) throws IOException {
        NavigationHelper.manageAppointmentView(event);
    }

    @FXML
    public void navigateToCustomerView(ActionEvent event) throws IOException {
        NavigationHelper.manageCustomerView(event);
    }

    @FXML
    public void navigateToReportView(ActionEvent event) throws IOException {
        NavigationHelper.reportView(event);
    }
}
