/*
Name: Jared Camilo
Course: CNT 4714 Spring 2026
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 15, 2026
Class: route.java
Description: I created this class so I could make objects of each route for the trains.
I'm going to add getter methods so that yard can just call them to match the route to the train
*/

// We will create the route class and declare necessary variables
public class Route {
    private final int inboundTrack;
    private final int outboundTrack;
    private final int[] switchNumbers;

    // Set the values through the constructor to create the object
    public Route(int inbound, int outbound, int[] switchNumbers) {
        this.inboundTrack = inbound;
        this.outboundTrack = outbound;
        this.switchNumbers = switchNumbers;
    }

    // This method will return the inbound track number
    public int getInboundTrack() {
        return inboundTrack;
    }

    // This method will return the outbound track number
    public int getOutboundTrack() {
        return outboundTrack;
    }

    // This method will return the 3 switch numbers necessary for the route
    public int[] getSwitchNumbers() {
        return switchNumbers;
    }
}
