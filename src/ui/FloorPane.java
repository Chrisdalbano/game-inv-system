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

import java.sql.SQLException;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * FloorPane class manages the UI for displaying and interacting with items placed on the floor.
 * It provides features such as sorting, filtering, crafting new items, and deleting items.
 * The items are displayed in a grid layout, with options for sorting and filtering via a control panel.
 */
public class FloorPane {
    private InventoryManager manager;
    private Main main;
    private VBox pane;
    private GridPane floorGrid;
    private ObservableList<Item> floorItems;
    private ChoiceBox<String> sortChoiceBox;
    private ChoiceBox<String> categoryChoiceBox;
    private TextField filterTextField;
    private ScrollPane scrollPane;

    public FloorPane(InventoryManager manager, Main main) {
        this.manager = manager;
        this.main = main;
        this.pane = new VBox(15);
        this.pane.setPadding(new Insets(20));
        this.pane.setAlignment(Pos.TOP_CENTER);

        this.floorGrid = new GridPane();
        this.floorGrid.setHgap(10);
        this.floorGrid.setVgap(10);
        this.floorGrid.setAlignment(Pos.CENTER);

        this.scrollPane = new ScrollPane(floorGrid);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setStyle("-fx-background: #2c2c2c; -fx-background-color: #2c2c2c;");

        // Initialize ObservableList
        floorItems = FXCollections.observableArrayList();

        // Add components to the pane
        pane.getChildren().addAll(new Label("Floor Items"), createControlPanel(), scrollPane);

        // Pick Up Item button
        Button btnPickUp = new Button("Pick Up Item");
        btnPickUp.setOnAction(e -> pickUpItem());
        pane.getChildren().add(btnPickUp);

        refresh(); // Refresh the grid to display items
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
        categoryChoiceBox.setOnAction(e -> filterByCategory());

        filterTextField = new TextField();
        filterTextField.setPromptText("Filter by name...");
        filterTextField.textProperty().addListener((obs, oldText, newText) -> filterItems());

        Button craftButton = new Button("Craft Item");
        craftButton.setOnAction(e -> craftItem());

        Button deleteButton = new Button("Delete Item");
        deleteButton.setOnAction(e -> deleteItem());

        Button clearButton = new Button("Clear Filter");
        clearButton.setOnAction(e -> clearFilter());

        controlPanel.getChildren().addAll(new Label("Sort by:"), sortChoiceBox, new Label("Category:"), categoryChoiceBox, filterTextField, craftButton, deleteButton, clearButton);
        return controlPanel;
    }

    private void sortItems() {
        String sortBy = sortChoiceBox.getValue();
        Comparator<Item> comparator = switch (sortBy) {
            case "Name" -> Comparator.comparing(Item::getName);
            case "Quantity" -> Comparator.comparingInt(Item::getQuantity);
            case "Weight" -> Comparator.comparingDouble(Item::getWeight);
            default -> Comparator.comparingInt(Item::getId);
        };
        floorItems.sort(comparator);
        refreshGrid();
    }

    private void filterByCategory() {
        String selectedCategory = categoryChoiceBox.getValue();
        if ("Select Category".equals(selectedCategory)) {
            floorItems.setAll(manager.getFloorItems());
        } else {
            floorItems.setAll(manager.getFloorItems().stream()
                    .filter(item -> item.getType().equalsIgnoreCase(selectedCategory))
                    .collect(Collectors.toList()));
        }
        refreshGrid();
    }

    private void filterItems() {
        String filterText = filterTextField.getText().toLowerCase();
        if (filterText.isEmpty()) {
            floorItems.setAll(manager.getFloorItems());
        } else {
            floorItems.setAll(manager.getFloorItems().stream()
                    .filter(item -> item.getName().toLowerCase().contains(filterText))
                    .collect(Collectors.toList()));
        }
        refreshGrid();
    }

    private void clearFilter() {
        filterTextField.clear();
        categoryChoiceBox.getSelectionModel().selectFirst();
        floorItems.setAll(manager.getFloorItems());
        refreshGrid();
    }

    private void refreshGrid() {
        floorGrid.getChildren().clear();
        int col = 0, row = 0;
        for (Item item : floorItems) {
            ImageView icon = new ImageView(IconHelper.getIconForCategory(item.getType()));
            icon.setFitWidth(40);
            icon.setFitHeight(40);

            Label idLabel = new Label("ID: " + item.getId());
            Label nameLabel = new Label(item.getName());
            Label qtyLabel = new Label("Qty: " + item.getQuantity());

            VBox itemBox = new VBox(5, idLabel, icon, nameLabel, qtyLabel);
            itemBox.setAlignment(Pos.CENTER);
            itemBox.setPadding(new Insets(5));
            itemBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 5; -fx-background-radius: 5;");

            itemBox.setOnMouseClicked(e -> main.showItemDetails(item));

            floorGrid.add(itemBox, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    public void refresh() {
        floorItems.setAll(manager.getFloorItems());
        refreshGrid();
    }

    private void craftItem() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Craft Item");
        idDialog.setHeaderText("Enter ID for the new item:");

        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);

                TextInputDialog nameDialog = new TextInputDialog();
                nameDialog.setTitle("Craft Item");
                nameDialog.setHeaderText("Enter Name for the new item:");

                nameDialog.showAndWait().ifPresent(name -> {
                    ChoiceDialog<String> typeDialog = new ChoiceDialog<>("Misc", "Weapon", "Armor", "Consumable", "Scroll");
                    typeDialog.setTitle("Craft Item");
                    typeDialog.setHeaderText("Select the type (category) for the new item:");

                    typeDialog.showAndWait().ifPresent(type -> {
                        TextInputDialog descDialog = new TextInputDialog();
                        descDialog.setTitle("Craft Item");
                        descDialog.setHeaderText("Enter a description for the new item:");

                        descDialog.showAndWait().ifPresent(description -> {
                            try {
                                manager.craftItemOnFloor(id, name, type, 1, 1.0, description);
                                main.refreshUI();
                            } catch (SQLException e) {
                                DialogHelper.showError("Error crafting item: " + e.getMessage());
                            }
                        });
                    });
                });
            } catch (Exception e) {
                DialogHelper.showError("Error crafting item: " + e.getMessage());
            }
        });
    }

    private void deleteItem() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Item");
        dialog.setHeaderText("Enter the ID of the item to delete:");

        dialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                manager.deleteFloorItem(id);
                main.refreshUI();
            } catch (Exception e) {
                DialogHelper.showError("Error deleting item: " + e.getMessage());
            }
        });
    }

    private void pickUpItem() {
        String itemIdStr = DialogHelper.prompt("Enter Item ID to pick up:");
        if (itemIdStr != null) {
            try {
                int itemId = Integer.parseInt(itemIdStr);
                manager.pickUpItem(itemId);
                main.refreshUI();
            } catch (Exception e) {
                DialogHelper.showError("Failed to pick up item: " + e.getMessage());
            }
        }
    }
}
