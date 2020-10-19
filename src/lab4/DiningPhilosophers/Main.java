package lab4.DiningPhilosophers;

import java.util.Arrays;

public class Main {

    private static final String[] philosopherNames = {
        "Plato",
        "Aristotle",
        "Cicero",
        "Confucius",
        "Eratosthenes"
    };

    public static void main(String... args) {
        Fork[] forks = new Fork[philosopherNames.length];

        for (int index = 0; index < forks.length; index++) {
            forks[index] = new Fork(index + 1);
        }

        Philosopher[] philosophers = new Philosopher[philosopherNames.length];

        for (int index = 0; index < philosophers.length; index++) {
            if (index != philosophers.length - 1) {
                philosophers[index] = new Philosopher(
                    philosopherNames[index],
                    forks[index],
                    forks[(index + 1) % forks.length]
                );
            } else {
                philosophers[index] = new Philosopher(
                    philosopherNames[index],
                    forks[(index + 1) % forks.length],
                    forks[index]
                );
            }
        }

        Arrays.stream(philosophers).forEach(Thread::start);
    }

}
