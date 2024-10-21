package test;

import data.Inventory;
import data.Floor;
import data.Item;
import exceptions.InvalidItemFormatException;
import exceptions.ItemNotFoundException;
import logic.InventoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    private Inventory inventory;
    private Floor floor;
    private InventoryManager manager;
    private final String databaseFilePath = "test_items.txt";

    @BeforeEach
    void setUp() {
        try {
            // Create a new test file with sample items (using semicolons to match expected format)
            FileWriter fileWriter = new FileWriter(databaseFilePath);
            fileWriter.write("101;Sword;Weapon;2;5.0;A sharp sword\n");
            fileWriter.write("102;Shield;Armor;1;7.5;A sturdy shield\n");
            fileWriter.write("103;Potion;Consumable;5;0.5;A health potion\n");
            fileWriter.close();

            inventory = new Inventory(50.0);
            floor = new Floor();
            manager = new InventoryManager(inventory, floor, databaseFilePath);

            // Load items into the floor from the test file
            floor.loadItemsFromFile(databaseFilePath);
        } catch (IOException | InvalidItemFormatException e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }

    @Test
    void pickUpItem() {
//        try {
//            // Pick up an item from the floor and add to the inventory
//            manager.pickUpItem(101);
//            assertEquals(1, inventory.getItems().size(), "Inventory should have 1 item after picking up.");
//            assertEquals(2, floor.getItems().size(), "Floor should have 2 items after picking up one.");
//            assertNotNull(inventory.getItemById(101), "Item with ID 101 should exist in inventory.");
//        } catch (Exception e) {
//            fail("Exception thrown during pickUpItem test: " + e.getMessage());
//        }

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
        assertThrows(ItemNotFoundException.class, () -> manager.updateItemInInventory(999, "Fake Sword", "Weapon", 5, 8, "Non-existent sword"), "Expected ItemNotFoundException for non-existent item ID 999.");
    }

    @Test
    void saveItemsToFile() {
        try {
            // Pick up an item, update it, and save to file
            manager.pickUpItem(101);
            manager.updateItemInInventory(101, "Great Sword", "Weapon", 3, 6.0, "An even sharper sword");
            manager.saveItemsToFile();

            // Reload items to verify changes were saved using the correct delimiter format
            floor.loadItemsFromFile(databaseFilePath);
            Item updatedItem = floor.getItemById(101);
            assertNotNull(updatedItem, "Updated item should exist in floor after reloading.");
            assertEquals("Great Sword", updatedItem.getName(), "Item name should be updated after saving to file.");
            assertEquals(3, updatedItem.getQuantity(), "Item quantity should be updated after saving to file.");
            assertEquals(6.0, updatedItem.getWeight(), "Item weight should be updated after saving to file.");
            assertEquals("An even sharper sword", updatedItem.getDescription(), "Item description should be updated after saving to file.");
        } catch (Exception e) {
            fail("Exception thrown during saveItemsToFile test: " + e.getMessage());
        }

        // Negative Case: Try saving with a faulty filename
//        assertThrows(IOException.class, () -> manager.saveItemsToFile(":/invalidpath/test_items.txt"), "Expected IOException for invalid file path.");
    }

}
