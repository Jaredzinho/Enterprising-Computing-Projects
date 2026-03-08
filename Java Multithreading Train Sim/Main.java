/*
Name: Jared Camilo
Course: CNT 4714 Spring 2026
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 15, 2026
Class: main.java
Description: a description of what the class provides would normally be expected.
*/

// Import necessary tools
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors; 

// We will create the main class which handles the logic and creation of objects
public class Main {
    
    // Plug in the excel files we are given
    static final String trainFile = "theFleetFile.csv";
    static final String routeFile = "theYardFile.csv";

    // Declare a max # for the threads
    static final int MAX = 30;

    // Our main method
    public static void main(String[] args) {

    // Create arrays to hold the routes and trains objects
    ArrayList<Route> routes = new ArrayList<>();
    ArrayList<Train> trains = new ArrayList<>();

    // Create the switch objects and add them to an array
    Switch[] switches = new Switch[11];
    for (int i = 1; i < switches.length; i++) {
        switches[i] = new Switch(i);
    }
    
    // Read the yard file for the routes
    try (BufferedReader routeReader = new BufferedReader(new FileReader(routeFile))) {

        String line;

        // Loop through each line creating route objects and parsing the numbers properly
        while ((line = routeReader.readLine()) != null) {
            String[] data = line.split(",");
            int inboundTrack = Integer.parseInt(data[0]);
            int outboundTrack = Integer.parseInt(data[4]);
            int[] switchNumbers = new int[3];
            for (int i = 1; i < 4; i++) {
                switchNumbers[i - 1] = Integer.parseInt(data[i]);
            }

            Route route = new Route(inboundTrack, outboundTrack, switchNumbers);
            routes.add(route);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Create the yard object with the routes and switches 
    Yard yard = new Yard(switches, routes);

    // Now we will read the fleet file
    try (BufferedReader trainReader = new BufferedReader(new FileReader(trainFile))) {

        String line;

        // Loop through each line creating train objects and parsing the numbers properly
        while ((line = trainReader.readLine()) != null) {
            String[] data = line.split(",");
            int trainNumber = Integer.parseInt(data[0]);
            int inboundTrack = Integer.parseInt(data[1]);
            int outboundTrack = Integer.parseInt(data[2]);

            // I create the route object for the train using the passed in tracks to make calls easier
            Route route = yard.getRoute(inboundTrack, outboundTrack);
            Train train = new Train(trainNumber, inboundTrack, outboundTrack, route, yard);
            trains.add(train);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Start our simulation
    System.out.println("$ $ $ TRAIN MOVEMENT SIMULATION BEGINS..... $ $ $ \n");

    // Establish an executor
    ExecutorService executor = Executors.newFixedThreadPool(MAX);
    
    // Execute each train
    for (Train train : trains) {
        executor.execute(train);
    }

    // Shutdown executor and wait for threads to finish
    executor.shutdown();
    
    // Quick check with while loop to prevent unexpected behavior
    while (!executor.isTerminated()) {
        Thread.yield();
    }

    // End simulation
    System.out.println("\n");
    System.out.println("$ $ $ SIMULATION ENDS $ $ $\n");

    // Now we print the final table with all the trains and their switches, routes, holds, etc. 
    for (Train train : trains) {

        // I create a route object to make calls easier and prevent null pointer exceptions
        // I set the switches to 0 by default incase some of them were on permanent hold
        Route printRoute = train.getRoute();
        String finalSwitch1 = "0", finalSwitch2 = "0", finalSwitch3 = "0";

        // If not on permanent hold they will take their proper values
        if (printRoute != null) {
            int[] printSwitches = printRoute.getSwitchNumbers();
            finalSwitch1 = String.valueOf(printSwitches[0]);
            finalSwitch2 = String.valueOf(printSwitches[1]);
            finalSwitch3 = String.valueOf(printSwitches[2]);
        }

        // Print our table (Formatting is a bit goofy)
        System.out.println("Train Number " + train.getTrainNumber() + " assigned.");
        System.out.println("Train Number  \t  Inbound Track  \t  Outbound Track \t Switch 1 \t Switch 2 \t Switch 3 \t Hold \t\tDispatched \t Dispatch Sequence");
        System.out.println("----------------------------------------------------------------------------------------------------" +
       "--------------------------------------------------------------");
        System.out.println("\t" + train.getTrainNumber() + "\t\t" + train.getInboundTrack() + "\t\t\t" + train.getOutboundTrack() + "\t\t     " + finalSwitch1 + "\t\t     " 
        + finalSwitch2 + "\t\t     " + finalSwitch3 + "\t\t " + train.isHold() + "\t\t   " + train.isDispatched() + "\t\t         " + train.getDispatchSeq() + "\n");
        System.out.println("\n");
        }
    }
}
