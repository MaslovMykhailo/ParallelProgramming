package lab2.implementations;

public class CPUProcessFlowConfiguration {

    private final int processAmount;

    private final int minProcessExecutionTime;

    private final int maxProcessExecutionTime;

    private final int minProcessGenerationTime;

    private final int maxProcessGenerationTime;

    public CPUProcessFlowConfiguration(
        int processAmount,
        int minProcessExecutionTime,
        int maxProcessExecutionTime,
        int minProcessGenerationTime,
        int maxProcessGenerationTime
    ) {
        this.processAmount = processAmount;
        this.minProcessExecutionTime = minProcessExecutionTime;
        this.maxProcessExecutionTime = maxProcessExecutionTime;
        this.minProcessGenerationTime = minProcessGenerationTime;
        this.maxProcessGenerationTime = maxProcessGenerationTime;
    }

    public int getProcessAmount() {
        return processAmount;
    }

    public int getMinProcessExecutionTime() {
        return minProcessExecutionTime;
    }

    public int getMaxProcessExecutionTime() {
        return maxProcessExecutionTime;
    }

    public int getMinProcessGenerationTime() {
        return minProcessGenerationTime;
    }

    public int getMaxProcessGenerationTime() {
        return maxProcessGenerationTime;
    }

    public static class Builder {

        private int processAmount;

        private int minExecutionTime;

        private int maxExecutionTime;

        private int minGenerationTime;

        private int maxGenerationTime;

        public Builder setProcessAmount(int processAmount) {
            this.processAmount = processAmount;
            return this;
        }

        public Builder setProcessExecutionTimeRange(int min, int max) {
            this.minExecutionTime = min;
            this.maxExecutionTime = max;
            return this;
        }

        public Builder setProcessGenerationTimeRange(int min, int max) {
            this.minGenerationTime = min;
            this.maxGenerationTime = max;
            return this;
        }

        public CPUProcessFlowConfiguration build() {
            return new CPUProcessFlowConfiguration(
                processAmount,
                minExecutionTime,
                maxExecutionTime,
                minGenerationTime,
                maxGenerationTime
            );
        }

    }

}
