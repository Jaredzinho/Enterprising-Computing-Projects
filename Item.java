/* Name: Jared Camilo
Course: CNT 4714 – Spring 2026
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday February 1, 2026
*/

// We will declare the variables to set them 
public class Item  {
    private final String itemNumber;
    private final String itemName;
    private final boolean inStock;
    private final int inventoryQuantity;
    private final double itemPrice;

    // Create our item object with the values and assign them
    public Item (String itemNum, String name, boolean stock, int quantity, double price) {
        this.itemNumber = itemNum;
        this.itemName = name;
        this.inStock = stock;
        this.inventoryQuantity = quantity;
        this.itemPrice = price;
    }
    
    // This method is used to get the item ID
    public String getItemNumber() {
        return itemNumber;
    }

    // This method is used to get the item name
    public String getItemName() {
        return itemName;
    }

    // This method is used to check if the item is in stock
    public boolean isInStock() {
        return inStock;
    }

    // This method is used to get the item quantity
    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    // This method is used as a flag to see if an item is in stock or not
    public boolean isInStock(int requestedQuantity) {
        return inventoryQuantity >= requestedQuantity;
    }

    // This method is used to get the item price
    public double getPrice() {
        return itemPrice;
    }
}
