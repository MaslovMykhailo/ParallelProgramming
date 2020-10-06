package lab4.ProducerConsumer;

public class Main {

    public static void main(String... args) {

        SemaphoreQueue queue = new SemaphoreQueue();

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

    }

}
