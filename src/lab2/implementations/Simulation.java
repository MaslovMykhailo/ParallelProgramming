package lab2.implementations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Simulation {

    private final Monitor monitor = new Monitor();

    private final CPU processor = new CPU().withMonitor(monitor);

    public void simulate(CPUProcessFlowConfiguration... configurations) {
        monitor.reportSimulationStart();

        List<CPUProcessFlow> flows = IntStream
            .range(0, configurations.length)
            .mapToObj(
                index -> new CPUProcessFlow(
                    createFlowId(index),
                    processor,
                    configurations[index]
                ).withMonitor(monitor)
            )
            .collect(Collectors.toList());

        processor.start();
        flows.forEach(Thread::start);

        try {
            for (CPUProcessFlow flow : flows) {
                flow.join();
            }

            Simulation.waitForExecution(configurations);
            processor.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        monitor.reportSimulationFinish();
    }

    private static String createFlowId(int index) {
        return String.format("FLOW:%d", index + 1);
    }

    private static void waitForExecution(
        CPUProcessFlowConfiguration... configurations
    ) throws InterruptedException {
        Thread.sleep(
            Collections.max(
                Arrays.asList(configurations),
                Comparator.comparingInt(CPUProcessFlowConfiguration::getMaxProcessExecutionTime)
            ).getMaxProcessExecutionTime()
        );
    }

}
