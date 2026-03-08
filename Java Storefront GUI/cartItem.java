/* Name: Jared Camilo
Course: CNT 4714 – Spring 2026
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday February 1, 2026
*/

// This file was created to separate the item objects in the cart from the inventory items
public class cartItem {
    private final Item item;
    private int cartQuantity;

    // Mark the item as in the cart and set its quantity
    public cartItem(Item item, int quantity) {
        this.item = item;
        this.cartQuantity = quantity;
    }

    // This method is used to call the item in the cart
    public Item getItem() {
        return item;
    }

    // This method is used to get the item in the carts quantity
    public int getCartQuantity() {
        return cartQuantity;
    }

    // This method is used to set the item quantity in the cart
    public void setCartQuantity(int quantity) {
        this.cartQuantity = quantity;
    }

    // This method gets the subtotal based on items in the cart
    public double getSubtotal() {
        return item.getPrice() * cartQuantity;
    }
}