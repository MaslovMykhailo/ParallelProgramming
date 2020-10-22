package lab4.SleepingBarber;

public class Monitor {

    public final Object client = new Object();

    public final Object barber = new Object();

    public final Object service = new Object();

}
