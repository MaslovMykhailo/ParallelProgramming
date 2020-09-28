package lab2;

public class CPUProcessFlow extends Thread {
    private final String flowId;
    private final CPU cpu;

    private int processAmount;

    private final int minExecutionTime;
    private final int maxExecutionTime;

    private final int minGenerationTime;
    private final int maxGenerationTime;

    public CPUProcessFlow(
        String flowId,
        CPU cpu,
        int processAmount,
        int minExecutionTime,
        int maxExecutionTime,
        int minGenerationTime,
        int maxGenerationTime
    ) {
        this.flowId = flowId;
        this.cpu = cpu;
        this.processAmount = processAmount;
        this.minExecutionTime = minExecutionTime;
        this.maxExecutionTime = maxExecutionTime;
        this.minGenerationTime = minGenerationTime;
        this.maxGenerationTime = maxGenerationTime;
    }

    public void run() {
        while (processAmount > 0) {
            cpu.execute(generateProcess());
            processAmount--;
        }
    }

    private CPUProcess generateProcess() {
        try {
            Thread.sleep(getProcessGenerationTime());
        } catch (InterruptedException e) {
            System.out.println("Generate process interrupted " + flowId);
        }
        return new CPUProcess(flowId, getProcessExecutionTime());
    }

    private int getProcessGenerationTime() {
        return Randomizer.getInRange(minGenerationTime, maxGenerationTime);
    }

    private int getProcessExecutionTime() {
        return Randomizer.getInRange(minExecutionTime, maxExecutionTime);
    }
}
