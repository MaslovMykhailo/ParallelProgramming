package lab4.ReadersWriters.database;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrivilegedDatabase implements Database {

    ReentrantLock rwLock = new ReentrantLock();

    Condition rwChangeCondition = rwLock.newCondition();

    private int activeReadersCount = 0;

    Condition writeCondition = rwLock.newCondition();

    private int requestedWritersCount = 0;

    public void requestReading() {
        rwLock.lock();
        try {
            while (requestedWritersCount > 0) {
                writeCondition.await();
            }
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
        requestedWritersCount++;
        try {
            while (requestedWritersCount != 1) {
                writeCondition.await();
            }
            while (activeReadersCount != 0) {
                rwChangeCondition.await();
            }
            activeReadersCount = -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rwLock.unlock();
        }
    }

    public void releaseWriting() {
        rwLock.lock();

        requestedWritersCount--;
        writeCondition.signalAll();

        activeReadersCount = 0;
        rwChangeCondition.signal();

        rwLock.unlock();
    }
}
