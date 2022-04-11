package util.math.vector;

import rlbotexample.dynamic_objects.car.orientation.Orientation;

import java.io.Serializable;

public class OrientedPosition implements Serializable {
    public Vector3 position;
    public Orientation orientation;

    public OrientedPosition() {
        this.position = new Vector3();
        this.orientation = new Orientation();
    }

    public OrientedPosition(Vector3 position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public ZyxOrientedPosition toZyxOrientedPosition() {
        final Vector3 restOrientation = Vector3.X_VECTOR;
        Vector3 flatFront = new Vector3(orientation.noseVector.flatten(), 0);
        if(flatFront.isZero()) {
            flatFront = restOrientation;
        }
        final Vector3 rotatorZ = flatFront.findRotator(restOrientation);
        final double angleZ = rotatorZ.dotProduct(Vector3.Z_VECTOR);

        final Orientation thisOrientationRotatedInZ = orientation.rotate(rotatorZ);
        final Vector3 rotatorY = thisOrientationRotatedInZ.noseVector.findRotator(restOrientation);
        final double angleY = rotatorY.dotProduct(Vector3.Y_VECTOR);

        final Orientation thisOrientationRotatedInZy = thisOrientationRotatedInZ.rotate(rotatorY);
        final Vector3 rotatorX = thisOrientationRotatedInZy.roofVector.findRotator(Vector3.Z_VECTOR);
        final double angleX = rotatorX.dotProduct(Vector3.X_VECTOR);

        return new ZyxOrientedPosition(position, new Vector3(-angleX, -angleY, -angleZ));
    }

    // this function assumes that the origin of this object is zeroed, and outputs the same object, but with the specified origin in parameters:
    // position of origin = (0, 0, 0),
    // orientation = ((1, 0, 0), (0, 0, 1))
    /*
    public OrientedPosition toGlobalPosition(OrientedPosition globalOrigin) {
        ZyxOrientedPosition zyxGlobalOrigin = globalOrigin.toZyxOrientedPosition();
        Vector3 rotatorZPosition = Vector3.UP_VECTOR.scaled(zyxGlobalOrigin.eulerZYX.z);
        Vector3 rotatorYPosition = Vector3.Y_VECTOR.rotate(rotatorZPosition).scaled(zyxGlobalOrigin.eulerZYX.y);
        Vector3 rotatorXPosition = Vector3.X_VECTOR.rotate(rotatorZPosition).rotate(rotatorYPosition).scaled(zyxGlobalOrigin.eulerZYX.x);

        Vector3 rotatorZOrientation = Vector3.UP_VECTOR.scaled(zyxGlobalOrigin.eulerZYX.z);
        Vector3 rotatorYOrientation = Vector3.Y_VECTOR.rotate(rotatorZPosition).scaled(zyxGlobalOrigin.eulerZYX.y);
        Vector3 rotatorXOrientation = Vector3.X_VECTOR.rotate(rotatorZPosition).rotate(rotatorYPosition).scaled(zyxGlobalOrigin.eulerZYX.x);

        Vector3 rotatedTranslatedPosition = position
                .rotate(rotatorZPosition)
                .rotate(rotatorYPosition)
                .rotate(rotatorXPosition)
                .plus(globalOrigin.position);
        Orientation rotatedOrientation = orientation
                .rotate(rotatorZOrientation)
                .rotate(rotatorYOrientation)
                .rotate(rotatorXOrientation);

        return new OrientedPosition(rotatedTranslatedPosition, rotatedOrientation);
    }*/
    public OrientedPosition toGlobalPosition(final OrientedPosition globalOrigin) {
        final Vector3 angularDisplacement = globalOrigin.orientation.asAngularDisplacement();

        final Vector3 rotatedTranslatedPosition = position.rotate(angularDisplacement).plus(globalOrigin.position);
        final Orientation rotatedOrientation = orientation.rotate(angularDisplacement);

        return new OrientedPosition(rotatedTranslatedPosition, rotatedOrientation);
    }
}
