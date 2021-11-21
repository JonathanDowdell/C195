package c195.util;

import c195.Main;
import c195.controller.ManageAppointmentViewController;
import c195.controller.ManageCustomerViewController;
import c195.model.Appointment;
import c195.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationHelper {
    public static void mainView(ActionEvent event) throws IOException {
        navigator(event, "view/main-view.fxml");
    }

    public static void customerView(ActionEvent event, Customer customer) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/manage-customer-view.fxml"));
        Parent parent = fxmlLoader.load();
        if (customer != null) {
            ManageCustomerViewController manageCustomerViewController = fxmlLoader.getController();
            manageCustomerViewController.loadCustomer(customer);
        }
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void manageAppointmentView(ActionEvent event, Appointment appointment) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/manage-appointment-view.fxml"));
        Parent parent = fxmlLoader.load();
        if (appointment != null) {
            ManageAppointmentViewController manageAppointmentViewController = fxmlLoader.getController();
            // TODO: 11/21/2021 Add appointment loader
        }
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public static void navigator(ActionEvent event, String designation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(designation));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
