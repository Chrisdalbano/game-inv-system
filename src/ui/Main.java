package ui;

import data.Inventory;
import data.Floor;
import data.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.InventoryManager;

import java.util.Objects;

public class Main extends Application {
    private InventoryManager manager;
    private Inventory inventory;
    private Floor floor;
    private InventoryPane inventoryPane;
    private FloorPane floorPane;
    private Label emptyMessageLabel;

    @Override
    public void start(Stage primaryStage) {
        inventory = new Inventory(50.0);
        floor = new Floor();
        manager = new InventoryManager(inventory, floor, "");

        // Create the main layout
        BorderPane root = new BorderPane();

        // Create the UI components
        TopMenu topMenu = new TopMenu(manager, primaryStage, this);
        inventoryPane = new InventoryPane(manager, this);
        floorPane = new FloorPane(manager, this);

        // Empty message label, centered with padding
        emptyMessageLabel = new Label("Your Inventory Management is empty, let's start by importing some items from a text file.");
        emptyMessageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
        emptyMessageLabel.setVisible(false);

        StackPane centerPane = new StackPane(emptyMessageLabel);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setStyle("-fx-padding: 20;");

        // Tabs for inventory and floor items
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab inventoryTab = new Tab("Inventory", inventoryPane.getPane());
        Tab floorTab = new Tab("Floor Items", floorPane.getPane());

        tabPane.getTabs().addAll(inventoryTab, floorTab);

        // Arrange components in the layout
        root.setTop(topMenu.getMenu());
        root.setCenter(tabPane);
        root.setBottom(centerPane);

        // Set up the primary stage
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/theme.css")).toExternalForm());
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        refreshUI();
    }

    public void refreshUI() {
        boolean isInventoryEmpty = inventory.getItems().isEmpty();
        boolean isFloorEmpty = floor.getItems().isEmpty();

        emptyMessageLabel.setVisible(isInventoryEmpty && isFloorEmpty);
        inventoryPane.refresh();
        floorPane.refresh();
    }

    public void showItemDetails(Item item) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Details");
        alert.setHeaderText("Details for Item ID: " + item.getId());
        alert.setContentText("Name: " + item.getName() + "\nType: " + item.getType() + "\nQuantity: " + item.getQuantity() +
                "\nWeight: " + item.getWeight() + "\nDescription: " + item.getDescription());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
