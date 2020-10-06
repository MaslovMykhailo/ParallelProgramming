package lab4.ProducerConsumer;

import utils.Randomizer;

public class Producer extends Thread {

    private final SemaphoreQueue queue;

    public Producer(SemaphoreQueue queue) {
        this.queue = queue;
    }

    public void run() {
        while (!interrupted()) {
            try {
                Task task = new Task(Randomizer.getInRange(100, 500));
                System.out.println("Start producing " + task);
                Thread.sleep(Randomizer.getInRange(10, 50));
                System.out.println("Finish producing " + task);
                queue.push(task);
            } catch (InterruptedException e) {
                System.out.println("Producer interrupted");
            }
        }
    }

}
