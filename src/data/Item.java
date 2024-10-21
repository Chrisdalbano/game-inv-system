package data;
import java.util.*;

public class Item {
    private final int id;
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

    public void updateDetails(String name, String type, int quantity, double weight, String description) {
        if (name != null && !name.isEmpty()) this.name = name;
        if (type != null && !type.isEmpty()) this.type = type;
        if (quantity >= 0) this.quantity = quantity;
        if (weight >= 0) this.weight = weight;
        if (description != null && !description.isEmpty()) this.description = description;
    }

    public String getDetails() {
        return String.format("ID: %d, Name: %s, Type: %s, Quantity: %d, Weight: %.2f, Description: %s", id, name, type, quantity, weight, description);
    }
}


