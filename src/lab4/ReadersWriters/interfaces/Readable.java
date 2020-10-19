package lab4.ReadersWriters.interfaces;

public interface Readable {

    void requestReading();

    void releaseReading();

    default void read(int timeout) throws InterruptedException {
        Thread.sleep(timeout);
    }

}
