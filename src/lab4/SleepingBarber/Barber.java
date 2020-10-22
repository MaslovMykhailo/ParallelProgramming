package lab4.SleepingBarber;

public class Barber extends Thread {

    private final Monitor monitor;

    private final String barberName;

    private final WaitingRoom waitingRoom;

    public Barber(String barberName, WaitingRoom waitingRoom, Monitor monitor) {
        this.monitor = monitor;
        this.barberName = barberName;
        this.waitingRoom = waitingRoom;
    }

    public void run() {
        while (!interrupted()) {
            if (!waitingRoom.hasCustomers()) {
                waitForCustomer();
            }

            Customer customer = inviteCustomer();

            serviceCustomer(customer);
        }
    }

    private void waitForCustomer() {
        try {
            synchronized (monitor.client) {
                monitor.client.wait(); // wait for customer
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Customer inviteCustomer() {
        synchronized (monitor.barber) {
            monitor.barber.notify(); // invite customer
        }

        Customer customer = waitingRoom.pop();

        System.out.printf(
            "%s was invited for hair cutting by %s\n",
            customer.getCustomerName(),
            getBarberName()
        );

        return customer;
    }

    private void serviceCustomer(Customer customer) {
        try {
            Thread.sleep(customer.getHairCuttingTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf(
            "%s was serviced by %s\n",
            customer.getCustomerName(),
            getBarberName()
        );

        synchronized (monitor.service) {
            monitor.service.notify(); // service finished
        }
    }

    public String getBarberName() {
        return barberName;
    }

}
