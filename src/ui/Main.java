package ui;

import data.Inventory;
import data.Floor;
import logic.InventoryManager;
import utils.InputValidator;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory(50.0);  // Max capacity is 50.0 units of weight
        Floor floor = new Floor();
        InventoryManager manager = new InventoryManager(inventory, floor);

        boolean loadedSuccessfully = false;
        while (true) {
            System.out.println("\nSelect an option to initialize items:");
            System.out.println("1. Load items from a file");
            System.out.println("2. Manually add items to the floor");
            int initChoice = InputValidator.getValidInt("Enter your choice: ");

            if (initChoice == 1) {
                String filename = InputValidator.getValidString("Enter the path to the items.txt file: ");
                loadedSuccessfully = floor.loadItemsFromFile(filename);
                if (loadedSuccessfully) {
                    break;
                } else {
                    System.out.println("Failed to load items from the specified file. Please try again.");
                }
            } else if (initChoice == 2) {
                manager.manuallyAddItemToFloor();
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        boolean running = true;
        while (running) {
            System.out.println("\nVideogame Inventory Management System:");
            System.out.println("1. Pick Up Item from Floor");
            System.out.println("2. Drop Item to Floor");
            System.out.println("3. Display Inventory");
            System.out.println("4. Display Floor Items");
            System.out.println("5. Update Item in Inventory");
            System.out.println("6. Save Floor Items to File");
            System.out.println("7. Filter Inventory by Type");
            System.out.println("8. Manually Add Item to Floor");
            System.out.println("9. Exit");
            int choice = InputValidator.getValidInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    int pickUpId = InputValidator.getValidInt("Enter Item ID to pick up: ");
                    manager.pickUpItem(pickUpId);
                    break;
                case 2:
                    int dropId = InputValidator.getValidInt("Enter Item ID to drop: ");
                    manager.dropItem(dropId);
                    break;
                case 3:
                    manager.displayInventory();
                    break;
                case 4:
                    manager.displayFloor();
                    break;
                case 5:
                    int updateId = InputValidator.getValidInt("Enter Item ID to update: ");
                    String name = InputValidator.getValidString("Enter new name (or press Enter to keep unchanged): ");
                    String type = InputValidator.getValidString("Enter new type (or press Enter to keep unchanged): ");
                    int quantity = InputValidator.getValidInt("Enter new quantity (or -1 to keep unchanged): ");
                    double weight = InputValidator.getValidDouble("Enter new weight (or -1 to keep unchanged): ");
                    String description = InputValidator.getValidString("Enter new description (or press Enter to keep unchanged): ");
                    manager.updateItemInInventory(updateId, name, type, quantity, weight, description);
                    break;
                case 6:
                    String saveFilename = InputValidator.getValidString("Enter the filename to save floor items: ");
                    manager.saveFloorItemsToFile(saveFilename);
                    break;
                case 7:
                    String filterType = InputValidator.getValidString("Enter the type to filter by: ");
                    manager.filterAndDisplayItems(filterType);
                    break;
                case 8:
                    manager.manuallyAddItemToFloor();
                    break;
                case 9:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}