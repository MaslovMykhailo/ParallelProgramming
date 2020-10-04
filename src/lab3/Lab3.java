package lab3;

import utils.Randomizer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Lab3 {

    private static final int COUNT_OF_RUNS = 3;

    private static final int MIN_LENGTH = 1000;
    private static final int MAX_LENGTH = 100000;

    public static void main(String... args) {

        IntStream.range(0, COUNT_OF_RUNS).forEach(n -> {
            System.out.printf("Run #%d\n", n + 1);

            int[] array = createIntArray(Randomizer.getInRange(MIN_LENGTH, MAX_LENGTH));

            System.out.printf("Generated array of %d items\n", array.length);

            int countOfNegativeItems = getItemsCountByCondition(array, item -> item < 0);
            System.out.printf("Count of negative items -> %d\n", countOfNegativeItems);

            IndexedValue min = getMin(array);
            System.out.printf("Min -> %s\n", min);

            IndexedValue max = getMax(array);
            System.out.printf("Max ->  %s\n", max);

            int checkSum = getCheckSum(array);
            System.out.printf("Check sum -> %d\n", checkSum);

            System.out.println("<----------------------------->");
        });

    }

    private static int getItemsCountByCondition(int[] array, Function<Integer, Boolean> condition) {
        AtomicInteger itemsCount = new AtomicInteger(0);

        IntStream.of(array).parallel().forEach(item -> {
            if (condition.apply(item)) {
                itemsCount.incrementAndGet();
            }
        });

        return itemsCount.get();
    }

    private static IndexedValue getMax(int[] array) {
        return getByCondition(array, (item1, item2) -> item1 > item2);
    }

    private static IndexedValue getMin(int[] array) {
        return getByCondition(array, (item1, item2) -> item1 < item2);
    }

    private static IndexedValue getByCondition(int[] array, BiPredicate<Integer, Integer> condition) {
        if (array.length == 0) {
            return null;
        }

        AtomicInteger itemIndex = new AtomicInteger(0);

        IntStream.range(0, array.length).parallel().forEach(index -> {
            int oldIndex, newIndex;
            do {
                oldIndex = itemIndex.get();
                if (condition.test(array[oldIndex], array[index])) {
                    newIndex = oldIndex;
                } else {
                    newIndex = index;
                }
            } while (!itemIndex.compareAndSet(oldIndex, newIndex));
        });

        int index = itemIndex.get();
        return new IndexedValue(index, array[index]);
    }

    private static int getCheckSum(int[] array) {
        AtomicInteger checkSum = new AtomicInteger(0);

        IntStream.of(array).parallel().forEach(item -> {
            int oldCheckSum, newCheckSum;
            do {
                oldCheckSum = checkSum.get();
                newCheckSum = oldCheckSum ^ item;
            } while (!checkSum.compareAndSet(oldCheckSum, newCheckSum));
        });

        return checkSum.get();
    }

    private static int[] createIntArray(int length) {
        return IntStream
            .of(new int[length])
            .map(item -> Randomizer.getInRange(-1000, 1000))
            .toArray();
    }

    private static class IndexedValue {

        private final int index;

        private final int value;

        public IndexedValue(int index, int value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("[INDEX] %d [VALUE] %d", index, value);
        }

    }
}
