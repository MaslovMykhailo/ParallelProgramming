package lab4.DiningPhilosophers;

import utils.Randomizer;

public class Philosopher extends Thread {

    private final String name;

    private final Fork leftFork;

    private final Fork rightFork;

    public Philosopher(String name, Fork leftFork, Fork rightFork) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public void run() {
        while (!interrupted()) {
            think();

            pickUpFork(leftFork);
            pickUpFork(rightFork);

            eat();

            putDownFork(leftFork);
            putDownFork(rightFork);
        }
    }

    private void think() {
        doAction(String.format("Philosopher %s is thinking", name));
    }

    private void eat() {
        doAction(String.format("Philosopher %s is eating", name));
    }

    private void pickUpFork(Fork fork) {
        doAction(String.format("Philosopher %s picks up fork-%d", name, fork.getIndex()));
        fork.pickUp();
    }

    private void putDownFork(Fork fork) {
        doAction(String.format("Philosopher %s puts down fork-%d", name, fork.getIndex()));
        fork.putDown();
    }

    private static void doAction(String actionDescription) {
        System.out.println(actionDescription);
        try {
            Thread.sleep(Randomizer.getInRange(10, 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
