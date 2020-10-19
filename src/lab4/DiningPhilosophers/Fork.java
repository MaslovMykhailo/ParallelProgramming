package lab4.DiningPhilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private boolean isBusy = false;

    private final int index;

    public Fork(int index) {
        this.index = index;
    }

    public void pickUp() {
        lock.lock();

        while (isBusy) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isBusy = true;

        lock.unlock();
    }

    public void putDown() {
        lock.lock();

        isBusy = false;
        condition.signal();

        lock.unlock();
    }

    public int getIndex() {
        return index;
    }

}
