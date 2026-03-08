/* Name: Jared Camilo
Course: CNT 4714 – Spring 2026
Assignment title: Project 1 – An Event-driven Enterprise Simulation
Date: Sunday February 1, 2026
*/

// Import the necessary tools
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

// Since our class has multiple buttons, flags, and interactions a lot will be declared
public class NileStorefront extends JFrame {
	private shoppingCart cart;
	private NileLogic logic;
	
	private static final long serialVersionUID = 1L;
	
	private final JPanel panel = new JPanel();
	
	private JLabel txtItemID;
	private JLabel txtQuantity;
	private JLabel txtItemDetails;
	private JLabel txtSubtotal;
	
	private JTextField txtUserControls;
	private JTextField SubtotalOutput;
	private JTextField ItemDetailsOutput;
	private JTextField QuantityOutput;
	private JTextField ItemIDOutput;
	
	private JButton btnSearch;
	private JButton btnAddItem;
	private JButton btnDeleteLast;
	private JButton btnEmptyCart;
	private JButton btnCheckOut;
	private JButton btnExit;
	
	private JTextArea cartItem1, cartItem2, cartItem3, cartItem4, cartItem5;
	private JTextArea cartHeader;

	private Item lastSearchedItem = null;
	private int currentItemNum = 1;
	private boolean searchLocked = false;
	private int lastRequestedQuantity = 0;
	
