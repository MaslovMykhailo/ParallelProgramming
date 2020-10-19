package lab4.ReadersWriters.database;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class EqualityDatabase implements Database {

    ReentrantLock rwLock = new ReentrantLock();

    Condition rwChangeCondition = rwLock.newCondition();

    /**
     * [activeReadersCount] could be equal -1
     * it means that writing is processing
     */
    private int activeReadersCount = 0;

    public void requestReading() {
        rwLock.lock();
        try {
            while (activeReadersCount == -1) {
                rwChangeCondition.await();
            }
            activeReadersCount++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwLock.unlock();
        }
    }

    public void releaseReading() {
        rwLock.lock();
        activeReadersCount--;
        rwChangeCondition.signal();
        rwLock.unlock();
    }

    public void requestWriting() {
        rwLock.lock();
        try {
            while (activeReadersCount != 0) {
                try {
                    rwChangeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            activeReadersCount = -1;
        } finally {
            rwLock.unlock();
        }
    }

    public void releaseWriting() {
        rwLock.lock();
        activeReadersCount = 0;
        rwChangeCondition.signal();
        rwLock.unlock();
    }

}
