package lab4.ProducerConsumer;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class SemaphoreQueue {

    private final LinkedList<Task> list = new LinkedList<>();

    private final Semaphore mutex = new Semaphore(1);

    private final Semaphore consumptionSemaphore;

    private final Semaphore productionSemaphore;

    public SemaphoreQueue(int capacity) {
        consumptionSemaphore = new Semaphore(0);
        productionSemaphore = new Semaphore(capacity);
    }

    public void push(Task task) throws InterruptedException {
        productionSemaphore.acquire();
        mutex.acquire();

        list.push(task);
        System.out.println("Pushed: " + task);
        System.out.println("Queue: " + list);

        mutex.release();
        consumptionSemaphore.release();
    }

    public Task pop() throws InterruptedException {
        consumptionSemaphore.acquire();
        mutex.acquire();

        Task task = list.pop();
        System.out.println("Popped: " + task);
        System.out.println("Queue: " + list);

        mutex.release();
        productionSemaphore.release();

        return task;
    }

}