	private void showError(String message) {
	    JOptionPane.showMessageDialog(
	        this,
	        message,
	        "Nile Dot Com - ERROR",
	        JOptionPane.ERROR_MESSAGE
	    );
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NileStorefront frame = new NileStorefront();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NileStorefront() {
		cart = new shoppingCart();
		logic = new NileLogic();
		
		// Create the window for the store
		setTitle("Nile.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 684);
		getContentPane().setLayout(null);
		panel.setBackground(new Color(176, 224, 230));
		panel.setBounds(0, 0, 784, 165);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		// We will create multiple labels for the user to view info about the transaction
		txtItemID = new JLabel(" Enter item ID for Item#:");
		txtItemID.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtItemID.setBounds(213, 34, 153, 21);
		panel.add(txtItemID);
		
		txtQuantity = new JLabel("Enter quantity for Item#:");
		txtQuantity.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtQuantity.setBounds(209, 66, 157, 17);
		panel.add(txtQuantity);
		
		txtItemDetails = new JLabel("Details for Item#:");
		txtItemDetails.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtItemDetails.setBounds(249, 94, 117, 20);
		panel.add(txtItemDetails);
		
		txtSubtotal = new JLabel("Current Subtotal for 0 item(s):");
		txtSubtotal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtSubtotal.setBounds(188, 122, 175, 20);
		panel.add(txtSubtotal);
		
		// We will create multiple text fields for the user to enter info about the items
		ItemIDOutput = new JTextField();
		ItemIDOutput.setBounds(376, 35, 398, 20);
		panel.add(ItemIDOutput);
		ItemIDOutput.setColumns(10);
		
		QuantityOutput = new JTextField();
		QuantityOutput.setColumns(10);
		QuantityOutput.setBounds(376, 66, 398, 20);
		panel.add(QuantityOutput);
		
		ItemDetailsOutput = new JTextField();
		ItemDetailsOutput.setFont(new Font("Tahoma", Font.PLAIN, 11));
		ItemDetailsOutput.setBackground(new Color(220, 220, 220));
        ItemDetailsOutput.setEditable(false);
		ItemDetailsOutput.setColumns(10);
		ItemDetailsOutput.setBounds(376, 94, 398, 20);
		panel.add(ItemDetailsOutput);
		
		SubtotalOutput = new JTextField();
		SubtotalOutput.setFont(new Font("Tahoma", Font.PLAIN, 11));
		SubtotalOutput.setBackground(new Color(220, 220, 220));
        SubtotalOutput.setEditable(false);
		SubtotalOutput.setColumns(10);
		SubtotalOutput.setBounds(376, 122, 398, 20);
		panel.add(SubtotalOutput);
		
		// We create the middle panel for the cart
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(0, 164, 784, 253);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		cartHeader  = new JTextArea();
		cartHeader.setFont(new Font("Bodoni MT", Font.BOLD, 25));
		cartHeader.setText("Your Shopping Cart Is Currently Empty");
		cartHeader.setEditable(false);
		cartHeader.setBounds(144, 11, 640, 34);
		panel_1.add(cartHeader);
		cartHeader.setOpaque(false);
		cartHeader.setBackground(panel_1.getBackground());
		
		// Create the 5 spaces for our cart items
		cartItem1 = new JTextArea();
		cartItem1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		cartItem1.setBackground(new Color(220, 220, 220));
		cartItem1.setBounds(10, 45, 764, 27);
        cartItem1.setEditable(false);
		panel_1.add(cartItem1);
		
		cartItem2 = new JTextArea();
		cartItem2.setFont(new Font("Monospaced", Font.PLAIN, 11));
		cartItem2.setBackground(new Color(220, 220, 220));
		cartItem2.setBounds(10, 83, 764, 27);
        cartItem2.setEditable(false);
		panel_1.add(cartItem2);
		
		cartItem3 = new JTextArea();
		cartItem3.setFont(new Font("Monospaced", Font.PLAIN, 11));
		cartItem3.setBackground(new Color(220, 220, 220));
		cartItem3.setBounds(10, 121, 764, 27);
        cartItem3.setEditable(false);
		panel_1.add(cartItem3);
		
		cartItem4 = new JTextArea();
		cartItem4.setFont(new Font("Monospaced", Font.PLAIN, 11));
		cartItem4.setBackground(new Color(220, 220, 220));
		cartItem4.setBounds(10, 159, 764, 27);
        cartItem4.setEditable(false);
		panel_1.add(cartItem4);
		
		cartItem5 = new JTextArea();
		cartItem5.setFont(new Font("Monospaced", Font.PLAIN, 11));
		cartItem5.setBackground(new Color(220, 220, 220));
		cartItem5.setBounds(10, 197, 764, 27);
        cartItem5.setEditable(false);
		panel_1.add(cartItem5);
		
		// We create the bottom panel for the buttons
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(176, 224, 230));
		panel_2.setBounds(0, 416, 784, 229);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		txtUserControls = new JTextField();
		txtUserControls.setForeground(new Color(0, 0, 0));
		txtUserControls.setEditable(false);
		txtUserControls.setBackground(new Color(176, 224, 230));
		txtUserControls.setFont(new Font("Bodoni MT", Font.BOLD, 30));
		txtUserControls.setText("USER CONTROLS");
		txtUserControls.setBounds(73, 9, 246, 41);
		panel_2.add(txtUserControls);
		
		// We create the add button
		btnAddItem = new JButton("Add Item # To Cart");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			// Add error handling to ensure proper input
			if (lastSearchedItem == null || lastRequestedQuantity <= 0) {
				showError("Please search for an item before adding to the cart.");
	            return;
		        }
			// Add the item to the cart
			boolean added = cart.add(lastSearchedItem, lastRequestedQuantity);
			
			if (!added) {
				if (cart.getCartItems().size() >= 5) {
	                showError("Shopping cart is full. Cannot add more than 5 items.");
				} else {
	                showError("Unable to add item to cart. Please check stock and quantity.");
				}
				return;
			}
			 
			// Keep all displays and buttons updated to ensure continuous use
			updateCartDisplay();
	        updateDisplay();
	        
	        lastSearchedItem = null;
	        lastRequestedQuantity = 0;
	        searchLocked = false;
	        
	        ItemIDOutput.setText("");
	        QuantityOutput.setText("");
	        
	        updateButtons();
			}
		});
		btnAddItem.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnAddItem.setBounds(402, 59, 328, 43);
		panel_2.add(btnAddItem);
		btnAddItem.setEnabled(false);
		
		// We create our checkout button
		btnCheckOut = new JButton("Check Out");
		btnCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (cart.getCartItems().isEmpty()) {
		            showError("Your cart is empty.");
		            return;
		        }
				// Declare the transaction and pass it the function to be printd
				Transaction trans = cart.checkOut();
				String invoice = printReceipt(trans);
				
				// Set the text for the top of the window
				JOptionPane.showMessageDialog(
		            NileStorefront.this,
		            invoice,
		            "Nile Dot Com - FINAL INVOICE",
		            JOptionPane.INFORMATION_MESSAGE
		        );
			 } catch (Exception ex) {
		            ex.printStackTrace(); 
		            showError("Checkout failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
			 }
				
				// Disable necessary buttons after checkout
				btnSearch.setEnabled(false);
		        btnAddItem.setEnabled(false);
		        btnDeleteLast.setEnabled(false);
		        btnCheckOut.setEnabled(false);
		        
		        // Set necessary text to blank
		        ItemIDOutput.setText("");
		        QuantityOutput.setText("");
		        ItemDetailsOutput.setText("");
		        SubtotalOutput.setText("");

		        // Set our flags and update all displays
		        lastSearchedItem = null;
		        lastRequestedQuantity = 0;
		        updateDisplay();
		        updateCartDisplay();
				 
			}
		});
		btnCheckOut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnCheckOut.setBounds(402, 113, 328, 43);
		panel_2.add(btnCheckOut);
		
		// We create the exit button
		btnExit = new JButton("Exit (Close App)");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Super simple, just exits the program
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnExit.setBounds(402, 167, 328, 43);
		panel_2.add(btnExit);
		
		// We create the search button
		btnSearch = new JButton("Search for Item #");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Take in the values entered by the user and trim for processing
				String itemID = ItemIDOutput.getText().trim();
				String reqItemQuantity = QuantityOutput.getText().trim();

				// Add error handling to ensure proper inputs
				if (itemID.isEmpty()) {
				    showError("Please enter an item ID");
				    return;
				}
			
		        if (reqItemQuantity.isEmpty()) {
		            showError("Please enter a quantity");
		            return;
		        }
		        
		        // Save the quantity
		        int quantity = Integer.parseInt(reqItemQuantity);
		        
		        if (quantity <= 0) {
		            showError("Quantity must be at least 1");
		            return;
		        }
		        
		        // Save the item
				Item item = logic.itemLookup(itemID);

				if (item == null) {
				    showError("item ID " + itemID + " not in file");
				    return;
				}

				// Check the availability of the item
				int onHandQuantity = item.getInventoryQuantity();
				int cartQuantity = cart.getCartQuantity(item.getItemNumber());
				int available  = onHandQuantity - cartQuantity;
				
				// Errors for item stock
				if (available == 0) {
				    showError("Sorry.... that item is out of stock, please try another item");
				    return;
				}
				
				if (quantity > available) {
		            showError("Insufficient stock. Only " + available + " on hand. Please reduce the quantity.");
		            return;
		        }

				// Set the prices and subtotals
				double unitPrice = item.getPrice();
		        double itemSubtotal = unitPrice * quantity;
		        double disc = NileLogic.calcDiscount(quantity);
		        
		        // Had to cast the discount to int for proper printing
		        int integerDisc = (int)(disc * 100);
		        
		        // Print our item in the subtotal tab with all the required values
				String details = String.format(
				    "%s \"%s\" $%.2f %d %d%% $%.2f ",
				    item.getItemNumber(), item.getItemName(), unitPrice, quantity, integerDisc, itemSubtotal);

				// Update everything for continuous use
				ItemDetailsOutput.setText(details);
				
				lastSearchedItem = item;
				lastRequestedQuantity = quantity;
				
				searchLocked = true;
				
				updateDisplay();
				updateButtons();
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSearch.setBounds(42, 59, 328, 43);
		panel_2.add(btnSearch);
		
		// We create our delete button
		btnDeleteLast = new JButton("Delete Last Item From Cart");
		btnDeleteLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Can't delete if cart is empty right?
				if (cart.getCartItems().isEmpty()) {
		            showError("Your cart is already empty.");
		            return;
		        }
				
				// Call remove last function
		        cart.removeLast();
		        
		        // Update everything before continuing
		        searchLocked = false;
		        lastSearchedItem = null;
		        lastRequestedQuantity = 0;
		        ItemDetailsOutput.setText("");
		        
		        updateDisplay();
		        updateCartDisplay();
		        updateButtons();
			}
		});
		
		btnDeleteLast.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDeleteLast.setBounds(42, 112, 328, 43);
		panel_2.add(btnDeleteLast);
		
		// We create our empty cart button
		btnEmptyCart = new JButton("Empty Cart - Start A New Order");
		btnEmptyCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Call the empty cart method
				cart.emptyCart();
				
				// Update everything for continuous use and a clean slate
				lastSearchedItem = null;
		        lastRequestedQuantity = 0;
		        searchLocked = false;
		        
		        ItemIDOutput.setText("");
		        QuantityOutput.setText("");
		        ItemDetailsOutput.setText("");
		        SubtotalOutput.setText("");

		        updateButtons();
		        updateDisplay();
		        updateCartDisplay();
			}
		});
		btnEmptyCart.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnEmptyCart.setBounds(42, 167, 328, 43);
		panel_2.add(btnEmptyCart);
		updateDisplay();
		updateCartDisplay();
		updateButtons();
	}
	
	// I created this helper method to keep the gui on the right screen when necessary
	private void updateDisplay() {		
		// Call and check our cart size
		int cartSize = cart.getCartItems().size();
		
		if (cartSize == 0) {
	        cartHeader.setText("Your Shopping Cart Is Currently Empty");
	    } else if (cartSize == 1) {
	        cartHeader.setText("Your Shopping Cart Currently Contains 1 Item");
	    } else {
	        cartHeader.setText("Your Shopping Cart Currently Contains " + cartSize + " Item(s)");
	    }
		cartHeader.revalidate();
		cartHeader.repaint();
		
		// Get current item
		int currentItemNum = cartSize + 1;
		
        if (currentItemNum > 5) currentItemNum = 5;

        // Ensure the menu at the top is giving the correct values
        txtItemID.setText("Enter item ID for item #" + currentItemNum + ":");
        txtQuantity.setText("Enter quantity for item #" + currentItemNum + ":");
        txtItemDetails.setText("Details for Item #" + (cartSize == 0 ? 1 : cartSize) + ":");

        // This ensures our subtotal is being handled properly
        if (cartSize == 0) {
            txtSubtotal.setText("Current Subtotal for 0 item(s):");
            SubtotalOutput.setText("");
        } else {
        	txtSubtotal.setText("Current Subtotal for " + cartSize + " item(s):");
            SubtotalOutput.setText(String.format("$%.2f", cart.cartSubtotal()));
        }
        
        // Keep the add and search buttons updated
        btnSearch.setText("Search for Item #" + currentItemNum);
        btnAddItem.setText("Add Item #" + currentItemNum + " To Cart");
	}
	
	// Created this helper method to make sure the is consistent and formatted
	private void updateCartDisplay() {
		// Set the cart
		 cartItem1.setText("");
	     cartItem2.setText("");
	     cartItem3.setText("");
	     cartItem4.setText("");
	     cartItem5.setText("");
	     
        ArrayList<cartItem> items = cart.getCartItems();
        
        // Loop through our items until 5 (cart max) and print items in cart properly
        for (int i = 0; i < items.size() && i < 5; i++) {
        	cartItem cartItem = items.get(i);
        	
        	String line = String.format("Item %d - SKU: %s. Desc: \"%s\", Price ea. $%.2f, Qty: %d, Total: $%.2f",
        			(i+1), cartItem.getItem().getItemNumber(), cartItem.getItem().getItemName(), cartItem.getItem().getPrice(), cartItem.getCartQuantity(), cartItem.getSubtotal());
        	
        	// This keeps the lines properly set based on the cart size
        	if (i == 0) {
        		cartItem1.setText(line);
        	} else if (i == 1) {
        		cartItem2.setText(line);
        	} else if (i == 2) {
        		cartItem3.setText(line);
        	} else if (i == 3) {
        		cartItem4.setText(line);
        	} else if (i == 4) {
        		cartItem5.setText(line);
        	}
        }
	}
	
	// Created this helper method to make sure our buttons become available and unavailable at the correct times
	private void updateButtons() {
		// We check the cart size to make sure if it's empty, you can't use delete last, checkout, or empty
	    int cartSize = cart.getCartItems().size();
	    
	    if (cartSize > 0) {
	    	btnDeleteLast.setEnabled(true);
	    	btnCheckOut.setEnabled(true);
	    	btnEmptyCart.setEnabled(true);
	    } else {
	    	btnDeleteLast.setEnabled(false);
	    	btnCheckOut.setEnabled(false);
	    	btnEmptyCart.setEnabled(false);
	    }
	    
	    // Ensure add is only available at the right times
	    if (lastSearchedItem != null) {
	    	btnAddItem.setEnabled(true);
	    } else {
	    	btnAddItem.setEnabled(false);
	    }
	    
	    // Ensure search is only available at the right times
	    if (lastSearchedItem == null) {
	    	btnSearch.setEnabled(true);
	    } else {
	    	btnSearch.setEnabled(false);
	    }
	}
	
	// This method will display the GUi window with the invoice
	private String printReceipt(Transaction trans) {
    	// Set the timestamp and date and format it
		java.time.ZonedDateTime time = java.time.ZonedDateTime.of(trans.getTransTime(), java.time.ZoneId.systemDefault());
    	java.time.format.DateTimeFormatter timeFormat = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm:ss a z");
    	String date = time.format(timeFormat);
    	
    	// Declare the array of purchased items
    	ArrayList<cartItem> items = trans.getPurchasedItems();
    	
    	// Get our totals and amounts
    	double orderSubTotal = trans.getSubtotal();
    	double discountAmt = trans.getDiscount();
    	double afterDiscount = orderSubTotal - discountAmt;
    	double taxedAmount = afterDiscount * 0.06;
    	double orderTotal = afterDiscount + taxedAmount;
        
        // Now we will use StrinBuilder to display the contents
        StringBuilder printer = new StringBuilder();
        
        // We will add the date and other info before the item list
        printer.append("Date: ").append(date).append("\n");
        printer.append("\n");
        printer.append("Number of line items: ").append(items.size()).append("\n");
        printer.append("\n");
        printer.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:\n");
        printer.append("\n");

        // Loop through our items and print all of the purchased ones to the receipt
        for (int i = 0; i < items.size(); i++) {
        	cartItem cartItem = items.get(i);
        	Item item = cartItem.getItem();
        	int quantity = cartItem.getCartQuantity();
        	
        	double price = item.getPrice();
        	double subTotal = price * quantity;
        	
        	double discount = NileLogic.calcDiscount(quantity);
        	int integerDisc = (int)(discount * 100);
        	
        	printer.append(String.format("%d. %s %s $%.2f %d %d%% $%.2f\n",
        			(i+1), item.getItemNumber(), item.getItemName(), price, quantity, integerDisc, subTotal));
        }
        
        // Print the rest of the money stuff after
        printer.append("\n");
        printer.append(String.format("Order subtotal:  $%.2f\n", orderSubTotal));
        printer.append("\n");
        printer.append("Tax rate:        6%\n");
        printer.append("\n");
        printer.append(String.format("Tax amount:        $%.2f\n", taxedAmount));
        printer.append("\n");
        printer.append(String.format("ORDER TOTAL:        $%.2f\n", orderTotal));
        printer.append("\n");
        printer.append("Thanks for shopping at Nile Dot Com!");
        printer.append("\n");
        
        // Return the receipt
        return printer.toString();
	}
}	
