package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class manages a collection of items and keeps track of the total weight of all items.
 * Provides methods to add, remove, update, and display items, and to save the items to a file.
 */

public class Inventory {
    private double maxWeightCapacity;
    private double currentWeight;
    private List<Item> items;

    /**
     * Constructs an Inventory object with a specified maximum weight capacity.
     *
     * @param maxWeightCapacity the maximum allowable weight for the inventory
     */

    public Inventory(double maxWeightCapacity) {
        this.maxWeightCapacity = maxWeightCapacity;
        this.currentWeight = 0;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        double itemWeight = item.getWeight() * item.getQuantity();
        double totalWeight = currentWeight + itemWeight;
        if (totalWeight <= maxWeightCapacity) {
            Item existingItem = getItemById(item.getId());
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                items.add(item);
            }
            currentWeight = totalWeight;
            return true;
        } else {
            System.out.println("Cannot add item. Exceeds maximum weight capacity.");
            return false;
        }
    }

    public boolean removeItem(int id) {
        Item item = getItemById(id);
        if (item != null) {
            currentWeight -= item.getWeight() * item.getQuantity();
            items.remove(item);
            return true;
        }
        return false;
    }

    public Item getItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void clearItems() {
        items.clear();
        currentWeight = 0;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public void updateItemDetails(int id, String name, String type, int quantity, double weight, String description) {
        Item item = getItemById(id);
        if (item != null) {
            currentWeight -= item.getWeight() * item.getQuantity();
            item.updateDetails(name, type, quantity, weight, description);
            currentWeight += item.getWeight() * item.getQuantity();
        }
    }

    public double getTotalWeight() {
        return currentWeight;
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            for (Item item : items) {
                System.out.println(item.getDetails());
            }
        }
    }

    public List<Item> filterItemsByType(String type) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getType().equalsIgnoreCase(type)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

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
