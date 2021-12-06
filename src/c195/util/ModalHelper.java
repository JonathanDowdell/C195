package c195.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.stage.Screen;

/**
 * @author Jonathan Dowdell
 */
public class ModalHelper {

    /**
     *
     * @param alertType Type of Alert.
     * @param text Alert Header
     * @return Alert object for further use.
     */
    public static Alert displayAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(text);
        alert.showAndWait();
        return alert;
    }

    public static Alert displayAlert(Alert.AlertType alertType, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
//        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        alert.setX((primScreenBounds.getWidth() - alert.getWidth()) / 2);
//        alert.setY((primScreenBounds.getHeight() - alert.getHeight()) / 2);
        return alert;
    }

    /**
     *
     * @param alertType Type of Alert.
     * @param title Alert Title
     * @param header Alert Header
     * @param content Alert Content
     * @return Alert object for further use.
     */
    public static Alert displayAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert nullAlert = new Alert(alertType);
        nullAlert.setTitle(title);
        nullAlert.setHeaderText(header);
        nullAlert.setContentText(content);
        nullAlert.showAndWait();
        return nullAlert;
    }
}