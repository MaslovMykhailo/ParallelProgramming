package lab2;

public class Lab2Variant18 {

    private static class SystemSimulation {

        private final int PROCESSES_AMOUNT = 10;

        private final int MIN_PROCESS_EXECUTION_TIME = 10;
        private final int MAX_PROCESS_EXECUTION_TIME = 100;

        private final int MIN_PROCESS_GENERATION_TIME = 10;
        private final int MAX_PROCESS_GENERATION_TIME = 100;

        public void operate() {
            CPU cpu = new CPU();

            CPUProcessFlow flow1 = new CPUProcessFlow(
                "flow-1",
                cpu,
                PROCESSES_AMOUNT,
                MIN_PROCESS_EXECUTION_TIME,
                MAX_PROCESS_EXECUTION_TIME,
                MIN_PROCESS_GENERATION_TIME,
                MAX_PROCESS_GENERATION_TIME
            );

            CPUProcessFlow flow2 = new CPUProcessFlow(
                "flow-2",
                cpu,
                PROCESSES_AMOUNT,
                MIN_PROCESS_EXECUTION_TIME,
                MAX_PROCESS_EXECUTION_TIME,
                MIN_PROCESS_GENERATION_TIME,
                MAX_PROCESS_GENERATION_TIME
            );

            flow1.start();
            flow2.start();

            try {
                flow1.join();
                flow1.join();
            } catch (InterruptedException e) {
                System.out.println("Join interrupted");
            }
        }
    }

    public static void main(String[] args) {
        new SystemSimulation().operate();
    }
}
