package c195.util;

import c195.Main;
import c195.controller.ManageAppointmentViewController;
import c195.controller.ManageCustomerViewController;
import c195.model.Appointment;
import c195.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Jonathan Dowdell
 */
public class NavigationHelper {

    /**
     * Navigate to MainView.
     * @param event
     * @throws IOException
     */
    public static void mainView(ActionEvent event) throws IOException {
        navigator(event, "view/main-view.fxml");
    }

    /**
     * Navigate to CustomerView.
     * @param event
     * @param customer
     * @throws IOException
     */
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
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Navigate to Appointment View.
     * @param event
     * @param appointment
     * @throws IOException
     */
    public static void manageAppointmentView(ActionEvent event, Appointment appointment) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/manage-appointment-view.fxml"));
        Parent parent = fxmlLoader.load();
        if (appointment != null) {
            ManageAppointmentViewController manageAppointmentViewController = fxmlLoader.getController();
            manageAppointmentViewController.loadAppointment(appointment);
        }
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Navigate to Report View.
     * @param event
     * @throws IOException
     */
    public static void reportView(ActionEvent event) throws IOException {
        navigator(event, "view/report-view.fxml");
    }

    public static void navigator(ActionEvent event, String designation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(designation));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
