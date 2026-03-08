/*
Name: Jared Camilo
Course: CNT 4714 Spring 2026
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 15, 2026
Class: switch.java
Description: This class will create a switch object and follows certain locking mechanisms to avoid bugs
*/

// Import necessary tools
import java.util.concurrent.locks.ReentrantLock;

// We will create the switch class and declare necessary variables
public class Switch {
    private final int switchNumber;
    private final ReentrantLock lock;
    
    // Set the values for the switch object through the constructor
    public Switch(int number) {
        this.switchNumber = number;
        this.lock = new ReentrantLock();
    }

    // This method will return the switch number
    public int getSwitchNumber() {
        return switchNumber;
    }

    // This method will acquire the lock for the switch
    // It utilizes tryLock to avoid deadlocks 
    public boolean tryAcquire() {
        return lock.tryLock();
    }

    // This method will release the lock for the switch
    // I added a check to ensure that the lock is only released by the correct thread
    public void release() {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }   
    }
}
