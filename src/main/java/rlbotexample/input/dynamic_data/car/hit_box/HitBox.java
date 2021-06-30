package rlbotexample.input.dynamic_data.car.hit_box;

import rlbot.flat.BoxShape;
import rlbotexample.input.dynamic_data.car.orientation.Orientation;
import util.math.vector.Vector3;

public class HitBox {
    public final Vector3 centerPositionOfHitBox;
    public final Vector3 localHitBoxOffset;
    public final Vector3 cornerPosition;
    public final Vector3 frontOrientation;
    public final Vector3 roofOrientation;

    public HitBox(Vector3 centerPositionOfCar, rlbot.flat.Vector3 centerOfMassOffset, BoxShape boxShape, Vector3 frontOrientation, Vector3 roofOrientation) {
        this.centerPositionOfHitBox = centerPositionOfCar.plus(new Vector3(centerOfMassOffset).scaled(-1, 1, 1).matrixRotation(frontOrientation, roofOrientation));
        this.localHitBoxOffset = new Vector3(centerOfMassOffset).scaled(-1, 1, 1);
        this.cornerPosition = new Vector3(boxShape.length(), boxShape.width(), boxShape.height()).scaled(0.5);
        this.frontOrientation = frontOrientation;
        this.roofOrientation = roofOrientation;
    }

    public HitBox(Vector3 centerPosition, Vector3 boxSize) {
        this.centerPositionOfHitBox = centerPosition;
        // this is HORRIBLY WRONG
        // but this constructor is only used for prediction of car trajectories.
        // when this constructor is called, the center position of the car is never used.
        this.localHitBoxOffset = null;
        this.cornerPosition = boxSize;
        this.frontOrientation = new Vector3(1, 0, 0);
        this.roofOrientation = new Vector3(0, 0, 1);
    }

    private HitBox(Vector3 centerPosition, Vector3 boxSize, Orientation orientation) {
        this.centerPositionOfHitBox = centerPosition;
        // this one is simply wrong LOL
        // we need to infer the hit box offset
        // with static variables that we set at
        // the start of the program...
        this.localHitBoxOffset = null;
        this.cornerPosition = boxSize;
        this.frontOrientation = orientation.getNose();
        this.roofOrientation = orientation.getRoof();
    }

    public HitBox generateHypotheticalHitBox(Vector3 hypotheticalPosition, Orientation hypotheticalOrientation) {
        return new HitBox(hypotheticalPosition, cornerPosition, hypotheticalOrientation);
    }

    public HitBox generateHypotheticalHitBox(Vector3 hypotheticalPosition) {
        return new HitBox(hypotheticalPosition, cornerPosition, new Orientation(frontOrientation, roofOrientation));
    }

    // doesn't work if the point is withing the hit box, but we hardly need that
    public Vector3 closestPointOnSurface(Vector3 globalPoint) {
        Vector3 localPoint = getLocal(globalPoint);

        double newXCoordinate = localPoint.x;
        if(localPoint.x > cornerPosition.x) {
            newXCoordinate = cornerPosition.x;
        }
        else if(localPoint.x < -cornerPosition.x) {
            newXCoordinate = -cornerPosition.x;
        }

        double newYCoordinate = -localPoint.y;
        if(localPoint.y > cornerPosition.y) {
            newYCoordinate = -cornerPosition.y;
        }
        else if(localPoint.y < -cornerPosition.y) {
            newYCoordinate = cornerPosition.y;
        }

        double newZCoordinate = localPoint.z;
        if(localPoint.z > cornerPosition.z) {
            newZCoordinate = cornerPosition.z;
        }
        else if(localPoint.z < -cornerPosition.z) {
            newZCoordinate = -cornerPosition.z;
        }

        return getGlobal(new Vector3(newXCoordinate, newYCoordinate, newZCoordinate));
    }

    // z under the car might be badly behaving D: (too much z, maybe because the center of mass is outside the hit box??)
    public Vector3 projectPointOnSurfaceFromCenterOfMass(Vector3 pointToProject) {
        final Vector3 localPoint = getLocal(pointToProject);
        final Vector3 absoluteNonZeroLocalPoint = new Vector3(
                Math.abs(makeNonZero(localPoint.x)),
                Math.abs(makeNonZero(localPoint.y)),
                Math.abs(makeNonZero(localPoint.z)));
        final Vector3 localHitBoxOffset = new Vector3(
                localPoint.x<0 ? -this.localHitBoxOffset.x : this.localHitBoxOffset.x,
                this.localHitBoxOffset.y,
                localPoint.z<0 ? -this.localHitBoxOffset.z : this.localHitBoxOffset.z);
        final Vector3 cornerPosition = new Vector3(
                this.cornerPosition.x+localHitBoxOffset.x,
                this.cornerPosition.y+localHitBoxOffset.y,
                this.cornerPosition.z+localHitBoxOffset.z);

        // find the correct side to project onto, and find the right scalar
        double scalar = 1;
        if(absoluteNonZeroLocalPoint.x/absoluteNonZeroLocalPoint.y > Math.abs(cornerPosition.x/cornerPosition.y)
                && absoluteNonZeroLocalPoint.x/absoluteNonZeroLocalPoint.z > Math.abs(cornerPosition.x/cornerPosition.z)) {
            scalar = cornerPosition.x/absoluteNonZeroLocalPoint.x;
        }
        else if(absoluteNonZeroLocalPoint.y/absoluteNonZeroLocalPoint.x > Math.abs(cornerPosition.y/cornerPosition.x)
                && absoluteNonZeroLocalPoint.y/absoluteNonZeroLocalPoint.z > Math.abs(cornerPosition.y/cornerPosition.z)) {
            scalar = cornerPosition.y/absoluteNonZeroLocalPoint.y;
        }
        else if(absoluteNonZeroLocalPoint.z/absoluteNonZeroLocalPoint.x > Math.abs(cornerPosition.z/cornerPosition.x)
                && absoluteNonZeroLocalPoint.z/absoluteNonZeroLocalPoint.y > Math.abs(cornerPosition.z/cornerPosition.y)) {
            scalar = cornerPosition.z/absoluteNonZeroLocalPoint.z;
        }

        return getGlobal(localPoint
                .plus(this.localHitBoxOffset)
                .scaled(scalar)
                .scaled(1, -1, 1)
                .minus(this.localHitBoxOffset));
    }

    private double makeNonZero(double value) {
        if(Math.abs(value) < 0.00001) {
            return 0.00001;
        }
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HitBox)) {
            return false;
        }
        return ((HitBox)obj).centerPositionOfHitBox.minus(this.centerPositionOfHitBox).magnitudeSquared() < 0.1
        && ((HitBox)obj).cornerPosition.minus(this.cornerPosition).magnitudeSquared() < 0.1
        && ((HitBox)obj).frontOrientation.minus(this.frontOrientation).magnitudeSquared() < 0.1
        && ((HitBox)obj).roofOrientation.minus(this.roofOrientation).magnitudeSquared() < 0.1;
    }

    @Override
    public int hashCode() {
        return centerPositionOfHitBox.hashCode()
                + cornerPosition.hashCode()
                + frontOrientation.hashCode()
                + roofOrientation.hashCode();
    }

    private Vector3 getLocal(Vector3 globalPoint) {
        return globalPoint.minus(centerPositionOfHitBox).toFrameOfReference(frontOrientation, roofOrientation);
    }

    private Vector3 getGlobal(Vector3 localPoint) {
        return localPoint.matrixRotation(frontOrientation, roofOrientation).plus(centerPositionOfHitBox);
    }
}
