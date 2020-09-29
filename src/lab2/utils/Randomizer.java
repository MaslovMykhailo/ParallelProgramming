package lab2.utils;

public class Randomizer {

    public static int getInRange(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

}
