package rlbotexample.input.dynamic_data.goal;

import util.game_constants.RlConstants;
import util.math.vector.Ray3;
import util.math.vector.Vector2;
import util.math.vector.Vector3;
import util.shapes.Plane3D;

import java.util.Optional;

// this class is an oversimplification of real goals because we
// assume that the goals are actually planes in the XZ direction.
public class GoalRegion {

    public final Vector3 upperLeftPost;
    public final Vector3 lowerRightPost;
    public final Vector3 upperLeft;
    public final Vector3 lowerRight;
    public final Ray3 normal;
    public final Plane3D normalPlane;

    public GoalRegion(Vector3 upperLeft, Vector3 lowerRight) {
        this.upperLeftPost = upperLeft;
        this.lowerRightPost = lowerRight;

        // chopping a radius length from the side
        Vector3 localUpperLeft = upperLeftPost.minus(lowerRightPost);
        double newLocalUpperLeftLengthX = Math.abs(localUpperLeft.x) - RlConstants.BALL_RADIUS;
        double newLocalUpperLeftLengthZ = Math.abs(localUpperLeft.z) - RlConstants.BALL_RADIUS;
        upperLeft = lowerRightPost.plus(localUpperLeft.scaledToMagnitude(newLocalUpperLeftLengthX, localUpperLeft.y, newLocalUpperLeftLengthZ));
        // adding depth in y
        upperLeft = upperLeft.plus(new Vector3(0, upperLeft.y > 0 ? RlConstants.BALL_RADIUS:-RlConstants.BALL_RADIUS, 0));

        // chopping a radius length from the side
        Vector3 localLowerRight = lowerRightPost.minus(upperLeftPost);
        double newLocalLowerRightLengthX = Math.abs(localLowerRight.x) - RlConstants.BALL_RADIUS;
        double newLocalLowerRightLengthZ = Math.abs(localLowerRight.z) - RlConstants.BALL_RADIUS;
        lowerRight = upperLeftPost.plus(localLowerRight.scaledToMagnitude(newLocalLowerRightLengthX, localLowerRight.y, newLocalLowerRightLengthZ));
        // adding depth in y
        lowerRight = lowerRight.plus(new Vector3(0, upperLeft.y > 0 ? RlConstants.BALL_RADIUS:-RlConstants.BALL_RADIUS, 0));

        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.normal = new Ray3(
                upperLeft.plus(lowerRight).scaled(0.5),
                lowerRight.minus(upperLeft).normalized()
                        .crossProduct(Vector3.UP_VECTOR));
        this.normalPlane = new Plane3D(this.normal);
    }

    public Optional<Vector3> closestPointOfBallOnSurface(Vector3 globalPoint) {
        Vector3 globalPointCopy = new Vector3(globalPoint);
        globalPoint = globalPoint.minus(globalPoint.projectOnto(normal.direction))
                .plus(normal.offset.projectOnto(normal.direction));

        // if goal is orange
        if(lowerRight.x > upperLeft.x) {
            if(globalPoint.x > lowerRight.x) {
                double angle = Math.sin(
                        RlConstants.BALL_RADIUS / lowerRightPost.flatten().minus(globalPointCopy.flatten())
                                .magnitude());
                Vector3 rotator = Vector3.UP_VECTOR.scaled(angle);
                Vector3 localCorrectlyOrientedVector = new Vector3(lowerRightPost.flatten().minus(globalPointCopy.flatten()), 0)
                        .rotate(rotator);
                globalPoint = globalPointCopy.plus(localCorrectlyOrientedVector.scaled((globalPointCopy.y - lowerRight.y)/(-localCorrectlyOrientedVector.y)));
                if(globalPoint.x < upperLeft.x) {
                    return Optional.empty();
                }
            }
            else if(globalPoint.x < upperLeft.x) {
                double angle = Math.sin(
                        RlConstants.BALL_RADIUS / upperLeftPost.flatten().minus(globalPointCopy.flatten())
                                .magnitude());
                Vector3 rotator = Vector3.DOWN_VECTOR.scaled(angle);
                Vector3 localCorrectlyOrientedVector = new Vector3(upperLeftPost.flatten().minus(globalPointCopy.flatten()), 0)
                        .rotate(rotator);
                globalPoint = globalPointCopy.plus(localCorrectlyOrientedVector.scaled((globalPointCopy.y - upperLeft.y)/(-localCorrectlyOrientedVector.y)));
                if(globalPoint.x > lowerRight.x) {
                    return Optional.empty();
                }
            }
        }
        // if goal is blue
        else {
            if(globalPoint.x > upperLeft.x) {
                double angle = Math.sin(
                        RlConstants.BALL_RADIUS / upperLeftPost.flatten().minus(globalPointCopy.flatten())
                                .magnitude());
                Vector3 rotator = Vector3.DOWN_VECTOR.scaled(angle);
                Vector3 localCorrectlyOrientedVector = new Vector3(upperLeftPost.flatten().minus(globalPointCopy.flatten()), 0)
                        .rotate(rotator);
                globalPoint = globalPointCopy.plus(localCorrectlyOrientedVector.scaled((globalPointCopy.y - upperLeft.y)/(-localCorrectlyOrientedVector.y)));
                if(globalPoint.x < lowerRight.x) {
                    return Optional.empty();
                }
            }
            else if(globalPoint.x < lowerRight.x) {
                double angle = Math.sin(
                        RlConstants.BALL_RADIUS / lowerRightPost.flatten().minus(globalPointCopy.flatten())
                                .magnitude());
                Vector3 rotator = Vector3.UP_VECTOR.scaled(angle);
                Vector3 localCorrectlyOrientedVector = new Vector3(lowerRightPost.flatten().minus(globalPointCopy.flatten()), 0)
                        .rotate(rotator);
                globalPoint = globalPointCopy.plus(localCorrectlyOrientedVector.scaled((globalPointCopy.y - lowerRight.y)/(-localCorrectlyOrientedVector.y)));
                if(globalPoint.x > upperLeft.x) {
                    return Optional.empty();
                }
            }
        }


        if(globalPoint.z > upperLeft.z) {
            double angle = Math.sin(
                    RlConstants.BALL_RADIUS / new Vector2(upperLeftPost.z, upperLeftPost.y).minus(new Vector2(globalPointCopy.z, globalPointCopy.y))
                            .magnitude());
            Vector3 rotator;
            if(upperLeft.y > 0) {
                rotator = Vector3.X_VECTOR.scaled(-angle);
            }
            else {
                rotator = Vector3.X_VECTOR.scaled(angle);
            }
            Vector3 localCorrectlyOrientedVector = new Vector3(0, upperLeftPost.y - globalPointCopy.y, upperLeftPost.z - globalPointCopy.z)
                    .rotate(rotator);
            System.out.println(localCorrectlyOrientedVector.magnitude());
            globalPoint = globalPoint.plus(new Vector3(0, 0, localCorrectlyOrientedVector.scaled((globalPointCopy.y - upperLeft.y)/(-localCorrectlyOrientedVector.y)).z));
        }
        // no if(globalPoint.z < upperLeft.z) {} because the ball never goes under the net lol

        return Optional.of(globalPoint);
    }
}
