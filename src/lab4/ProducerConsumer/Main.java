package lab4.ProducerConsumer;

/*
    Lab #4
    Variant 3
    Producer Consumer problem
    Synchronisation mechanism - Semaphore
 */

public class Main {

    public static void main(String... args) {

        SemaphoreQueue queue = new SemaphoreQueue(8);

        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);

        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        Consumer consumer3 = new Consumer(queue);

        producer1.start();
        producer2.start();

        consumer1.start();
        consumer2.start();
        consumer3.start();

    }

}
