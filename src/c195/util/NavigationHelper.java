package c195.util;

import c195.Main;
import c195.controller.CustomerViewController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/customer-view.fxml"));
        Parent parent = fxmlLoader.load();
        if (customer != null) {
            CustomerViewController customerViewController = fxmlLoader.getController();
            customerViewController.loadCustomer(customer);
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
