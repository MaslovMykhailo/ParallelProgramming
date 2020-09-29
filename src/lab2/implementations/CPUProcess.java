package lab2.implementations;

import lab2.interfaces.Process;

public class CPUProcess implements Process {

    private final String origin;

    private final int executionTime;

    public CPUProcess(String origin, int executionTime) {
        this.origin = origin;
        this.executionTime = executionTime;
    }

    public String getOrigin() {
        return origin;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public boolean hasSameOrigin(Process process) {
        return origin.equals(process.getOrigin());
    }

}
