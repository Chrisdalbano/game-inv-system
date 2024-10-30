package ui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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

        Button btnLoadItemsFile = new Button("Load Items File");
        btnLoadItemsFile.setOnAction(e -> loadItemsFile());

        menu.getChildren().add(btnLoadItemsFile);
    }

    public HBox getMenu() {
        return menu;
    }

    private void loadItemsFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Items File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String filename = selectedFile.getAbsolutePath();
            manager.setFilename(filename);

            if (manager.loadItemsFromFile()) {
                DialogHelper.showInformation("Items loaded successfully from " + filename);
                main.refreshUI();
            } else {
                DialogHelper.showError("Failed to load items from file.");
            }
        }
    }
}