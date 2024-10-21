package logic;

import data.Floor;
import data.Inventory;
import data.Item;
import exceptions.ItemNotFoundException;
import utils.InputValidator;

import java.util.List;

public class InventoryManager {
    private Inventory inventory;
    private Floor floor;
    private String filename;

    public InventoryManager(Inventory inventory, Floor floor, String filename) {
        this.inventory = inventory;
        this.floor = floor;
        this.filename = filename;
    }

    // Pick up an item from the floor and add it to the inventory
    public void pickUpItem(int id) throws ItemNotFoundException {
        Item item = floor.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found on the floor.");
        }
        if (inventory.addItem(item)) {
            floor.removeItem(id);
            System.out.println("Item picked up and added to inventory.");
        } else {
            System.out.println("Could not pick up item. Exceeds inventory weight capacity.");
        }
    }

    // Drop an item from inventory to the floor
    // Drop an item from inventory to the floor
    public void dropItem(int id) throws ItemNotFoundException {
        Item item = inventory.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found in inventory.");
        }
        inventory.removeItem(id);
        floor.addItem(item);
        System.out.println("Item dropped from inventory to floor.");
    }


    // Display all items in the inventory
    public void displayInventory() {
        inventory.displayItems();
    }

    // Display all items on the floor
    public void displayFloor() {
        floor.displayItems();
    }

    // Update an item in the inventory with new details
    public void updateItemInInventory(int id, String name, String type, int quantity, double weight, String description) throws ItemNotFoundException {
        Item item = inventory.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found in inventory.");
        }
        item.updateDetails(name, type, quantity, weight, description);
        System.out.println("Item updated successfully.");
    }

    // Save all items to a file (including both floor and inventory)
    public void saveItemsToFile() {
        floor.saveItemsToFile(filename); // Save items on the floor to file
        inventory.saveItemsToFile(filename); // Save items in the inventory to file
    }

    // Filter and display items by type in the inventory
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

    // Manually add an item to the floor with user input
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

    // Manually add an item to the inventory with user input
    public void manuallyAddItemToInventory() {
        int id = InputValidator.getValidInt("Enter item ID: ");
        String name = InputValidator.getValidString("Enter item name: ");
        String type = InputValidator.getValidString("Enter item type: ");
        int quantity = InputValidator.getValidInt("Enter item quantity: ");
        double weight = InputValidator.getValidDouble("Enter item weight: ");
        String description = InputValidator.getValidString("Enter item description: ");

        Item newItem = new Item(id, name, type, quantity, weight, description);
        if (inventory.addItem(newItem)) {
            System.out.println("Item added to inventory successfully.");
        } else {
            System.out.println("Could not add item to inventory. Exceeds weight capacity.");
        }
    }

    // Manually remove an item from the inventory
    public void manuallyRemoveItemFromInventory() throws ItemNotFoundException {
        int id = InputValidator.getValidInt("Enter item ID to remove from inventory: ");
        if (!inventory.removeItem(id)) {
            throw new ItemNotFoundException("Item with ID " + id + " not found in inventory.");
        }
        System.out.println("Item removed from inventory successfully.");
    }
}
