package ui;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * DialogHelper class provides utility methods for displaying different types of dialog boxes,
 * such as error messages, information messages, and text input prompts.
 * Utilizes JavaFX Alert and TextInputDialog for creating dialogs.
 */

public class DialogHelper {
    /**
     * Displays an error message in an alert dialog.
     *
     * @param message the error message to display
     */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static String prompt(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setContentText(message);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
