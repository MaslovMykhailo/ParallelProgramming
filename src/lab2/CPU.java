package lab2;

public class CPU extends Thread {

    private CPUProcess process = null;

    public void run() {
        while (!interrupted()) {
            CPUProcess currentProcess = process;

            if (currentProcess == null) {
                continue;
            }

            try {
                System.out.println("Start execution " + currentProcess.getFlowId());
                Thread.sleep(currentProcess.getExecutionTime());
            } catch (InterruptedException e) {
                System.out.println("Interrupted execution " + currentProcess.getFlowId());
            } finally {
                process = null;
            }
        }
    }

    public synchronized void execute(CPUProcess processToExecute) {
        if (process != null && processToExecute != null) {
            if (process.getFlowId().equals(processToExecute.getFlowId())) {
                System.out.println("Process lost " + process.getFlowId());
            } else {
                System.out.println("Process killed " + process.getFlowId());
            }

            if (!interrupted()) {
                interrupt();
            }
        }
    }

}
