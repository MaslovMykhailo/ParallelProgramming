package lab5;

import utils.Pair;
import utils.Randomizer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
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

    private static final int ARRAY_1_SIZE = 100;

    private static final Pair<Integer, Integer> ARRAY_1_RANGE = Pair.create(0, 1000);

    private static final int ARRAY_2_SIZE = 100;

    private static final Pair<Integer, Integer> ARRAY_2_RANGE = Pair.create(500, 1000);

    public static void main(String... args) throws ExecutionException, InterruptedException {

        List<int[]> listOfArrays = Stream
            .of(
                CompletableFuture
                    .supplyAsync(createRandomArray(ARRAY_1_SIZE, getRandomInteger(ARRAY_1_RANGE)))
                    .thenApplyAsync(log(array -> "Array1 create: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::getArrayGreaterThanAverage)
                    .thenApplyAsync(log(array -> "Array1 filtered: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::sortArray)
                    .thenApplyAsync(log(array -> "Array1 sorted: " + Arrays.toString(array))),

                CompletableFuture
                    .supplyAsync(createRandomArray(ARRAY_2_SIZE, getRandomInteger(ARRAY_2_RANGE)))
                    .thenApplyAsync(log(array -> "Array2 create: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::getArrayLessThanAverage)
                    .thenApplyAsync(log(array -> "Array2 filtered: " + Arrays.toString(array)))
                    .thenApplyAsync(Main::sortArray)
                    .thenApplyAsync(log(array -> "Array2 sorted: " + Arrays.toString(array)))
            )
            .map(CompletableFuture::join)
            .collect(Collectors.toList());

        CompletableFuture
            .supplyAsync(() -> mergeArrays(listOfArrays.get(0), listOfArrays.get(1)))
            .thenApplyAsync(log(array -> "Result: " + Arrays.toString(array)))
            .get();
    }

    private static int[] mergeArrays(int[] array1, int[] array2) {
        return Arrays
            .stream(array1)
            .distinct()
            .filter(e1 -> Arrays
                .stream(array2)
                .anyMatch(e2 -> e1 == e2)
            )
            .toArray();
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

    private static Supplier<int[]> createRandomArray(int size, Supplier<Integer> nextInteger) {
        return () -> IntStream.range(0, size).map(e -> nextInteger.get()).toArray();
    }

    private static Supplier<Integer> getRandomInteger(Pair<Integer, Integer> range) {
        return () -> Randomizer.getInRange(range);
    }

    private static <T> UnaryOperator<T> log(Function<T, String> messageGetter) {
        return t -> {
            System.out.println(messageGetter.apply(t));
            return t;
        };
    }

}
