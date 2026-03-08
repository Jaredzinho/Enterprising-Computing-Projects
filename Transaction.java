/* Name: Jared Camilo
Course: CNT 4714 – Spring 2026
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday February 1, 2026
*/

// Import the necessary tools
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalTime;


// This file was created to turn each completed transaction into an object for record keeping
public class Transaction {
    private final ArrayList<cartItem> purchasedItems;
    private final double subtotal;   
    private final double discount;
    private final double total;
    private final double tax = 0.06;
    private final String transactionID;
    private final LocalDateTime transactionDateTime;
    

    // Create the transaction object with the values from our cart
    public Transaction(ArrayList<cartItem> purchasedItems, double subtotal, double discount) {
        this.purchasedItems = purchasedItems;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = (subtotal - discount) * (1 + tax);
        this.transactionDateTime = LocalDateTime.now();
        this.transactionID = createTransactionID(transactionDateTime);


    }

    // This method will create the transaction ID based on the actual time of purchase
    private String createTransactionID(LocalDateTime dateTime) {
        DateTimeFormatter transactionFormatting = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
        return transactionFormatting.format(dateTime);
    }   

    // This method is used to get the local transaction time 
    public LocalDateTime getTransTime() {
    	return transactionDateTime;
    }
    
    
    // This method is used to get the local transaction time 
    public String getTransID() {
    	return transactionID;
    }
    
    // This method is used to call the items purchased from the cart
    public ArrayList<cartItem> getPurchasedItems() {
        return purchasedItems;
    }

    // This method is used to get the subtotal from the cart
    public double getSubtotal() {
        return subtotal;
    }

    // This method is used to get the discount from the cart
    public double getDiscount() {
        return discount;
    }

    // This method is used to get the total from the cart
    public double getTotal() {
        return total;
    }

    // This method will write and store the transaction data into the transactions.csv file
    // Commas and characters in names caused inconsistency, so I added replace to fix that
    public void writeTransaction() {
    	// Set the timestamp and date and format it
    	java.time.ZonedDateTime time = java.time.ZonedDateTime.of(transactionDateTime, java.time.ZoneId.systemDefault());
    	java.time.format.DateTimeFormatter timeFormat = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm:ss a z");
    	String date = time.format(timeFormat);

    	// Now we set our writer to write on transaction
        try (FileWriter writer = new FileWriter("transactions.csv", true)) {
        	// Loop through each purchased item and write its data on the file in proper format
            writer.append("\n");
        	for (cartItem cartItem : purchasedItems) {
                Item item = cartItem.getItem();
                int quantity = cartItem.getCartQuantity();

                double discRate = NileLogic.calcDiscount(quantity);  
                double lineSubtotal = item.getPrice() * quantity;
                
                writer.append(transactionID).append(",");
                writer.append(item.getItemNumber()).append(",");
                writer.append("\"").append(item.getItemName().replace("\"", "\"\"")).append("\",");
                writer.append(String.format(java.util.Locale.US, "%.2f", item.getPrice())).append(",");
                writer.append(String.valueOf(quantity)).append(",");
                writer.append(String.format(java.util.Locale.US, "%.1f", discRate * 100)).append(",");
                writer.append(String.format(java.util.Locale.US, "%.2f", lineSubtotal)).append(",");
                writer.append(date);
                writer.append("\n");
        	}
            writer.append("\n");
        } catch (IOException e) {
            System.out.println("Error writing transaction data to file: " + e.getMessage());
        }
    }
}
