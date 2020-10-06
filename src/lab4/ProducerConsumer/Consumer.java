package lab4.ProducerConsumer;

public class Consumer extends Thread {

    private final SemaphoreQueue queue;

    public Consumer(SemaphoreQueue queue) {
        this.queue = queue;
    }

    public void run() {
        while (!interrupted()) {
            try {
                Task task = queue.pop();
                System.out.println("Start consuming " + task);
                Thread.sleep(task.getDuration());
                System.out.println("Finish consuming " + task);
            } catch (InterruptedException e) {
                System.out.println("Producer interrupted");
            }
        }
    }

}
