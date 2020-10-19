package lab4.ReadersWriters;

import lab4.ReadersWriters.interfaces.Writable;
import utils.Pair;
import utils.Randomizer;

public class Writer extends Thread {

    private final String id;

    private final Writable db;

    private final Pair<Integer, Integer> writingTime;

    private final Pair<Integer, Integer> idleTime;

    public Writer(
        String id,
        Writable db,
        Pair<Integer, Integer> writingTime
    ) {
        this.id = id;
        this.db = db;
        this.writingTime = writingTime;
        this.idleTime = writingTime.map(
            (t1, t2) -> Pair.create(t1 * 2, t2 * 2)
        );
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(Randomizer.getInRange(idleTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("Writer %s: Writing requested\n", id);
            db.requestWriting();

            write();

            db.releaseWriting();
            System.out.printf("Writer %s: Writing released\n", id);
        }
    }

    public void write() {
        try {
            int timeout = Randomizer.getInRange(writingTime);
            System.out.printf("Writer %s: Writing for %d ms\n", id, timeout);
            db.write(timeout);
        } catch (InterruptedException e) {
            System.out.printf("Writer %s: Writing interrupted\n", id);
        }
    }

}
