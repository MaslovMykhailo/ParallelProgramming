package lab6;

import mpi.MPI;

import java.util.function.UnaryOperator;

/*
    Variant 18
    Integrate function x*sin(x) from -1.5 to 1.5
    using trapezoidal rule
*/

/*
    Formula of integration by trapezoidal rule
    ∫ f(x)dx from a to b = (h / 2) * (f(x0) + 2 * (∑ f(xi) from i = 1 to i = n - 1) + f(xn))
    where
    h = (b - a) / n
    xi = a + i * h
 */

public class Main {

    static class Fx {

        private static final double stepOfIntegration = 0.0001;

        public static UnaryOperator<Double> fx = (x) -> x * Math.sin(x); // f(x) in formula

        private static final double integrateFrom = -1.5; // a in formula
        private static final double integrateTo = 1.5; // b in formula

        public static final int integrationStepsCount = calcIntegrationStepsCount(); // n in formula
        public static final double trapezoidHeight = calcTrapezoidHeight(); // h in formula

        private static int calcIntegrationStepsCount() {
            return (int) ((integrateTo - integrateFrom) / stepOfIntegration);
        }

        private static double calcTrapezoidHeight() {
            return (integrateTo - integrateFrom) / integrationStepsCount;
        }

        public static double calcXi(int i) {
            return integrateFrom + i * trapezoidHeight;
        }

        public static double calcFx(double integrationSum) {
            double fX0 = fx.apply(calcXi(0));
            double fXn = fx.apply(calcXi(integrationStepsCount));

            return (trapezoidHeight / 2) * (fX0 + 2 * integrationSum + fXn);
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);

        int root = 0;
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int fromIndex = (Fx.integrationStepsCount / size * rank) + 1;
        int toIndex = rank == size - 1 ? Fx.integrationStepsCount - 1 : Fx.integrationStepsCount / size * (rank + 1);

        double[] localIntegrationSum = {0};
        for (int i = fromIndex; i < toIndex; i++) {
            localIntegrationSum[0] += Fx.fx.apply(Fx.calcXi(i));
        }

        double[] globalIntegrationSum = {0};
        MPI.COMM_WORLD.Reduce(
            localIntegrationSum,
            0,
            globalIntegrationSum,
            0,
            1,
            MPI.DOUBLE,
            MPI.SUM,
            root
        );

        if (rank == root) {
            double result = Fx.calcFx(globalIntegrationSum[0]);
            System.out.println("Result: " + result);
        }

        MPI.Finalize();
    }

}
