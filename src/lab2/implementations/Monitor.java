package lab2.implementations;

import lab2.interfaces.Process;
import lab2.interfaces.ProcessFlowMonitor;
import lab2.interfaces.ProcessorMonitor;

import java.util.HashMap;
import java.util.Map;

public class Monitor implements ProcessorMonitor, ProcessFlowMonitor {

    private final Map<String, FlowStatistic> records = new HashMap<>();

    public void reportSimulationStart() {
        System.out.println("SIMULATION STARTED");
        System.out.println("==================");
    }

    public void reportSimulationFinish() {
        System.out.println("SIMULATION FINISHED");
        System.out.println("===================");

        records.forEach((flowId, statistic) -> {
            System.out.printf("%s REPORT\n", flowId);
            System.out.println("-------------------");
            System.out.printf("TOTAL PROCESS COUNT %d\n", statistic.generatedProcessCount);
            System.out.printf("FINISHED PROCESS COUNT %d\n", statistic.finishedProcessCount);
            System.out.printf("LOST PROCESS COUNT %d\n", statistic.lostProcessCount);
            System.out.printf("KILLED PROCESS COUNT %d\n", statistic.killedProcessCount);
            System.out.println("~~~~~~~~~~~~~~~~~~~");

            float lostPercent = ((float) statistic.lostProcessCount / statistic.generatedProcessCount) * 100;
            float killedPercent = ((float) statistic.killedProcessCount / statistic.generatedProcessCount) * 100;

            System.out.printf("LOST PROCESS PERCENT %f\n", lostPercent);
            System.out.printf("KILLED PROCESS PERCENT %f\n", killedPercent);
            System.out.println("-------------------");
        });
    }

    public void reportProcessorWorkFinish() {
        System.out.println("PROCESSOR FINISHED");
    }

    public void reportProcessorWorkStart() {
        System.out.println("PROCESSOR STARTED");
    }

    public void reportFlowRegistered(String flowId) {
        records.put(flowId, new FlowStatistic());
    }

    public void reportFlowStart(String flowId) {
        System.out.printf("%s STARTED\n", flowId);
    }

    public void reportFlowFinish(String flowId) {
        System.out.printf("%s FINISHED\n", flowId);
    }

    public void reportProcessGeneration(Process process) {
        records.get(process.getOrigin()).generatedProcessCount++;
        System.out.printf(
            "PROCESS GENERATED - %s ~ %d ms\n",
            process.getOrigin(),
            process.getExecutionTime()
        );
    }

    public void reportProcessExecutionStart(Process process) {
        System.out.printf(
            "START EXECUTION - %s ~ %d ms\n",
            process.getOrigin(),
            process.getExecutionTime()
        );
    }

    public void reportProcessExecutionFinish(Process process) {
        records.get(process.getOrigin()).finishedProcessCount++;
        System.out.printf(
            "PROCESS FINISHED - %s ~ %d ms\n",
            process.getOrigin(),
            process.getExecutionTime()
        );
    }

    public void reportProcessExecutionLost(Process process) {
        records.get(process.getOrigin()).lostProcessCount++;
        System.out.printf(
            "PROCESS LOST - %s ~ %d ms\n",
            process.getOrigin(),
            process.getExecutionTime()
        );
    }

    public void reportProcessExecutionKilled(Process process) {
        records.get(process.getOrigin()).killedProcessCount++;
        System.out.printf(
            "PROCESS KILLED - %s ~ %d ms\n",
            process.getOrigin(),
            process.getExecutionTime()
        );
    }

    public void reportProcessGenerationInterruption(Process process) {
        // ignore
    }

    public void reportProcessExecutionInterruption(Process process) {
        // ignore
    }

    private static class FlowStatistic {

        public int generatedProcessCount = 0;

        public int lostProcessCount = 0;

        public int killedProcessCount = 0;

        public int finishedProcessCount = 0;

    }
}
