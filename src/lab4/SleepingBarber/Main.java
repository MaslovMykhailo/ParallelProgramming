package lab4.SleepingBarber;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
    Lab #4
    Variant 3
    Sleeping Barber problem
    Synchronisation mechanism - Synchronized / Wait / Notify
 */

public class Main {

    private static final int BARBERS_COUNT = 5;

    private static final int WAITING_ROOM_SIZE = 10;

    public static void main(String... args) {
        Monitor monitor = new Monitor();

        WaitingRoom waitingRoom = new WaitingRoom(WAITING_ROOM_SIZE);

        List<Barber> barbers = IntStream
            .range(0, BARBERS_COUNT)
            .mapToObj(index -> new Barber(String.format("Barber%d", index + 1), waitingRoom, monitor))
            .collect(Collectors.toList());

        new BarberShop(barbers, waitingRoom, monitor).start();
    }

}
