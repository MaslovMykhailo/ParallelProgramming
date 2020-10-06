package lab4.ProducerConsumer;

public class Task {

    private final int duration;

    public Task(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public String toString() {
        return String.format("Task {duration=%d}", duration);
    }
}
