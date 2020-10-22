package lab4.SleepingBarber;

public class Customer extends Thread {

    private final String customerName;

    private final int hairCuttingTime;

    private final Monitor monitor;

    public Customer(String customerName, int hairCuttingTime, Monitor monitor) {
        this.customerName = customerName;
        this.hairCuttingTime = hairCuttingTime;
        this.monitor = monitor;
    }

    public void run() {
        arrive();
        waitForBarber();
        waitForService();
        leave();
    }

    public void arrive() {
        synchronized (monitor.client) {
            monitor.client.notify(); // wake up barber
        }
        System.out.printf("%s arrived\n", getCustomerName());
    }

    public void leave() {
        System.out.printf("%s left\n", getCustomerName());
    }

    private void waitForBarber() {
        try {
            synchronized (monitor.barber) {
                monitor.barber.wait(); // wait for barber
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForService() {
        try {
            synchronized (monitor.service) {
                monitor.service.wait(); // wait for service
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getHairCuttingTime() {
        return hairCuttingTime;
    }

}
