package lab2;

public class CPU extends Thread {

    private final Core core = new Core();

    public void run() {
        while (!interrupted()) {
            synchronized (core) {
                if (!core.hasCapturedProcess()) {
                    // CPU idles
                    continue;
                }

                CPUProcess process = core.getCapturedProcess();

                try {
                    System.out.println("Start execution " + process.getFlowId() + "-" + process.getExecutionTime());
                    CPUProcess result = core.executeProcess();

                    if (result == process) {
                        System.out.println("Finish execution " + process.getFlowId() + "-" + process.getExecutionTime());
                    } else if (result == null) {
                        System.out.println("Should be killed " + process.getFlowId() + "-" + process.getExecutionTime());
                    } else {
                        System.out.println("Should be lost " + process.getFlowId() + "-" + process.getExecutionTime());
                    }
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                } finally {
                    core.escapeProcess();
                    core.notify();
                }
            }
        }
    }

    public void execute(CPUProcess processToExecute) throws InterruptedException {
        synchronized (core) {
            if (core.hasCapturedProcess() && processToExecute != null) {
                CPUProcess capturedProcess = core.getCapturedProcess();

                if (capturedProcess.getFlowId().equals(processToExecute.getFlowId())) {
                    core.lostProcess();
                    core.wait();
                    System.out.println("Process lost " + capturedProcess.getFlowId() + "-" + capturedProcess.getExecutionTime());
                } else {
                    core.killProcess();
                    core.wait();
                    System.out.println("Process killed " + capturedProcess.getFlowId() + "-" + capturedProcess.getExecutionTime());
                }
            }
            core.captureProcess(processToExecute);
        }
    }

    private static class Core {

        private CPUProcess capturedProcess = null;

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

        public CPUProcess executeProcess() throws InterruptedException {
            if (hasCapturedProcess()) {
                wait(capturedProcess.getExecutionTime());
            }
            return capturedProcess;
        }

        public void lostProcess() {
            notify();
        }

        public void killProcess() {
            escapeProcess();
            notify();
        }
    }
}
