/* Name: Jared Camilo
Course: CNT 4714 – Spring 2026
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday February 1, 2026
*/

// Import the necessary tools
import java.util.ArrayList;

public class shoppingCart {

    // We will create an ArrayList to store the cart of Item objects
    private final ArrayList<cartItem> cartItems = new ArrayList<>();

    // This method is simple, it follows the rules of the cart only holding 5 items
    // It also ensures item is in stock before adding, if both apply, it creates the new cart item
    public boolean add(Item item, int quantity) {
        if (cartItems.size() < 5 && item.isInStock(quantity)) {
            cartItems.add(new cartItem(item, quantity));
            return true;
        } else {
            return false;
        }
    }

    // This method will loop through the item in the cart and return the quantity of it
    public int getCartQuantity(String itemNumber) {
    	int quantity = 0;
    	for (cartItem cartItem : cartItems) {
    		if (cartItem.getItem().getItemNumber().equals(itemNumber)) {
    			quantity += cartItem.getCartQuantity();
    		}
    	}
    	return quantity;
    }
    
    // This method will remove an item from the cart, it loops through the cart to ensure it finds the correct one
    public boolean remove(Item item) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getItem().equals(item)) {
                cartItems.remove(i);
                return true;
            }
        }
        return false;
    }

    // This method will loop through all the items in the cart and add their prices
    public double cartSubtotal() {
        double subTotal = 0;
        for (cartItem cartItem : cartItems) {
            subTotal += cartItem.getSubtotal();
        }
        return subTotal;
    }

    // This method will calculate the total including the 6% tax
    public double calculateTotal() {
        return cartSubtotal() * 1.06;
    }

    // This method clears the list
    public void emptyCart() {
        cartItems.clear();
    }
    
    // This method will return current cart items
    public ArrayList<cartItem> getCartItems() {
        return cartItems; 
    }
    
    // This method is essentially the logic for the delete last item from cart button
    public boolean removeLast() {
        if (cartItems.isEmpty()) {
            return false;
        }

        cartItems.remove(cartItems.size() - 1);
        return true;
    }
    
    // This method applies the discount by getting the item quantity then passing it to the discount function
    // Then it will calculate the discount total
    public double applyDiscount() {
        double discountTotal = 0.0;

        for (cartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getCartQuantity();
            double rate = NileLogic.calcDiscount(quantity);

            double itemTotal = item.getPrice() * quantity;
            discountTotal += itemTotal * rate;
        }

        return discountTotal;
    }

    // This method will handle the checkout process by taking the total, checking for discounts, and clearing the cart
    // It will also update item quantities
    public Transaction checkOut() {
        double subTotal = cartSubtotal();
        double discount = applyDiscount();
        Transaction transaction = new Transaction(new ArrayList<>(cartItems), subTotal, discount);
        transaction.writeTransaction();

        // Now we ensure to update the inventory quantities
        new NileLogic().updateInventory(cartItems);
        
        // Finally, we empty the cart after checkout
        emptyCart();
        return transaction;
    }
}
