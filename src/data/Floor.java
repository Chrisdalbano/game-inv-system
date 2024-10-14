package data;
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
        for (Item item : items) {
            if (item.getId() == id) {
                items.remove(item);
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

    public void loadItemsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String type = parts[2].trim();
                    int quantity = Integer.parseInt(parts[3].trim());
                    double weight = Double.parseDouble(parts[4].trim());
                    String description = parts[5].trim();
                    Item item = new Item(id, name, type, quantity, weight, description);
                    addItem(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading items from file: " + e.getMessage());
        }
    }
}