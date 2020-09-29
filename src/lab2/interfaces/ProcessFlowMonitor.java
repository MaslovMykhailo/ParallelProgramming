package lab2.interfaces;

public interface ProcessFlowMonitor {

    void reportFlowRegistered(String flowId);

    void reportFlowStart(String flowId);

    void reportFlowFinish(String flowId);

    void reportProcessGeneration(Process process);

    void reportProcessGenerationInterruption(Process process);

}
