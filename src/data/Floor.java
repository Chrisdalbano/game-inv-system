package data;

import exceptions.InvalidItemFormatException;

import java.util.*;
import java.io.*;

public class Floor {
    private List<Item> items;

    public Floor() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(int id) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getId() == id) {
                iterator.remove();
                return true;
            }
        }
        System.out.println("Item not found on the floor.");
        return false;
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items on the floor.");
        } else {
            for (Item item : items) {
                System.out.println(item.getDetails());
            }
        }
    }

    public Item getItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items); // Returning a copy to prevent external modifications.
    }

    public boolean loadItemsFromFile(String filename) throws InvalidItemFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String type = parts[2].trim();
                        int quantity = Integer.parseInt(parts[3].trim());
                        double weight = Double.parseDouble(parts[4].trim());
                        String description = parts[5].trim();
                        Item item = new Item(id, name, type, quantity, weight, description);
                        addItem(item);
                    } catch (NumberFormatException e) {
                        throw new InvalidItemFormatException("Invalid data format in items file: " + line);
                    }
                } else {
                    throw new InvalidItemFormatException("Invalid item format in items file: " + line);
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error reading items from file: " + e.getMessage());
            return false;
        }
    }

    public void saveItemsToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Item item : items) {
                bw.write(item.toFileString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving items to file: " + e.getMessage());
        }
    }


    public boolean manuallyAddItemToFloor(String filename, int id, String name, String type, int quantity, double weight, String description) {
        Item item = new Item(id, name, type, quantity, weight, description);
        addItem(item);
        saveItemsToFile(filename);
        return true;
    }

    public boolean manuallyRemoveItemFromFloor(String filename, int id) {
        boolean removed = removeItem(id);
        if (removed) {
            saveItemsToFile(filename);
        }
        return removed;
    }
}
