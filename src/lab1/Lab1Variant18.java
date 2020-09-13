package lab1;

/*
  Formula of integration by trapezoidal rule
  ∫ f(x)dx from a to b = (h / 2) * (f(x0) + 2 * (∑ f(xi) from i = 1 to i = n - 1) + f(xn))
  where
  h = (b - a) / n
  xi = a + i * h
 */

/*
  Formula of speedup
  S = Ts / Tp
  where
  Ts - time of serial execution
  Tp - time of parallel execution
 */

/*
  Formula of efficiency
  E = Ts / (p * Tp)
  where
  Ts - time of serial execution
  Tp - time of parallel execution
  p - count of processes or threads
 */

class FunctionConfiguration {
    interface Function {
        double calculate(double x);
    }

    public Function function = (x) -> x * Math.sin(x); // f(x) in formula
    public Function derivativeFunction = (x) -> Math.sin(x) - x * Math.cos(x);

    private final double integrateFrom; // a in formula
    private final double integrateTo; // b in formula

    private final int integrationStepsCount; // n in formula
    private final double trapezoidHeight; // h in formula

    public FunctionConfiguration(
        double integrateFrom,
        double integrateTo,
        double stepOfIntegration
    ) {
        this.integrateFrom = integrateFrom;
        this.integrateTo = integrateTo;

        this.integrationStepsCount = (int) (
            (integrateTo - integrateFrom) / stepOfIntegration
        );
        this.trapezoidHeight = (integrateTo - integrateFrom) /
            this.integrationStepsCount;
    }

    public int getIntegrationStepsCount() {
        return integrationStepsCount;
    }

    public double getXi(int i) {
        return integrateFrom + i * trapezoidHeight;
    }

    public double getResult(double integrationSum) {
        double fX0 = function.calculate(getXi(0));
        double fXn = function.calculate(getXi(integrationStepsCount));

        return (trapezoidHeight / 2) * (fX0 + 2 * integrationSum + fXn);
    }

    public double getDerivativeResult() {
        return derivativeFunction.calculate(integrateTo) -
            derivativeFunction.calculate(integrateFrom);
    }
}

class IntegrationCalculator {
    private final FunctionConfiguration configuration;

    private final int fromIndex;
    private final int toIndex;

    private double integrationSum = 0;

    public double getIntegrationSum() {
        return integrationSum;
    }

    public IntegrationCalculator(
        FunctionConfiguration configuration,
        int fromIndex,
        int toIndex
    ) {
        this.configuration = configuration;

        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public double calculate(int i) {
        return configuration.function.calculate(configuration.getXi(i));
    }

    public void execute() {
        for (int i = fromIndex; i <= toIndex; i++) {
            integrationSum += this.calculate(i);
        }
    }
}

class IntegrationThreadCalculator extends IntegrationCalculator implements Runnable {
    public IntegrationThreadCalculator(
        FunctionConfiguration configuration,
        int fromIndex,
        int toIndex
    ) {
        super(configuration, fromIndex, toIndex);
    }

    public void run() {
        this.execute();
    }
}

public class Lab1Variant18 {

    private static final double THRESHOLD = 0.000001;

    private static void checkResult(double result, double accurateResult) {
        double check = Math.abs(1 - result / accurateResult);
        if (check < THRESHOLD) {
            System.out.println("Check passed!");
        } else {
            System.out.println("Check failed!");
        }
    }

    private static double calculateSpeedUp(
        double serialExecutionTime,
        double parallelExecutionTime
    ) {
        return serialExecutionTime / parallelExecutionTime;
    }

    private static double calculateEfficiency(
        double serialExecutionTime,
        double parallelExecutionTime,
        int threadCount
    ) {
        return serialExecutionTime / (threadCount * parallelExecutionTime) * 100;
    }

    private static double calculateParallel(
        FunctionConfiguration configuration,
        int threadCount
    ) throws InterruptedException {
        System.out.println("\nParallel execution in " + threadCount + " threads");

        long start = System.nanoTime();

        int stepsCount = configuration.getIntegrationStepsCount();

        Thread[] threads = new Thread[threadCount];
        IntegrationThreadCalculator[] calculators = new IntegrationThreadCalculator[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int fromIndex = (stepsCount / threadCount * i) + 1;
            int toIndex = threadCount - 1 == i ?
                stepsCount - 1 :
                stepsCount / threadCount * (i + 1);

            calculators[i] = new IntegrationThreadCalculator(
                configuration,
                fromIndex,
                toIndex
            );

            threads[i] = new Thread(calculators[i]);
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        double parallelIntegrationSum = 0;
        for (int i = 0; i < threadCount; i++) {
            parallelIntegrationSum += calculators[i].getIntegrationSum();
        }
        double parallelIntegrationResult = configuration.getResult(parallelIntegrationSum);

        long end = System.nanoTime();

        System.out.println("Parallel execution time: " + (end - start));
        System.out.println("Parallel execution result: " + parallelIntegrationResult);

        double derivativeResult = configuration.getDerivativeResult();
        checkResult(parallelIntegrationResult, derivativeResult);

        return end - start;
    }

    public static double calculateSerial(
        FunctionConfiguration configuration
    ) {
        System.out.println("\nSerial execution");

        long start = System.nanoTime();

        IntegrationCalculator calculator = new IntegrationCalculator(
            configuration,
            1,
            configuration.getIntegrationStepsCount() - 1
        );
        calculator.execute();

        double serialIntegrationResult = configuration.getResult(
            calculator.getIntegrationSum()
        );

        long end = System.nanoTime();

        System.out.println("Serial execution time: " + (end - start));
        System.out.println("Serial execution result: " + serialIntegrationResult);

        double derivativeResult = configuration.getDerivativeResult();
        checkResult(serialIntegrationResult, derivativeResult);

        return end - start;
    }

    private static final double INTEGRATE_FROM = -1.5;
    private static final double INTEGRATE_TO = 1.5;

    private static final double STEP_OF_INTEGRATION_V1 = 0.001;
    private static final double STEP_OF_INTEGRATION_V2 = 0.0001;

    private static void run(double stepOfIntegration) throws InterruptedException {
        System.out.println("\nIntegration with " + stepOfIntegration + " step");

        FunctionConfiguration configuration = new FunctionConfiguration(
            INTEGRATE_FROM,
            INTEGRATE_TO,
            stepOfIntegration
        );

        double serialTime = calculateSerial(configuration);

        for (int threadCount = 2; threadCount <= 8; threadCount++) {
            double parallelTime = calculateParallel(configuration, threadCount);

            double speedUp = calculateSpeedUp(serialTime, parallelTime);
            System.out.println("Speedup for " + threadCount + " threads: " + speedUp);

            double efficiency = calculateEfficiency(serialTime, parallelTime, threadCount);
            System.out.println("Efficiency for " + threadCount + " threads: " + efficiency);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        run(STEP_OF_INTEGRATION_V1);
        run(STEP_OF_INTEGRATION_V2);
    }
}
