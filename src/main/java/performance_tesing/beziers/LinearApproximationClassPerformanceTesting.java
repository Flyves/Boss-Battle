package performance_tesing.beziers;

import util.math.linear_transform.LinearApproximator;
import util.math.vector.Vector2;

public class LinearApproximationClassPerformanceTesting {

    public static void main(String[] args) {

        LinearApproximator approximation = new LinearApproximator();

        approximation.sample(new Vector2(-200, -500));
        approximation.sample(new Vector2(-50, 1));
        approximation.sample(new Vector2(40, 2));
        approximation.sample(new Vector2(800, -800));

        System.out.println(approximation.inverse(-800));
        /*
        // sampling the function
        int amountOfPoints = 20000;
        double precision = 0.001;
        for(int i = 0; i < amountOfPoints; i++) {
            double x = precision*i;
            double y = x*x;
            approximation.sample(new Vector2(x, y));
        }

        // testing the function
        double y = approximation.inverse(100);
        System.out.println(y);*/
    }
}
