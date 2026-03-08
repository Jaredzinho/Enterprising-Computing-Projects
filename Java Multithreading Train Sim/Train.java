/*
Name: Jared Camilo
Course: CNT 4714 Spring 2026
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 15, 2026
Class: train.java
Description: This class will create a train object with the train number, inbound track, and outbound track.
I will add getter methods to help the flow of the train station
*/

// Import necessary tools
// I improted ThreadLocalRandom to help with the buffer time between trains
import java.util.concurrent.ThreadLocalRandom;

// We will create the train class and declare necessary variables
// It will implement runnable so that each train can be a thread and utilize the run method
// I decided to add volatile variables to help with multithreading and printing at the end
public class Train implements Runnable {
    private final int trainNumber;
    private final int inboundTrack;
    private final int outboundTrack; 
    private final Yard yard;
    private final Route route;
    private volatile boolean hold = false;
    private volatile boolean dispatched = false;
    private volatile int dispatchSeq = 0;

    // Set the values for the train object through the constructor
    public Train(int number, int inbound, int outbound, Route route, Yard yard) {
        this.trainNumber = number;
        this.inboundTrack = inbound;
        this.outboundTrack = outbound;
        this.route = route;
        this.yard = yard;
    }

    // This method will return the train number
    public int getTrainNumber() {
        return trainNumber;
    }

    // This method will return the inbound track number
    public int getInboundTrack() {
        return inboundTrack;
    }

    // This method will return the outbound track number
    public int getOutboundTrack() {
        return outboundTrack;
    }

    // This method will return the route object for the train
    public Route getRoute() {
        return route;
    }   

    // This method will return the flag showing if the train is on hold
    public boolean isHold() {
        return hold;
    }

    // This method will return the flag showing if the train has been dispatched
    public boolean isDispatched() {
        return dispatched;
    }

    // This method will return the dispatch sequence number
    public int getDispatchSeq() {
        return dispatchSeq;
    }   

    // This method will help the flow of code by avoiding the need to write try catch blocks for sleep every time
    private void sleepRandom() {
        int sleepTime = ThreadLocalRandom.current().nextInt(100, 500);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    // This method is exactly the same as sleepRandom but it is used for the departure time to help with readability and organization of code
    private void departRandom() {
        int departTime = ThreadLocalRandom.current().nextInt(300, 800);
        try {
            Thread.sleep(departTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    // As the name entails, this method will run the train thread
    @Override
    public void run() {


        // I create our route object using the passed in tracks
        Route route = yard.getRoute(inboundTrack, outboundTrack);


        // Prevent null routes from running
        if (route == null) {
            hold = true;

            System.out.println("\n");
            System.out.println("\t\t *************\n");
            System.out.println("\t\t Train " + getTrainNumber() + " is on permanent hold and cannot be dispatched.\n");
            System.out.println("\t\t *************\n");
            return;
        }

        // I get the switch numbers for the route and assign them as ints to make syntax easier
        int[] switchNumbers = route.getSwitchNumbers();
        int firstSwitch = switchNumbers[0];
        int secondSwitch = switchNumbers[1];
        int thirdSwitch = switchNumbers[2];

        // Create the three switch objects using the switch numbers as indices for the yard's switch array
        Switch switch1 = yard.getSwitch(firstSwitch);
        Switch switch2 = yard.getSwitch(secondSwitch);
        Switch switch3 = yard.getSwitch(thirdSwitch);

        // We will use a while loop to try acquiring every switch
       while (!dispatched) {

        // Check if the train can acquire the first switch or not
        if (!switch1.tryAcquire()) {
            System.out.println("Train " + getTrainNumber() + ": UNABLE TO LOCK first required switch: Switch " + firstSwitch+". Train will wait...");
            sleepRandom();
            continue;
        }

        // Prints if train was able to get the first switch
        System.out.println("Train " + getTrainNumber() + ": HOLDS LOCK on Switch " + firstSwitch+".");
            
        // Check if the train can acquire the second switch or not
        if (!switch2.tryAcquire()) {
            System.out.println("\nTrain " + getTrainNumber() + ": UNABLE TO LOCK second required switch: Switch " + secondSwitch+".");
            System.out.println("Train " + getTrainNumber() + ": Releasing lock on first required switch: Switch " + firstSwitch+". Train will wait...");
            switch1.release();
            sleepRandom();
            continue;
        }

        // Prints if train was able to get the second switch
        System.out.println("Train " + getTrainNumber() + ": HOLDS LOCK on Switch " + secondSwitch+".");

        // Since it couldn't get switch three, we release the first two twitches and put the train on hold
        if (!switch3.tryAcquire()) {
            System.out.println("\nTrain " + getTrainNumber() + ": UNABLE TO LOCK third required switch: Switch " + thirdSwitch+".");
            System.out.println("Train " + getTrainNumber() + ": Releasing lock on first and second required switches: Switch " 
            + firstSwitch+" and Switch " + secondSwitch+". \nTrain will wait...");
            switch1.release();
            switch2.release();
            sleepRandom();
            continue;
        }
        
        // If the train was able to acquire all three switches, we can dispatch it and release the locks
        System.out.println("Train " + getTrainNumber() + ": HOLDS LOCK on Switch " + thirdSwitch+".");

        // The train will be on hold and maintain the switches before departing
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Train " + getTrainNumber() + ": HOLDS ALL NEEDED SWITCH LOCKS - Train movement begins.\n");
        departRandom();

        // Just to ensure flow continues
        if (Thread.currentThread().isInterrupted()) {
            switch1.release();
            switch2.release();
            switch3.release();
        }

        // Train is ready to continue now
        System.out.println("\nTrain " + getTrainNumber() + ": Clear of yard control.\n");

        // We will release the locks in order of acquisiton
        System.out.println("Train " + getTrainNumber() + ": Releasing all switch locks.");
        System.out.println("Train " + getTrainNumber() + ": Unlocks/releases lock on Switch " + firstSwitch+".");
        System.out.println("Train " + getTrainNumber() + ": Unlocks/releases lock on Switch " + secondSwitch+".");
        System.out.println("Train " + getTrainNumber() + ": Unlocks/releases lock on Switch " + thirdSwitch+".\n");
        switch1.release();
        switch2.release();
        switch3.release();

        // Flag train as departed
        dispatchSeq = yard.nextSequenceNumber();
        dispatched = true;

        // Train hes left the yard!
        System.out.println("\n");
        System.out.println("\t\t TRAIN " + getTrainNumber() + ": Has been dispatched and moves on down the line out of yard control into CTC.\n");
        System.out.println("\t\t @ @ @ TRAIN " + getTrainNumber() + ": DISPATCHED @ @ @\n");
        System.out.println("\n");
        }
    }
}
