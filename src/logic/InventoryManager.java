package logic;

import data.Floor;
import data.Inventory;
import data.Item;
import exceptions.ItemNotFoundException;

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

    // Set the filename (used when loading items from file)
    public void setFilename(String filename) {
        this.filename = filename;
    }

    // Loads items from the file into the floor
    public boolean loadItemsFromFile() {
        try {
            return floor.loadItemsFromFile(filename);
        } catch (Exception e) {
            System.out.println("Error loading items from file: " + e.getMessage());
            return false;
        }
    }

    // Get a list of items in the inventory
    public List<Item> getInventoryItems() {
        return inventory.getItems();
    }

    // Get a list of items on the floor
    public List<Item> getFloorItems() {
        return floor.getItems();
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
    public void dropItem(int id) throws ItemNotFoundException {
        Item item = inventory.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found in inventory.");
        }
        inventory.removeItem(id);
        floor.addItem(item);
        System.out.println("Item dropped from inventory to floor.");
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

    // Save all items to a file (both floor and inventory)
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
}
