package lab4.SleepingBarber;

import utils.Randomizer;

import java.util.List;

public class BarberShop extends Thread {

    private final Monitor monitor;

    private final List<Barber> barbers;

    private final WaitingRoom waitingRoom;

    public BarberShop(List<Barber> barbers, WaitingRoom waitingRoom, Monitor monitor) {
        this.barbers = barbers;
        this.waitingRoom = waitingRoom;
        this.monitor = monitor;
    }

    public void run() {
        barbers.forEach(Thread::start);

        while (!isInterrupted()) {
            Customer customer = customerArrives();

            if (!waitingRoom.hasPlaces()) {
                customer.leave();
                continue;
            }

            waitingRoom.push(customer);

            customer.start();
        }
    }

    private int customersCount = 0;

    private Customer customerArrives() {
        try {
            Thread.sleep(Randomizer.getInRange(10, 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Customer(
            String.format("Customer%d", ++customersCount),
            Randomizer.getInRange(50, 500),
            monitor
        );
    }

}
