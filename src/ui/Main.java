package ui;

import data.Inventory;
import data.Floor;
import exceptions.InvalidItemFormatException;
import exceptions.ItemNotFoundException;
import logic.InventoryManager;
import utils.InputValidator;

public class Main {
    public static void main(String[] args) throws ItemNotFoundException {
        // Get the filename to load items from
        String filename = InputValidator.getValidString("Enter the path to the items.txt file: ");

        // Initialize inventory and floor objects
        Inventory inventory = new Inventory(50.0);  // Max capacity of 50.0 units
        Floor floor = new Floor();
        InventoryManager manager = new InventoryManager(inventory, floor, filename);

        // Load items from file
        try {
            boolean loadedSuccessfully = floor.loadItemsFromFile(filename);
            if (!loadedSuccessfully) {
                System.out.println("Failed to load items from file.");
                return;
            }
        } catch (InvalidItemFormatException e) {
            System.out.println(e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            // Display menu options
            System.out.println("\nVideogame Inventory Management System:");
            System.out.println("1. Pick Up Item from Floor");
            System.out.println("2. Drop Item to Floor");
            System.out.println("3. Display Inventory");
            System.out.println("4. Display Floor Items");
            System.out.println("5. Update Item in Inventory");
            System.out.println("6. Save All Items to File");
            System.out.println("7. Filter Inventory by Type");
            System.out.println("8. Manually Add Item to Floor");
            System.out.println("9. Manually Remove Item from Floor");
            System.out.println("10. Exit");

            int choice = InputValidator.getValidInt("Enter your choice: ");

            switch (choice) {
                case 1: // Pick up an item from the floor
                    int pickUpId = InputValidator.getValidInt("Enter Item ID to pick up: ");
                    if (pickUpId > 0) {
                        manager.pickUpItem(pickUpId);
                    } else {
                        System.out.println("Invalid Item ID.");
                    }
                    break;

                case 2: // Drop an item to the floor
                    int dropId = InputValidator.getValidInt("Enter Item ID to drop: ");
                    if (dropId > 0) {
                        manager.dropItem(dropId);
                    } else {
                        System.out.println("Invalid Item ID.");
                    }
                    break;

                case 3: // Display inventory
                    manager.displayInventory();
                    break;

                case 4: // Display items on the floor
                    manager.displayFloor();
                    break;

                case 5: // Update an item in inventory
                    int updateId = InputValidator.getValidInt("Enter Item ID to update: ");
                    if (updateId > 0) {
                        String name = InputValidator.getValidString("Enter new name (or press Enter to keep unchanged): ");
                        String type = InputValidator.getValidString("Enter new type (or press Enter to keep unchanged): ");
                        int quantity = InputValidator.getValidInt("Enter new quantity (or -1 to keep unchanged): ");
                        double weight = InputValidator.getValidDouble("Enter new weight (or -1 to keep unchanged): ");
                        String description = InputValidator.getValidString("Enter new description (or press Enter to keep unchanged): ");
                        manager.updateItemInInventory(updateId, name, type, quantity, weight, description);
                        floor.saveItemsToFile(filename); // Save updates to file
                    } else {
                        System.out.println("Invalid Item ID.");
                    }
                    break;

                case 6: // Save all items to file
                    floor.saveItemsToFile(filename);
                    break;

                case 7: // Filter inventory by type
                    String filterType = InputValidator.getValidString("Enter the type to filter by: ");
                    manager.filterAndDisplayItems(filterType);
                    break;

                case 8: // Manually add item to floor
                    int newId = InputValidator.getValidInt("Enter new item ID: ");
                    String newName = InputValidator.getValidString("Enter new item name: ");
                    String newType = InputValidator.getValidString("Enter new item type: ");
                    int newQuantity = InputValidator.getValidInt("Enter quantity: ");
                    double newWeight = InputValidator.getValidDouble("Enter item weight: ");
                    String newDescription = InputValidator.getValidString("Enter item description: ");
                    if (newId > 0 && newQuantity > 0 && newWeight > 0) {
                        floor.manuallyAddItemToFloor(filename, newId, newName, newType, newQuantity, newWeight, newDescription);
                    } else {
                        System.out.println("Invalid item parameters. Make sure all values are positive.");
                    }
                    break;

                case 9: // Manually remove item from floor
                    int removeId = InputValidator.getValidInt("Enter Item ID to remove from floor: ");
                    if (removeId > 0) {
                        floor.manuallyRemoveItemFromFloor(filename, removeId);
                    } else {
                        System.out.println("Invalid Item ID.");
                    }
                    break;

                case 10: // Exit application
                    running = false;
                    break;

                default: // Invalid choice
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
