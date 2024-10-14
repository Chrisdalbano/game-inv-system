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

        // Load items from the text file into the floor on initiating the app
        String filename = "src/data/items.txt";
        floor.loadItemsFromFile(filename);

        boolean running = true;
        while (running) {
            System.out.println("\nGame Inventory Management System:");
            System.out.println("1. Pick Up Item from Floor");
            System.out.println("2. Drop Item to Floor");
            System.out.println("3. Display Inventory");
            System.out.println("4. Display Floor Items");
            System.out.println("5. Exit");
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
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

