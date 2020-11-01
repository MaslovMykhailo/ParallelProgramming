package lab5;

import utils.Randomizer;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Variant 8
 * 1) Create 2 arrays (collections) with random numbers
 * 2) In first array leave elements which are greater than average value
 * 3) In second array leave elements which are less than average value
 * 4) Sort arrays
 * 5) Merge elements that are present in both arrays into third array
 */

public class Main {

    private static final int ARRAY_SIZE = 100;

    public static void main(String... args) {

        int[] result = Stream
            .of(
                CompletableFuture
                    .supplyAsync(Main::createRandomArray)
                    .thenApplyAsync(log(array -> "Array1 create: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::getArrayGreaterThanAverage)
                    .thenApplyAsync(log(array -> "Array1 filtered: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::sortArray)
                    .thenApplyAsync(log(array -> "Array1 sorted: " + Arrays.toString(array))),

                CompletableFuture
                    .supplyAsync(Main::createRandomArray)
                    .thenApplyAsync(log(array -> "Array2 create: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::getArrayLessThanAverage)
                    .thenApplyAsync(log(array -> "Array2 filtered: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::sortArray)
                    .thenApplyAsync(log(array -> "Array2 sorted: " + Arrays.toString(array)))
            )
            .map(CompletableFuture::join)
            .collect(mergeCollector);

        System.out.println("Result: " + Arrays.toString(result));

    }

    private static final Collector<int[], int[], int[]> mergeCollector = Collector.of(
        () -> new int[]{},
        Main::mergeArrays,
        Main::mergeArrays,
        Collector.Characteristics.UNORDERED
    );

    private static int[] mergeArrays(int[] array1, int[] array2) {
        return IntStream.concat(Arrays.stream(array1), Arrays.stream(array2)).distinct().toArray();
    }

    private static int[] sortArray(int[] array) {
        return Arrays.stream(array).sorted().toArray();
    }

    private static int[] getArrayLessThanAverage(int[] array) {
        int average = getArrayAverage(array);
        return filterArray(array, e -> e < average);
    }

    private static int[] getArrayGreaterThanAverage(int[] array) {
        int average = getArrayAverage(array);
        return filterArray(array, e -> e > average);
    }

    private static int[] filterArray(int[] array, IntPredicate predicate) {
        return Arrays.stream(array).filter(predicate).toArray();
    }

    private static int getArrayAverage(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException();
        }
        return Arrays.stream(array).reduce(0, Integer::sum) / array.length;
    }

    private static int[] createRandomArray() {
        return IntStream.range(0, ARRAY_SIZE).map(e -> getRandomInteger()).toArray();
    }

    private static int getRandomInteger() {
        return Randomizer.getInRange(0, ARRAY_SIZE);
    }

    private static <T> UnaryOperator<T> log(Function<T, String> messageGetter) {
        return t -> {
            System.out.println(messageGetter.apply(t));
            return t;
        };
    }

}
