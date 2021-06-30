package util.math.linear_transform;

import util.math.vector.Vector2;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LinearApproximator {

    public List<Vector2> functionSamples;

    public LinearApproximator() {
        functionSamples = new ArrayList<>();
    }

    public LinearApproximator(Function<Double, Double> function, double min, double max, int amountOfSamples) {
        functionSamples = new ArrayList<>();
        double rangeSize = java.lang.Math.abs(max-min);
        for(int i = 0; i < amountOfSamples; i++) {
            double x = min + (rangeSize*i/amountOfSamples);
            sample(new Vector2(x, function.apply(x)));
        }
    }

    public void sample(Vector2 sampledPoint) {
        for(int i = 0; i < functionSamples.size(); i++) {
            if(functionSamples.get(i).x > sampledPoint.x) {
                functionSamples.add(i, sampledPoint);
                return;
            }
        }
        functionSamples.add(sampledPoint);
    }

    // functional approximation
    public double compute(double x) {
        Vector2 closestPoint = new Vector2();
        double closestDistance = Double.MAX_VALUE;

        for(Vector2 element: functionSamples) {
            if(java.lang.Math.abs(element.x - x) < closestDistance) {
                closestPoint = element;
                closestDistance = java.lang.Math.abs(element.x - x);
            }
        }

        Vector2 secondClosestPoint;
        int indexOfClosestPoint = functionSamples.indexOf(closestPoint);
        // if the point found is to the right of x
        if(closestPoint.x - x > 0) {
            if(indexOfClosestPoint == 0) {
                secondClosestPoint = functionSamples.get(indexOfClosestPoint+1);
            }
            else {
                secondClosestPoint = functionSamples.get(indexOfClosestPoint-1);
            }
        }
        // if the point found is to the left of x
        else {
            if(indexOfClosestPoint == functionSamples.size()-1) {
                secondClosestPoint = functionSamples.get(indexOfClosestPoint-1);
            }
            else {
                secondClosestPoint = functionSamples.get(indexOfClosestPoint+1);
            }
        }

        double t = (x - secondClosestPoint.x) / (closestPoint.x - secondClosestPoint.x);
        return closestPoint.y*t + secondClosestPoint.y*(1-t);
    }

    // inverse approximation
    public double inverse(double y) {
        Vector2 pointThatCrossedTheLine = null;
        double closestDistance = Double.MAX_VALUE;

        // if the first point is smaller in y,
        // we find the first bigger value
        if(functionSamples.get(0).y <= y) {
            for(Vector2 element: functionSamples) {
                if(element.y > y) {
                    pointThatCrossedTheLine = element;
                    break;
                }
            }
        }
        // if the first point is bigger in y
        // we find the first lower value
        else {
            for(Vector2 element: functionSamples) {
                if(element.y < y) {
                    pointThatCrossedTheLine = element;
                    break;
                }
            }
        }

        // If this is still equal to null,
        // then the value of x that corresponds
        // to the queried y is undefined.
        // In out case, we still want a value,
        // so we take the closest point and
        // return its x coordinate
        if(pointThatCrossedTheLine == null) {
            Vector2 closestPoint = functionSamples.get(0);

            for(Vector2 element: functionSamples) {
                if(Math.abs(closestPoint.y - y) > Math.abs(element.y - y)) {
                    closestPoint = element;
                }
            }

            // we don't need to find the slope that fits 2 points,
            // because we know the value we are looking for lands
            // exactly on the point we found.
            return closestPoint.x;
        }

        // we don't need to check for out of bound because:
        // 1) we don't care about the upper bound (functionSamples.indexOf(pointThatCrossedTheLine) << functionSamples.size())
        // 2) the lower bound is strictly bigger than 0 because we
        // crossed the y value at least once (functionSamples.indexOf(pointThatCrossedTheLine) >> 0)
        int indexOfPrecedingPoint = functionSamples.indexOf(pointThatCrossedTheLine)-1;
        Vector2 pointBeforeCrossingTheLine = functionSamples.get(indexOfPrecedingPoint);

        double t = (y - pointThatCrossedTheLine.y) / (pointBeforeCrossingTheLine.y - pointThatCrossedTheLine.y);
        return pointBeforeCrossingTheLine.x*t + pointThatCrossedTheLine.x*(1-t);
    }

}
