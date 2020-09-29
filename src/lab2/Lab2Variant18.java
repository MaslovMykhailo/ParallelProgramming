package lab2;

import lab2.implementations.CPUProcessFlowConfiguration;
import lab2.implementations.Simulation;

public class Lab2Variant18 {

    public static void main(String[] args) {
        new Simulation().simulate(

            new CPUProcessFlowConfiguration
                .Builder()
                .setProcessAmount(100)
                .setProcessExecutionTimeRange(10, 20)
                .setProcessGenerationTimeRange(15, 30)
                .build(),

            new CPUProcessFlowConfiguration
                .Builder()
                .setProcessAmount(100)
                .setProcessExecutionTimeRange(10, 20)
                .setProcessGenerationTimeRange(15, 30)
                .build()

        );
    }

}
