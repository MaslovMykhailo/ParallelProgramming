package lab2.implementations;

import lab2.interfaces.Monitorable;
import lab2.interfaces.ProcessFlowMonitor;
import utils.Randomizer;

import java.util.Optional;

public class CPUProcessFlow extends Thread implements Monitorable<ProcessFlowMonitor> {

    private final String id;

    private final CPU cpu;

    private final CPUProcessFlowConfiguration configuration;

    private int processAmount;

    public CPUProcessFlow(
        String id,
        CPU cpu,
        CPUProcessFlowConfiguration configuration
    ) {
        this.id = id;
        this.cpu = cpu;
        this.configuration = configuration;
        this.processAmount = configuration.getProcessAmount();
    }

    public void run() {
        Optional.ofNullable(monitor).ifPresent(
            monitor -> monitor.reportFlowStart(id)
        );

        while (processAmount > 0) {
            cpu.execute(generateProcess());
            processAmount--;
        }

        Optional.ofNullable(monitor).ifPresent(
            monitor -> monitor.reportFlowFinish(id)
        );
    }

    private CPUProcess generateProcess() {
        CPUProcess process = new CPUProcess(id, getProcessExecutionTime());

        try {
            Thread.sleep(getProcessGenerationTime());
            Optional.ofNullable(monitor).ifPresent(
                monitor -> monitor.reportProcessGeneration(process)
            );
        } catch (InterruptedException e) {
            Optional.ofNullable(monitor).ifPresent(
                monitor -> monitor.reportProcessGenerationInterruption(process)
            );
        }

        return process;
    }

    private int getProcessGenerationTime() {
        return Randomizer.getInRange(
            configuration.getMinProcessGenerationTime(),
            configuration.getMaxProcessGenerationTime()
        );
    }

    private int getProcessExecutionTime() {
        return Randomizer.getInRange(
            configuration.getMinProcessExecutionTime(),
            configuration.getMaxProcessExecutionTime()
        );
    }

    private ProcessFlowMonitor monitor = null;

    public CPUProcessFlow withMonitor(ProcessFlowMonitor monitor) {
        this.monitor = monitor;
        monitor.reportFlowRegistered(id);
        return this;
    }

}
