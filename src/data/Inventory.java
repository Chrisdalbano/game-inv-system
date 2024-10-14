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
        for (Item item : items) {
            if (item.getId() == id) {
                currentWeight -= (item.getWeight() * item.getQuantity());
                items.remove(item);
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

    public double getCurrentWeight() {
        return currentWeight;
    }
}
