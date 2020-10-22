package lab4.SleepingBarber;

import java.util.LinkedList;

public class WaitingRoom {

    private final int capacity;

    private final LinkedList<Customer> queue = new LinkedList<>();

    public WaitingRoom(int capacity) {
        this.capacity = capacity;
    }

    public synchronized Customer pop() {
        if (queue.isEmpty()) {
            return null;
        }

        return queue.pop();
    }

    public synchronized void push(Customer customer) {
        if (queue.size() >= capacity) {
            return;
        }

        queue.push(customer);
    }

    public synchronized boolean hasPlaces() {
        return queue.size() < capacity;
    }

    public synchronized boolean hasCustomers() {
        return queue.size() > 0;
    }

}
