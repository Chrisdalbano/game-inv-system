package data;

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
    // Getters for each property
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

    // Setters for each property
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public void setType(String type) {
        if (type != null && !type.isEmpty()) {
            this.type = type;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public void setWeight(double weight) {
        if (weight >= 0) {
            this.weight = weight;
        }
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        }
    }

    // Method to update multiple fields at once
    public void updateDetails(String name, String type, int quantity, double weight, String description) {
        setName(name);
        setType(type);
        setQuantity(quantity);
        setWeight(weight);
        setDescription(description);
    }

    public String getDetails() {
        return String.format("ID: %d, Name: %s, Type: %s, Quantity: %d, Weight: %.2f, Description: %s", id, name, type, quantity, weight, description);
    }

    // Use semicolons as the delimiter for file saving.
    public String toFileString() {
        return String.format("%d;%s;%s;%d;%.2f;%s", id, name, type, quantity, weight, description);
    }
}
