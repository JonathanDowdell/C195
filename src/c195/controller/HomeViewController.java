package c195.controller;

import c195.util.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {

    @FXML
    public Button reportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Navigates to AppointmentView.
     * @param event ActionEvent
     * @throws IOException
     */
    @FXML
    public void navigateToAppointmentView(ActionEvent event) throws IOException {
        NavigationHelper.manageAppointmentView(event);
    }

    /**
     * Navigates to CustomerView.
     * @param event ActionEvent
     * @throws IOException
     */
    @FXML
    public void navigateToCustomerView(ActionEvent event) throws IOException {
        NavigationHelper.manageCustomerView(event);
    }

    /**
     * Navigates to ReportView
     * @param event ActionEvent
     * @throws IOException
     */
    @FXML
    public void navigateToReportView(ActionEvent event) throws IOException {
        NavigationHelper.reportView(event);
    }
}
