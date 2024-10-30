package ui;

import data.Item;
import exceptions.ItemNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import logic.InventoryManager;
import utils.IconHelper;

import java.util.Comparator;
import java.util.stream.Collectors;

public class InventoryPane {
    private InventoryManager manager;
    private Main main;
    private VBox pane;
    private GridPane inventoryGrid;
    private ObservableList<Item> inventoryItems;
    private ChoiceBox<String> categoryChoiceBox;
    private ChoiceBox<String> sortChoiceBox;
    private TextField filterTextField;
    private ScrollPane scrollPane;

    public InventoryPane(InventoryManager manager, Main main) {
        this.manager = manager;
        this.main = main;
        this.pane = new VBox(15);
        this.pane.setPadding(new Insets(20));
        this.pane.setAlignment(Pos.TOP_CENTER);

        this.inventoryGrid = new GridPane();
        this.inventoryGrid.setHgap(10);
        this.inventoryGrid.setVgap(10);
        this.inventoryGrid.setAlignment(Pos.CENTER);

        // Wrap inventoryGrid in ScrollPane and add it to pane
        this.scrollPane = new ScrollPane(inventoryGrid);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setStyle("-fx-background: #2c2c2c; -fx-background-color: #2c2c2c;");

        pane.getChildren().addAll(new Label("Inventory"), createControlPanel(), scrollPane, createDropButton());

        refresh();
    }

    public VBox getPane() {
        return pane;
    }

    private HBox createControlPanel() {
        HBox controlPanel = new HBox(15);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setPadding(new Insets(10, 0, 10, 0));

        sortChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("ID", "Name", "Quantity", "Weight"));
        sortChoiceBox.setValue("ID");
        sortChoiceBox.setOnAction(e -> sortItems());

        categoryChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Select Category", "Weapon", "Armor", "Consumable", "Scroll", "Misc"));
        categoryChoiceBox.getSelectionModel().selectFirst();
        categoryChoiceBox.setOnAction(e -> filterItems());

        filterTextField = new TextField();
        filterTextField.setPromptText("Filter by name...");
        filterTextField.textProperty().addListener((obs, oldText, newText) -> filterItems());

        Button clearButton = new Button("Clear Filter");
        clearButton.setOnAction(e -> clearFilter());

        controlPanel.getChildren().addAll(new Label("Sort by:"), sortChoiceBox, categoryChoiceBox, filterTextField, clearButton);
        return controlPanel;
    }

    private Button createDropButton() {
        Button dropButton = new Button("Drop Item to Floor");
        dropButton.setOnAction(e -> dropSelectedItem());
        return dropButton;
    }

    private void dropSelectedItem() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Drop Item");
        dialog.setHeaderText("Drop Item to Floor");
        dialog.setContentText("Enter the ID of the item to drop:");

        dialog.showAndWait().ifPresent(itemIdStr -> {
            try {
                int itemId = Integer.parseInt(itemIdStr);
                manager.dropItem(itemId);
                main.refreshUI(); // Refresh both inventory and floor UI
            } catch (NumberFormatException e) {
                showAlert("Invalid ID", "Please enter a valid integer ID.");
            } catch (ItemNotFoundException e) {
                showAlert("Item Not Found", e.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void sortItems() {
        String sortBy = sortChoiceBox.getValue();
        Comparator<Item> comparator = switch (sortBy) {
            case "Name" -> Comparator.comparing(Item::getName);
            case "Quantity" -> Comparator.comparingInt(Item::getQuantity);
            case "Weight" -> Comparator.comparingDouble(Item::getWeight);
            default -> Comparator.comparingInt(Item::getId);
        };
        inventoryItems.sort(comparator);
        refreshGrid();
    }

    private void filterItems() {
        String filterText = filterTextField.getText().toLowerCase();
        String category = categoryChoiceBox.getValue();
        inventoryItems.setAll(manager.getInventoryItems().stream()
                .filter(item -> (filterText.isEmpty() || item.getName().toLowerCase().contains(filterText)) &&
                        (!"Select Category".equals(category) && category != null || item.getType().equalsIgnoreCase(category)))
                .collect(Collectors.toList()));
        refreshGrid();
    }

    private void clearFilter() {
        filterTextField.clear();
        categoryChoiceBox.getSelectionModel().selectFirst();
        inventoryItems.setAll(manager.getInventoryItems());
        refreshGrid();
    }

    private void refreshGrid() {
        inventoryGrid.getChildren().clear();
        int col = 0, row = 0;
        for (Item item : inventoryItems) {
            ImageView icon = new ImageView(IconHelper.getIconForCategory(item.getType()));
            icon.setFitWidth(50);
            icon.setFitHeight(50);

            Label idLabel = new Label("ID: " + item.getId());
            Label nameLabel = new Label(item.getName());
            Label qtyLabel = new Label("Qty: " + item.getQuantity());

            VBox itemBox = new VBox(idLabel, icon, nameLabel, qtyLabel);
            itemBox.setAlignment(Pos.CENTER);
            itemBox.setPadding(new Insets(5));
            itemBox.setOnMouseClicked(e -> main.showItemDetails(item));

            inventoryGrid.add(itemBox, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    public void refresh() {
        inventoryItems = FXCollections.observableArrayList(manager.getInventoryItems());
        refreshGrid();
    }
}
