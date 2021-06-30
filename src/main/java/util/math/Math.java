package util.math;

public class Math {
    public static double dilogarithm(double x) {
        final double x2 = x*x;
        final double x4 = x2*x2;
        return (x)
                + (x2/4)
                + (x2*x/9)
                + (x4/16)
                + (x4*x/25)
                + (x4*x2/36)
                + (x4*x2*x/49)
                + (x4*x4/64)
                + (x4*x4*x/81)
                + (x4*x4*x2/100);
    }
}
