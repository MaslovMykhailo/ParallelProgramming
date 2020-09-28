package lab2;

public class CPUProcess {

    private final String flowId;

    private final int executionTime;

    public CPUProcess(String flowId, int executionTime) {
        this.flowId = flowId;
        this.executionTime = executionTime;
    }

    public String getFlowId() {
        return flowId;
    }

    public int getExecutionTime() {
        return executionTime;
    }

}
