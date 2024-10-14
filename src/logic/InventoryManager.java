package logic;
import data.Inventory;
import data.Floor;
import data.Item;


public class InventoryManager {
    private Inventory inventory;
    private Floor floor;

    public InventoryManager(Inventory inventory, Floor floor) {
        this.inventory = inventory;
        this.floor = floor;
    }

    public void pickUpItem(int id) {
        Item item = floor.getItemById(id);
        if (item != null) {
            if (inventory.addItem(item)) {
                floor.removeItem(id);
                System.out.println("Item picked up and added to inventory.");
            }
        } else {
            System.out.println("Item not found on the floor.");
        }
    }

    public void dropItem(int id) {
        Item item = inventory.getItemById(id);
        if (item != null) {
            inventory.removeItem(id);
            floor.addItem(item);
            System.out.println("Item dropped from inventory to floor.");
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    public void displayInventory() {
        inventory.displayItems();
    }

    public void displayFloor() {
        floor.displayItems();
    }
}
