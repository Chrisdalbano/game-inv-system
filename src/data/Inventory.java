package data;
import java.util.*;

public class Inventory {
    private List<Item> items;
    private double maxWeightCapacity;
    private double currentWeight;

    public Inventory(double maxWeightCapacity) {
        this.items = new ArrayList<>();
        this.maxWeightCapacity = maxWeightCapacity;
        this.currentWeight = 0.0;
    }

    public boolean addItem(Item item) {
        double newTotalWeight = currentWeight + (item.getWeight() * item.getQuantity());
        if (newTotalWeight > maxWeightCapacity) {
            System.out.println("Cannot add item. Exceeds weight capacity.");
            return false;
        }
        items.add(item);
        currentWeight = newTotalWeight;
        return true;
    }

    public boolean removeItem(int id) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getId() == id) {
                currentWeight -= (item.getWeight() * item.getQuantity());
                iterator.remove();
                return true;
            }
        }
        System.out.println("Item not found in inventory.");
        return false;
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            items.sort(Comparator.comparing(Item::getName)); // Default sorting by name
            for (Item item : items) {
                System.out.println(item.getDetails());
            }
        }
        System.out.println(String.format("Current Weight: %.2f / %.2f", currentWeight, maxWeightCapacity));
    }

    public Item getItemById(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
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

    public double getCurrentWeight() {
        return currentWeight;
    }
}

