package data;
import java.util.*;

public class Item {
    private int id;
    private String name;
    private String type;
    private int quantity;
    private double weight;
    private String description;

    public Item(int id, String name, String type, int quantity, double weight, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.weight = weight;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    public String getDetails() {
        return String.format("ID: %d, Name: %s, Type: %s, Quantity: %d, Weight: %.2f, Description: %s", id, name, type, quantity, weight, description);
    }
}

