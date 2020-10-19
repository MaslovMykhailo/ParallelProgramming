package lab4.ReadersWriters.interfaces;

public interface Writable {

    void requestWriting();

    void releaseWriting();

    default void write(int timeout) throws InterruptedException {
        Thread.sleep(timeout);
    }

}
