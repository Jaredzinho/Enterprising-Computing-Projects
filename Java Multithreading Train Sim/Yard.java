/*
Name: Jared Camilo
Course: CNT 4714 Spring 2026
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 15, 2026
Class: yard.java
Description: This class will represent a yard in a train station.
It will essentially be a collection of switches and routes, and will have methods to acquire and release switches for a given route.
*/

// Import necessary tools
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// We will create the yard class with arrays of route and switch objects
// I made sure to use an atomic integer to avoid improper incrementation during multithreading
public class Yard {
    private final Switch switches[];
    private final Route routes[];
    private final AtomicInteger sequenceNumber;

    // Set the values for the yard object through the constructor
    public Yard(Switch[] switches, ArrayList<Route> routes) {
        this.switches = switches;
        this.routes = routes.toArray(new Route[0]);
        this.sequenceNumber = new AtomicInteger(0);
    }

    // This method will return the route object that matches the given inbound and outbound track numbers
    public Route getRoute(int inboundTrack, int outboundTrack) {   
        for (Route route : routes) {
            if (route.getInboundTrack() == inboundTrack && route.getOutboundTrack() == outboundTrack) {
                return route;
            }
        }
        return null; 
    }

    // This method will return the switch number using the switch number as an index for the switch array
    public Switch getSwitch(int switchNumber) {
        return switches[switchNumber];
    }

    // This method will return the sequrence number and increment it for the next train
    public int nextSequenceNumber() {
        return sequenceNumber.incrementAndGet();
    }
}
