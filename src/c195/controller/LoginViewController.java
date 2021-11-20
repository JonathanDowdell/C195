package c195.controller;


import c195.dao.UserDAO;
import c195.util.Logger;
import c195.util.NavigationHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML
    public TextField userIDTextField;

    @FXML
    public TextField passwordTextField;

    @FXML
    public AnchorPane root;

    @FXML
    public Label zoneIDLabel;

    private String alertTitle;
    private String alertHeader;
    private String alertContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("Languages", locale);
        String username = resourceBundle.getString("username");
        String password = resourceBundle.getString("password");
        zoneIDLabel.setText(locale.toString());
        userIDTextField.setPromptText(username);
        passwordTextField.setPromptText(password);
        alertTitle = resourceBundle.getString("alertTitle");
        alertHeader = resourceBundle.getString("alertHeader");
        alertContext = resourceBundle.getString("alertContext");
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        String userID = userIDTextField.getText();
        String password = passwordTextField.getText();
        Boolean validUser = UserDAO.login(userID, password);
        Logger.log(userID, validUser, "Login Attempt");
        if (validUser) {
            System.out.println("Logged In");
            NavigationHelper.mainView(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeader);
            alert.setContentText(alertContext);
            alert.showAndWait();
        }
    }

}
