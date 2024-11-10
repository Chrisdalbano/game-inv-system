package logic;

import data.Floor;
import data.Inventory;
import data.Item;
import exceptions.ItemNotFoundException;

import java.sql.*;
import java.util.List;

public class InventoryManager {
    private Inventory inventory;
    private Floor floor;
    private String dbFilePath; // Path to the SQLite database

    public InventoryManager(Inventory inventory, Floor floor, String dbFilePath) {
        this.inventory = inventory;
        this.floor = floor;
        this.dbFilePath = dbFilePath;
    }

    // Establish a connection to the SQLite database
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:" + dbFilePath;
        return DriverManager.getConnection(url);
    }

    // Load items from the database into the inventory and floor
    public boolean loadItemsFromDatabase() {
        if (dbFilePath == null || dbFilePath.isEmpty()) {
            System.out.println("Database file path is not set.");
            return false;
        }

        try (Connection conn = connect()) {
            loadItemsFromTable(conn, "inventory_items", inventory.getItems());
            loadItemsFromTable(conn, "floor_items", floor.getItems());

            // Debug: print loaded items
            System.out.println("Inventory items loaded: " + inventory.getItems().size());
            System.out.println("Floor items loaded: " + floor.getItems().size());

            return true;
        } catch (SQLException e) {
            System.out.println("Error loading items from database: " + e.getMessage());
            return false;
        }
    }


    // Helper method to load items from a specific table into an item container
    private void loadItemsFromTable(Connection conn, String tableName, List<Item> itemContainer) throws SQLException {
        itemContainer.clear(); // Make sure to clear the list before loading
        String sql = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int quantity = rs.getInt("quantity");
                double weight = rs.getDouble("weight");
                String description = rs.getString("description");

                // Create a new Item and add it to the container
                Item item = new Item(id, name, type, quantity, weight, description);
                System.out.println("Loaded item: ID=" + id + ", Name=" + name); // Debug

                // Use addItem() to ensure it's properly added
                if (tableName.equals("floor_items")) {
                    floor.addItem(item); // Add directly to the Floor object
                } else {
                    inventory.addItem(item); // Add to the Inventory object
                }
            }
        }
    }


    // Save all items to the database
    public void saveItemsToDatabase() {
        if (dbFilePath == null || dbFilePath.isEmpty()) {
            System.out.println("Database file path is not set.");
            return;
        }

        try (Connection conn = connect()) {
            clearTable(conn, "inventory_items");
            clearTable(conn, "floor_items");

            saveItemsToTable(conn, "inventory_items", inventory.getItems());
            saveItemsToTable(conn, "floor_items", floor.getItems());
        } catch (SQLException e) {
            System.out.println("Error saving items to database: " + e.getMessage());
        }
    }

    // Helper method to clear a table
    private void clearTable(Connection conn, String tableName) throws SQLException {
        String sql = "DELETE FROM " + tableName;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    // Helper method to save items to a specific table
    private void saveItemsToTable(Connection conn, String tableName, List<Item> items) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (id, name, type, quantity, weight, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Item item : items) {
                pstmt.setInt(1, item.getId());
                pstmt.setString(2, item.getName());
                pstmt.setString(3, item.getType());
                pstmt.setInt(4, item.getQuantity());
                pstmt.setDouble(5, item.getWeight());
                pstmt.setString(6, item.getDescription());
                pstmt.executeUpdate();
            }
        }
    }

    // Method to update an item's details in the inventory and database
    public void updateItemInInventory(int id, String name, String type, int quantity, double weight, String description) throws ItemNotFoundException {
        Item item = inventory.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found in inventory.");
        }

        item.updateDetails(name, type, quantity, weight, description);

        String sql = "UPDATE inventory_items SET name = ?, type = ?, quantity = ?, weight = ?, description = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, weight);
            pstmt.setString(5, description);
            pstmt.setInt(6, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new ItemNotFoundException("Item with ID " + id + " not found in the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating item in database: " + e.getMessage());
        }
    }

    public List<Item> getInventoryItems() {
        return inventory.getItems();
    }

    public List<Item> getFloorItems() {
        return floor.getItems();
    }

    public void pickUpItem(int id) throws ItemNotFoundException {
        Item item = floor.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found on the floor.");
        }
        if (inventory.addItem(item)) {
            floor.removeItem(id);
            saveItemsToDatabase();
            System.out.println("Item picked up and added to inventory.");
        } else {
            System.out.println("Could not pick up item. Exceeds inventory weight capacity.");
        }
    }

    public void dropItem(int id) throws ItemNotFoundException {
        Item item = inventory.getItemById(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with ID " + id + " not found in inventory.");
        }
        inventory.removeItem(id);
        floor.addItem(item);
        saveItemsToDatabase();
        System.out.println("Item dropped from inventory to floor.");
    }

    public void setDatabaseFilePath(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }
}
