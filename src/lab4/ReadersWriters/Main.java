package lab4.ReadersWriters;

import lab4.ReadersWriters.database.Database;
import lab4.ReadersWriters.database.EqualityDatabase;
import lab4.ReadersWriters.interfaces.Readable;
import lab4.ReadersWriters.interfaces.Writable;
import utils.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int READERS_COUNT = 3;

    private static final int WRITERS_COUNT = 2;

    private static final Pair<Integer, Integer> READING_TIME = Pair.create(10, 50);

    private static final Pair<Integer, Integer> WRITING_TIME = Pair.create(50, 100);

    public static void main(String... args) {
        Database db = new EqualityDatabase();
//        Database db = new PrivilegedDatabase();

        List<Reader> readers = createReaders(db);
        List<Writer> writers = createWriters(db);

        readers.forEach(Thread::start);
        writers.forEach(Thread::start);
    }

    private static List<Reader> createReaders(Readable db) {
        return IntStream
            .range(0, READERS_COUNT)
            .mapToObj(index -> new Reader(String.format("R%d", index + 1), db, READING_TIME))
            .collect(Collectors.toList());
    }

    private static List<Writer> createWriters(Writable db) {
        return IntStream
            .range(0, WRITERS_COUNT)
            .mapToObj(index -> new Writer(String.format("W%d", index + 1), db, WRITING_TIME))
            .collect(Collectors.toList());
    }

}
