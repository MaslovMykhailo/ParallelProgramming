package lab2.interfaces;

public interface ProcessorMonitor {

    void reportProcessorWorkStart();

    void reportProcessorWorkFinish();

    void reportProcessExecutionStart(Process process);

    void reportProcessExecutionKilled(Process process);

    void reportProcessExecutionFinish(Process process);

    void reportProcessExecutionLost(Process process);

    void reportProcessExecutionInterruption(Process process);

}
