package utils;

public class Randomizer {

    public static int getInRange(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static int getInRange(Pair<Integer, Integer> range) {
        int max = Math.max(range.getV1(), range.getV2());
        int min = Math.min(range.getV1(), range.getV2());
        return (int) (Math.random() * (max - min) + min);
    }

}
