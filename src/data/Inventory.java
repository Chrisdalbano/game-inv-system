package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private double maxWeightCapacity;
    private List<Item> items;

    public Inventory(double maxWeightCapacity) {
        this.maxWeightCapacity = maxWeightCapacity;
        this.items = new ArrayList<>();
    }

    // Adds an item to the inventory if the weight capacity allows
    public boolean addItem(Item item) {
        double totalWeight = getTotalWeight() + (item.getWeight() * item.getQuantity());
        if (totalWeight <= maxWeightCapacity) {
            items.add(item);
            return true; // Item added successfully
        } else {
            System.out.println("Cannot add item. Exceeds maximum weight capacity.");
            return false; // Item not added
        }
    }

    // Removes an item from the inventory by its ID
    public boolean removeItem(int id) {
        return items.removeIf(item -> item.getId() == id);
    }

    // Gets an item by its ID
    public Item getItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    // Returns a list of all items
    public List<Item> getItems() {
        return new ArrayList<>(items); // Returning a copy to prevent modification
    }

    // Gets items by their type
    public List<Item> getItemsByType(String type) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(type)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    // Gets the total weight of items in the inventory
    public double getTotalWeight() {
        double totalWeight = 0;
        for (Item item : items) {
            totalWeight += item.getWeight() * item.getQuantity();
        }
        return totalWeight;
    }

    // Displays all items in the inventory
    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            for (Item item : items) {
                System.out.println(item.getDetails());
            }
        }
    }

    // Filters items by type - returns a list of filtered items
    public List<Item> filterItemsByType(String type) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(type)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    // Method to save items to a file
    public void saveItemsToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Item item : items) {
                bw.write(item.toFileString() + "\n");
            }
            System.out.println("Inventory items saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving inventory items to file: " + e.getMessage());
        }
    }
}
