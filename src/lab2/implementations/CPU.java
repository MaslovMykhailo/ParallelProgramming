package lab2.implementations;

import lab2.interfaces.Monitorable;
import lab2.interfaces.ProcessorMonitor;

import java.util.Optional;

public class CPU extends Thread implements Monitorable<ProcessorMonitor> {

    private final Core core = new Core();

    public void run() {
        Optional.ofNullable(monitor).ifPresent(ProcessorMonitor::reportProcessorWorkStart);

        while (!interrupted()) {
            synchronized (core) {
                if (!core.hasCapturedProcess()) {
                    continue; // processor idles
                }

                CPUProcess process = core.getCapturedProcess();

                try {
                    Optional.ofNullable(monitor).ifPresent(
                        monitor -> monitor.reportProcessExecutionStart(process)
                    );

                    CPUProcessStatus status = core.executeProcess();
                    switch (status) {
                        case FINISHED -> Optional.ofNullable(monitor).ifPresent(
                            monitor -> monitor.reportProcessExecutionFinish(process)
                        );
                        case KILLED -> Optional.ofNullable(monitor).ifPresent(
                            monitor -> monitor.reportProcessExecutionKilled(process)
                        );
                        case LOST -> Optional.ofNullable(monitor).ifPresent(
                            monitor -> monitor.reportProcessExecutionLost(process)
                        );
                    }
                } catch (InterruptedException e) {
                    Optional.ofNullable(monitor).ifPresent(
                        monitor -> monitor.reportProcessExecutionInterruption(process)
                    );
                }
            }
        }

        Optional.ofNullable(monitor).ifPresent(ProcessorMonitor::reportProcessorWorkFinish);
    }

    public void execute(CPUProcess processToExecute) {
        synchronized (core) {
            if (core.hasCapturedProcess() && processToExecute != null) {
                if (core.getCapturedProcess().hasSameOrigin(processToExecute)) {
                    core.loseProcess();
                } else {
                    core.killProcess();
                }
            }
            core.captureProcess(processToExecute);
        }
    }

    private ProcessorMonitor monitor = null;

    public CPU withMonitor(ProcessorMonitor monitor) {
        this.monitor = monitor;
        return this;
    }

    private enum CPUProcessStatus {EXECUTING, LOST, KILLED, FINISHED}

    private static class Core {

        private CPUProcess capturedProcess = null;

        private CPUProcessStatus status = null;

        public void captureProcess(CPUProcess process) {
            capturedProcess = process;
        }

        public void escapeProcess() {
            capturedProcess = null;
        }

        public CPUProcess getCapturedProcess() {
            return capturedProcess;
        }

        public boolean hasCapturedProcess() {
            return capturedProcess != null;
        }

        public CPUProcessStatus executeProcess() throws InterruptedException {
            status = null;

            if (hasCapturedProcess()) {
                status = CPUProcessStatus.EXECUTING;
                wait(capturedProcess.getExecutionTime());
            }

            if (status == CPUProcessStatus.EXECUTING) {
                escapeProcess();
                return CPUProcessStatus.FINISHED;
            }

            return status;
        }

        public void loseProcess() {
            escapeProcess();
            status = CPUProcessStatus.LOST;
            notify();
        }

        public void killProcess() {
            escapeProcess();
            status = CPUProcessStatus.KILLED;
            notify();
        }

    }

}
