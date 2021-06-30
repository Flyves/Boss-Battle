package rlbotexample.input.prediction;

import rlbotexample.input.prediction.ball.RawBallTrajectory;
import util.math.vector.MovingPoint;
import util.math.vector.Ray3;
import util.math.vector.Vector3;

import java.util.function.Function;

@FunctionalInterface
public interface Trajectory3D extends Function<Double, Vector3> {

    default Vector3 derivative(double time) {
        double h = 2.0/ RawBallTrajectory.PREDICTION_REFRESH_RATE;
        Vector3 position1 = apply(time - h);
        Vector3 position2 = apply(time);
        if(position1 == null || position2 == null) {
            return null;
        }
        Vector3 result = position2.minus(position1);
        result = result.scaled(1 / h);

        return result;
    }

    static double findTimeOfClosestApproach(Trajectory3D trajectory1, Trajectory3D trajectory2, double duration, double resolution) {
        double bestTime = 0;
        Vector3 initialPosition1 = trajectory1.apply(0.0);
        Vector3 initialPosition2 = trajectory2.apply(0.0);
        double bestDistanceBetweenTrajectories;
        if(initialPosition1 == null || initialPosition2 == null) {
            bestDistanceBetweenTrajectories = Double.MAX_VALUE;
        }
        else {
            bestDistanceBetweenTrajectories = initialPosition1.minus(trajectory2.apply(0.0)).magnitude();
        }

        for(int i = 1; i < duration*resolution; i++) {
            double testedTime = i/resolution;
            Vector3 position1 = trajectory1.apply(testedTime);
            Vector3 position2 = trajectory2.apply(testedTime);
            if(position1 == null || position2 == null) {
                continue;
            }
            double distanceBetweenTrajectories = position1.minus(position2).magnitude();
            if(distanceBetweenTrajectories < bestDistanceBetweenTrajectories) {
                bestTime = testedTime;
                bestDistanceBetweenTrajectories = distanceBetweenTrajectories;
            }
        }

        return bestTime;
    }

    /** finds the first non-null element
     *  worst case is O(duration * dt)
     * */
    default MovingPoint first(final double duration, final double precision) {
        MovingPoint movingPoint = null;
        Vector3 tempPoint;
        double tempTime;

        for(int i = 0; i < duration/precision; i++) {
            tempTime = i*precision;
            tempPoint = apply(tempTime);

            if(tempPoint != null && derivative(tempTime) != null) {
                movingPoint = new MovingPoint(new Ray3(tempPoint, derivative(tempTime)), tempTime);
                break;
            }
        }

        return movingPoint;
    }

    /** lazy removal of some parts of the trajectory */
    default Trajectory3D remove(Function<MovingPoint, Boolean> isRemoved) {
        return time -> {
            Vector3 position = apply(time);
            if(position == null) {
                return null;
            }
            Vector3 velocity = derivative(time);
            if(velocity == null) {
                return null;
            }
            MovingPoint movingPosition = new MovingPoint(new Ray3(position, velocity), time);
            return isRemoved.apply(movingPosition) ? null : movingPosition.physicsState.offset;
        };
    }

    /** lazy keeping of some parts of the trajectory */
    default Trajectory3D keep(Function<MovingPoint, Boolean> isKept) {
        return time -> {
            Vector3 position = apply(time);
            if(position == null) {
                return null;
            }
            Vector3 velocity = derivative(time);
            if(velocity == null) {
                return null;
            }
            MovingPoint movingPosition = new MovingPoint(new Ray3(position, velocity), time);
            return isKept.apply(movingPosition) ? movingPosition.physicsState.offset : null;
        };
    }

    /** lazy modification of the trajectory */
    default Trajectory3D modify(Function<MovingPoint, Vector3> movingPointFunction) {
        return time -> {
            Vector3 position = apply(time);
            if(position == null) {
                return null;
            }
            Vector3 velocity = derivative(time);
            if(velocity == null) {
                return null;
            }
            MovingPoint movingPosition = new MovingPoint(new Ray3(position, velocity), time);
            return movingPointFunction.apply(movingPosition);
        };
    }

    /** iterate on every valid element */
    default void forEach(Function<MovingPoint, Void> iterationFunction, double duration, double precision) {
        Vector3 tempPoint;
        Vector3 tempDerivative;
        double tempTime;

        for(int i = 0; i < duration/precision; i++) {
            tempTime = i*precision;
            tempPoint = apply(tempTime);
            if(tempPoint == null) {
                continue;
            }
            tempDerivative = derivative(tempTime);

            if(tempDerivative != null) {
                iterationFunction.apply(new MovingPoint(new Ray3(tempPoint, tempDerivative), tempTime));
            }
        }
    }
}