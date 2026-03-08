/* Name: Jared Camilo
Course: CNT 4714 – Spring 2026
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday February 1, 2026
*/

// Import the necessary tools
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NileLogic {
    // We will use this method to find the item being searched
    // We pass in the number as a string since some IDs have a mix of letters and numbers
    public Item itemLookup (String lookupNumber) {
        // Set the file we are using to our excel file
        String file = "inventory.csv";
        
        // We create a reader to find the item in the file
        try (BufferedReader itemFinder = new BufferedReader(new FileReader(file))) {

            // Declare the item line
            String itemLine;

            // We will loop through the entire file to find the item
            while ((itemLine = itemFinder.readLine()) != null) {
                // We will need to break up each line to actually use its data
                String[] itemVars = itemLine.split(",");
                
                // Set the ID of the line we are on
                String itemNumber = itemVars[0].trim();
              
                // Use the set ID to compare it to the ID of the item we are searching for
                if (itemNumber.equals(lookupNumber)) {
                    // If we found the item we want, we will then save all the data in the line 
                    // Trim to keep processing clean
                    String name = itemVars[1].trim().replaceAll("^\"|\"$", "");
                    boolean stock = Boolean.parseBoolean(itemVars[2].trim());
                    int quantity = Integer.parseInt(itemVars[3].trim());
                    double price = Double.parseDouble(itemVars[4].trim());

                    // Now we can create a new Item object with the variables
                    return new Item(lookupNumber, name, stock, quantity, price);
                }
            }
        // Exception for safety
        } catch (IOException ex) {
            System.getLogger(NileLogic.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        // Return null if the item is never found
        return null;
    }

    // This method will take similar logic from the item lookup, but instead of creating the item, it will update the inventory
    public void updateInventory(ArrayList<cartItem> cartItems) {
        // Set the file we are using to our excel file again
        String file = "inventory.csv";

        // We create another reader to find the item to delete
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            // Declare the item line
            String line;

            // We will create a new ArrayList to store the updated lines
            ArrayList<String> updatedLines = new ArrayList<>();

            // We will loop through the entire file to find the item to delete
            while ((line = reader.readLine()) != null) {
                // We will need to break up each line againa
                String[] itemVars = line.split(",");

                String itemNumber = itemVars[0].trim();

                // We are gonna loop through our cart items to see if any match the current line item
                for (cartItem cartItem : cartItems) {
                    // Use the set ID to compare it to the ID of the item we are trying to delete
                    if (itemNumber.equals(cartItem.getItem().getItemNumber())) {
                        // If we found the item we want, we will go ahead and deduct the quantity
                        // Trim to keep processing clean
                        int currentQuantity = Integer.parseInt(itemVars[3].trim());
                        int newQuantity = currentQuantity - cartItem.getCartQuantity();

                        // Ensure quantity does not go negative and keeps the stock accurate
                        if (newQuantity < 0) {
                            newQuantity = 0;
                        }

                        // Update the quantity in the itemVars array
                        itemVars[3] = String.valueOf(newQuantity);

                        break;
                    }
                }
                // Now we will trim and rebuild the line before adding it to the updated lines
                String itemName = itemVars[1].trim();
                itemName = itemName.replaceAll("^\"|\"$", "");
                itemVars[1] = "\"" + itemName + "\"";

                // Add the updated line to the list
                updatedLines.add(String.join(",", itemVars));
            }
            // Now we will take the updated lines and put them back into the file
            try (FileWriter writer = new FileWriter(file)) {
                for (String updatedLine : updatedLines) {
                    writer.append(updatedLine + "\n");
                }
            }
        } catch (IOException ex) {
            System.getLogger(NileLogic.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    // This method will calculate our discount
    public static double calcDiscount (int itemQuantity) {
        double discount;

        // Very simple if else logic that determines the discount
        // I used double for discounts so I can multiple the total by them then add it
        if  (itemQuantity >= 15) {
            discount = 0.20;
        } else if (itemQuantity >= 10) {
            discount = 0.15;
        } else if (itemQuantity >= 5) {
            discount = 0.10;
        } else {
            discount = 0.0;
        }
        return discount;
    }
}

