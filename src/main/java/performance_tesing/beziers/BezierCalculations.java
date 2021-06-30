package performance_tesing.beziers;

public class BezierCalculations {

    /*

    // Deprecated code

    public static void main(String[] args) {
        executionTimeComparision();
    }

    static void singleRandomComparision() {
        int randomNumberOfControlPoints = 3;
        List<Vector3> controlPoints = new ArrayList<>();

        for(int i = 0; i < randomNumberOfControlPoints; i++) {
            controlPoints.add(new Vector3((Math.random()-0.5)*10000, (Math.random()-0.5)*10000, (Math.random()-0.5)*10000));
        }

        BezierCurve bezierCurve = new QuadraticBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2));
        //BezierCurve bezierCurve = new CubicBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2), controlPoints.get(3));

        System.out.println("Comparing algorithms that compute the length of bezier curves.\n");
        System.out.println(bezierCurve);
        System.out.println("Here's the resulting comparision:");
        System.out.println("Length sampling algorithm (10 samples): " + bezierCurve.curveLength(10) + " uu");
        System.out.println("Gaussian method: " + bezierCurve.gaussianQuadratureCurveLength() + " uu");
    }

    static void previousWayVsGaussianComparision() {
        int randomNumberOfControlPoints = 3;
        List<Vector3> controlPoints = new ArrayList<>();
        double averageErrorPreviousSampling = 0;
        double averageErrorGaussian = 0;
        int numberOfTests = 10000;

        System.out.println("Comparing the previous algorithm with gaussian.");
        System.out.println("Note:");
        System.out.println("Length sampling reference algorithm does 10'000 samples on curves\nas a reference for the Gaussian and the previous sampling algorithm.\n");
        System.out.println("Every tested bezier curves are of degree " + (randomNumberOfControlPoints-1) + ".");

        System.out.println("Starting calculations...");

        for(int i = 0; i < numberOfTests; i++) {
            for (int j = 0; j < randomNumberOfControlPoints; j++) {
                controlPoints.add(new Vector3((Math.random() - 0.5) * 10000, (Math.random() - 0.5) * 10000, (Math.random() - 0.5) * 10000));
            }

            BezierCurve bezierCurve = new QuadraticBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2));
            //BezierCurve bezierCurve = new CubicBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2), controlPoints.get(3));
            double closeEnoughValue = bezierCurve.curveLength(numberOfTests);
            averageErrorPreviousSampling += bezierCurve.curveLength(10)/closeEnoughValue;
            averageErrorGaussian += bezierCurve.gaussianQuadratureCurveLength()/closeEnoughValue;

            if(i%(numberOfTests/10) == 0) {
                System.out.println((int)((100*i)/(double)numberOfTests)+"%...");
            }

        }
        System.out.println("Finished.");
        System.out.println("Gaussian ratio: " + averageErrorGaussian/numberOfTests);
        System.out.println("Previous sampling ratio: " + averageErrorPreviousSampling/numberOfTests);
    }

    static void executionTimeComparision() {
        int randomNumberOfControlPoints = 3;
        List<Vector3> controlPoints = new ArrayList<>();
        double averagePreviousSamplingTime = 0;
        double averageGaussianTime = 0;
        int numberOfTests = 10000000;

        System.out.println("Comparing the previous algorithm execution time with gaussian execution time.");
        System.out.println("Every tested bezier curves are of degree " + (randomNumberOfControlPoints-1) + ".");
        System.out.println("Doing " + numberOfTests + " tests.");

        System.out.println("Starting calculations...");

        for(int i = 0; i < numberOfTests; i++) {
            for (int j = 0; j < randomNumberOfControlPoints; j++) {
                controlPoints.add(new Vector3((Math.random() - 0.5) * 10000, (Math.random() - 0.5) * 10000, (Math.random() - 0.5) * 10000));
            }

            BezierCurve bezierCurve = new QuadraticBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2));
            //BezierCurve bezierCurve = new CubicBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2), controlPoints.get(3));
            Clock clock = new Clock();

            clock.start();
            bezierCurve.curveLength(10);
            clock.stop();
            averagePreviousSamplingTime += clock.getElapsedSeconds();

            clock.start();
            bezierCurve.gaussianQuadratureCurveLength();
            clock.stop();
            averageGaussianTime += clock.getElapsedSeconds();

            if(i%(numberOfTests/10) == 0) {
                System.out.println((int)((100*i)/(double)numberOfTests)+"%...");
            }

        }
        System.out.println("Finished.");
        System.out.println("Average Gaussian method time: " + averageGaussianTime/numberOfTests);
        System.out.println("Average Previous sapling method time: " + averagePreviousSamplingTime/numberOfTests);
        System.out.println("Average Gaussian method is " + averagePreviousSamplingTime/averageGaussianTime + " times faster.");
        System.out.println("Average Previous sapling method is " + averageGaussianTime/averagePreviousSamplingTime + " times faster.");
    }

    static void executionGaussianImplementationTimeComparision() {
        int randomNumberOfControlPoints = 3;
        List<Vector3> controlPoints = new ArrayList<>();
        double averagePreviousSamplingTime = 0;
        double averageGaussianTime = 0;
        int numberOfTests = 1000000;

        System.out.println("Comparing the lambda implementation execution time with hardcode execution time.");
        System.out.println("Every tested bezier curves are of degree " + (randomNumberOfControlPoints-1) + ".");
        System.out.println("Doing " + numberOfTests + " tests.");

        System.out.println("Starting calculations...");

        for(int i = 0; i < numberOfTests; i++) {
            for (int j = 0; j < randomNumberOfControlPoints; j++) {
                controlPoints.add(new Vector3((Math.random() - 0.5) * 10000, (Math.random() - 0.5) * 10000, (Math.random() - 0.5) * 10000));
            }

            BezierCurve bezierCurve = new QuadraticBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2));
            //BezierCurve bezierCurve = new CubicBezier(controlPoints.get(0), controlPoints.get(1), controlPoints.get(2), controlPoints.get(3));
            Clock clock = new Clock();

            clock.start();
            bezierCurve.curveLength(10);
            clock.stop();
            averageGaussianTime += clock.getElapsedSeconds();

            clock.start();
            bezierCurve.gaussianQuadratureCurveLength();
            clock.stop();
            averagePreviousSamplingTime += clock.getElapsedSeconds();

            if(i%(numberOfTests/10) == 0) {
                System.out.println((int)((100*i)/(double)numberOfTests)+"%...");
            }

        }
        System.out.println("Finished.");
        System.out.println("Average Gaussian method time: " + averageGaussianTime/numberOfTests);
        System.out.println("Average Previous sapling method time: " + averagePreviousSamplingTime/numberOfTests);
        System.out.println("Average Previous sapling method is " + averageGaussianTime/averagePreviousSamplingTime + " times faster.");
    }

    */
}
