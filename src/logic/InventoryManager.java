package logic;
import data.Inventory;
import data.Floor;
import data.Item;
import utils.InputValidator;
import java.util.List;


public class InventoryManager {
    private Inventory inventory;
    private Floor floor;

    public InventoryManager(Inventory inventory, Floor floor) {
        this.inventory = inventory;
        this.floor = floor;
    }

    public void pickUpItem(int id) {
        Item item = floor.getItemById(id);
        if (item != null) {
            if (inventory.addItem(item)) {
                floor.removeItem(id);
                System.out.println("Item picked up and added to inventory.");
            }
        } else {
            System.out.println("Item not found on the floor.");
        }
    }

    public void dropItem(int id) {
        Item item = inventory.getItemById(id);
        if (item != null) {
            inventory.removeItem(id);
            floor.addItem(item);
            System.out.println("Item dropped from inventory to floor.");
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    public void displayInventory() {
        inventory.displayItems();
    }

    public void displayFloor() {
        floor.displayItems();
    }

    public void updateItemInInventory(int id, String name, String type, int quantity, double weight, String description) {
        Item item = inventory.getItemById(id);
        if (item != null) {
            item.updateDetails(name, type, quantity, weight, description);
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    public void saveFloorItemsToFile(String filename) {
        floor.saveItemsToFile(filename);
    }

    public void filterAndDisplayItems(String type) {
        List<Item> filteredItems = inventory.filterItemsByType(type);
        if (filteredItems.isEmpty()) {
            System.out.println("No items of type: " + type + " found in the inventory.");
        } else {
            for (Item item : filteredItems) {
                System.out.println(item.getDetails());
            }
        }
    }

    public void manuallyAddItemToFloor() {
        int id = InputValidator.getValidInt("Enter item ID: ");
        String name = InputValidator.getValidString("Enter item name: ");
        String type = InputValidator.getValidString("Enter item type: ");
        int quantity = InputValidator.getValidInt("Enter item quantity: ");
        double weight = InputValidator.getValidDouble("Enter item weight: ");
        String description = InputValidator.getValidString("Enter item description: ");

        Item newItem = new Item(id, name, type, quantity, weight, description);
        floor.addItem(newItem);
        System.out.println("Item added to the floor successfully.");
    }
}
