package util.math;

public class NewtonianApproximation {

    private static final double h = 0.00001;
    private static final double ONE_OVER_h = 1 / h;
    private static final double PRECISION = 1000;

    public static double process(double startingXValue) {
        double x = startingXValue;

        for(int i = 0; i < PRECISION; i++) {
            double y = f(x);
            double a = derivativeAt(x);
            double b = y - (a*x);
            double newX = -b/a;
            x = newX;
        }

        return x;
    }

    private static double derivativeAt(double x) {
        //         f(x+h) - f(x)
        // f'(x) = -------------
        //               h

        return (f(x + h) - f(x))
                    * ONE_OVER_h;
    }

    private static double f(double x) {
        // just get rid of that and put your function here or replace it in the code or do a lambda or etc.
        return x;
    }
}
