package lab4.ReadersWriters;

import lab4.ReadersWriters.interfaces.Readable;
import utils.Pair;
import utils.Randomizer;

public class Reader extends Thread {

    private final String id;

    private final Readable db;

    private final Pair<Integer, Integer> readingTime;

    private final Pair<Integer, Integer> idleTime;

    public Reader(
        String id,
        Readable db,
        Pair<Integer, Integer> readingTime
    ) {
        this.id = id;
        this.db = db;
        this.readingTime = readingTime;
        this.idleTime = readingTime.map(
            (t1, t2) -> Pair.create(t1 * 3, t2 * 3)
        );
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(Randomizer.getInRange(idleTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("Reader %s: Reading requested\n", id);
            db.requestReading();

            read();

            db.releaseReading();
            System.out.printf("Reader %s: Reading released\n", id);
        }
    }

    public void read() {
        try {
            int timeout = Randomizer.getInRange(readingTime);
            System.out.printf("Reader %s: Reading for %d ms\n", id, timeout);
            db.read(timeout);
        } catch (InterruptedException e) {
            System.out.printf("Reader %s: Reading interrupted\n", id);
        }
    }

}
