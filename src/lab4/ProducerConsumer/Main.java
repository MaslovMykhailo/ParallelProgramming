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

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

    }

}
