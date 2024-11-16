package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.InventoryManager;
import java.io.File;

/**
 * TopMenu class creates the top menu bar for the Inventory Management System.
 * This class provides options to load items from an external database using a file chooser.
 * It interacts with the InventoryManager to load items and refreshes the UI upon successful loading.
 */

public class TopMenu {
    private InventoryManager manager;
    private Stage stage;
    private Main main;
    private HBox menu;

    /**
     * Constructs a TopMenu object and initializes the UI components.
     *
     * @param manager the InventoryManager used to handle data operations
     * @param stage the primary stage used for file chooser dialogs
     * @param main the Main class instance to facilitate UI refresh operations
     */

    public TopMenu(InventoryManager manager, Stage stage, Main main) {
        this.manager = manager;
        this.stage = stage;
        this.main = main;
        this.menu = new HBox(10);

        // Button to load items from the database
        Button btnLoadItemsDatabase = new Button("Load Items from Database");
        btnLoadItemsDatabase.setOnAction(e -> selectDatabaseFile());

        menu.getChildren().add(btnLoadItemsDatabase);
    }

    public HBox getMenu() {
        return menu;
    }

    // Method to open a file chooser and select a database file
    private void selectDatabaseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Database File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite Database Files", "*.sqlite"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String databaseFilePath = selectedFile.getAbsolutePath();
            manager.setDatabaseFilePath(databaseFilePath); // Set the database file path in the manager

            if (manager.loadItemsFromDatabase()) {
                DialogHelper.showInformation("Items loaded successfully from the database.");
                main.refreshUI(); // Refresh UI after loading items
            } else {
                DialogHelper.showError("Failed to load items from the database.");
            }
        }
    }
}
