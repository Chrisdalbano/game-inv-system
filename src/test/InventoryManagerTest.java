package test;

import data.Floor;
import data.Inventory;
import data.Item;
import exceptions.ItemNotFoundException;
import logic.InventoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    private Inventory inventory;
    private Floor floor;
    private InventoryManager manager;
    private final String databaseFilePath = "test_items.db"; // Path to the test SQLite database

    @BeforeEach
    void setUp() {
        try {
            // Create a new SQLite database and add test data
            String url = "jdbc:sqlite:" + databaseFilePath;
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                // Create the items table
                stmt.execute("DROP TABLE IF EXISTS items");
                stmt.execute("CREATE TABLE items (" +
                        "id INTEGER PRIMARY KEY, " +
                        "name TEXT NOT NULL, " +
                        "type TEXT NOT NULL, " +
                        "quantity INTEGER NOT NULL, " +
                        "weight REAL NOT NULL, " +
                        "description TEXT NOT NULL)");

                // Insert test data
                stmt.execute("INSERT INTO items (id, name, type, quantity, weight, description) VALUES " +
                        "(101, 'Sword', 'Weapon', 2, 5.0, 'A sharp sword'), " +
                        "(102, 'Shield', 'Armor', 1, 7.5, 'A sturdy shield'), " +
                        "(103, 'Potion', 'Consumable', 5, 0.5, 'A health potion')");
            }

            // Initialize the InventoryManager with the test database
            inventory = new Inventory(50.0);
            floor = new Floor();
            manager = new InventoryManager(inventory, floor, databaseFilePath);

            // Load items from the database into the floor and inventory
            manager.loadItemsFromDatabase();
        } catch (Exception e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }

    @Test
    void pickUpItem() {
        try {
            // Pick up an item from the floor and add to the inventory
            manager.pickUpItem(101);
            assertEquals(1, inventory.getItems().size(), "Inventory should have 1 item after picking up.");
            assertEquals(2, floor.getItems().size(), "Floor should have 2 items after picking up one.");
            assertNotNull(inventory.getItemById(101), "Item with ID 101 should exist in inventory.");
        } catch (Exception e) {
            fail("Exception thrown during pickUpItem test: " + e.getMessage());
        }

        // Negative Case: Try to pick up an item that doesn't exist
        assertThrows(ItemNotFoundException.class, () -> manager.pickUpItem(999), "Expected ItemNotFoundException for non-existent item ID 999.");
    }

    @Test
    void dropItem() {
        try {
            // Pick up an item first, then drop it
            manager.pickUpItem(101);
            manager.dropItem(101);
            assertEquals(0, inventory.getItems().size(), "Inventory should be empty after dropping the item.");
            assertEquals(3, floor.getItems().size(), "Floor should have 3 items after dropping one back.");
            assertNotNull(floor.getItemById(101), "Item with ID 101 should exist on the floor.");
        } catch (Exception e) {
            fail("Exception thrown during dropItem test: " + e.getMessage());
        }

        // Negative Case: Try to drop an item that is not in the inventory
        assertThrows(ItemNotFoundException.class, () -> manager.dropItem(999), "Expected ItemNotFoundException for dropping a non-existent item ID 999.");
    }

    @Test
    void updateItemInInventory() {
        try {
            // Pick up an item and update its attributes
            manager.pickUpItem(101);
            manager.updateItemInInventory(101, "Great Sword", "Weapon", 3, 6.0, "An even sharper sword");
            Item updatedItem = inventory.getItemById(101);
            assertNotNull(updatedItem, "Updated item should exist in inventory.");
            assertEquals("Great Sword", updatedItem.getName(), "Item name should be updated.");
            assertEquals(3, updatedItem.getQuantity(), "Item quantity should be updated.");
            assertEquals(6.0, updatedItem.getWeight(), "Item weight should be updated.");
            assertEquals("An even sharper sword", updatedItem.getDescription(), "Item description should be updated.");
        } catch (Exception e) {
            fail("Exception thrown during updateItemInInventory test: " + e.getMessage());
        }

        // Negative Case: Try to update an item that doesn't exist
        assertThrows(ItemNotFoundException.class, () -> manager.updateItemInInventory(999, "Fake Sword", "Weapon", 5, 8.0, "Non-existent sword"), "Expected ItemNotFoundException for non-existent item ID 999.");
    }

//    @Test
//    void saveItemsToFile() {
//        try {
//            // Since we are not working with files anymore, we will just verify database updates
//            manager.pickUpItem(101);
//            manager.updateItemInInventory(101, "Great Sword", "Weapon", 3, 6.0, "An even sharper sword");
//            manager.saveItemToDatabase(inventory.getItemById(101)); // Save updated item to database
//
//            // Reload items from the database to verify changes were saved
//            manager.loadItemsFromDatabase();
//            Item updatedItem = inventory.getItemById(101);
//            assertNotNull(updatedItem, "Updated item should exist in inventory after reloading from database.");
//            assertEquals("Great Sword", updatedItem.getName(), "Item name should be updated after saving to database.");
//            assertEquals(3, updatedItem.getQuantity(), "Item quantity should be updated after saving to database.");
//            assertEquals(6.0, updatedItem.getWeight(), "Item weight should be updated after saving to database.");
//            assertEquals("An even sharper sword", updatedItem.getDescription(), "Item description should be updated after saving to database.");
//        } catch (Exception e) {
//            fail("Exception thrown during saveItemsToFile test: " + e.getMessage());
//        }
//    }
}
