package ui;

import data.Item;
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

/**
 * FloorPane class manages the UI for displaying and interacting with items placed on the floor.
 * It provides features such as sorting, filtering, and picking up items to add to the inventory.
 * The items are displayed in a grid layout, with options for sorting and filtering via a control panel.
 */

public class FloorPane {
    /**
     * Constructs a FloorPane object and initializes the UI components and layout.
     * Sets up the control panel for sorting and filtering items, and initializes the grid for displaying items.
     *
     * @param manager the InventoryManager used to manage item data and interactions
     * @param main the Main class instance used for displaying item details and refreshing the UI
     */
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

        Button clearButton = new Button("Clear Filter");
        clearButton.setOnAction(e -> clearFilter());

        controlPanel.getChildren().addAll(new Label("Sort by:"), sortChoiceBox, new Label("Category:"), categoryChoiceBox, filterTextField, clearButton);
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
        // Use setAll to ensure ObservableList updates properly
        floorItems.setAll(manager.getFloorItems());
        if (floorItems.isEmpty()) {
            System.out.println("No floor items available.");
        } else {
            System.out.println("Floor items available: " + floorItems.size());
        }
        refreshGrid();
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
