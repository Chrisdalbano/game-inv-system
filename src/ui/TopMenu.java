package ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.InventoryManager;
import java.io.File;

public class TopMenu {
    private InventoryManager manager;
    private Stage stage;
    private Main main;
    private HBox menu;

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
