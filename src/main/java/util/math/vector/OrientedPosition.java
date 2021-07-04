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
        Vector3 restOrientation = new Vector3(1, 0, 0);
        Vector3 flatFront = new Vector3(orientation.noseVector.flatten(), 0);
        Vector3 rotatorZ = restOrientation.findRotator(flatFront);
        double angleZ = rotatorZ.dotProduct(Vector3.UP_VECTOR);

        Orientation restOrientationRotatedInZ = new Orientation().rotate(rotatorZ);
        Vector3 rotatorY = restOrientationRotatedInZ.noseVector.findRotator(orientation.noseVector);
        double angleY = rotatorY.dotProduct(Vector3.Y_VECTOR.rotate(rotatorZ));

        Orientation restOrientationRotatedInZy = restOrientationRotatedInZ.rotate(rotatorY);
        Vector3 rotatorX = restOrientationRotatedInZy.roofVector.findRotator(orientation.roofVector);
        double angleX = rotatorX.dotProduct(Vector3.X_VECTOR.rotate(rotatorZ).rotate(rotatorY));
        // ugly fix but it seems to work now!
        if(orientation.roofVector.z < 0 && Math.abs(angleX) < 0.00000001) {
            angleX = Math.PI;
        }

        return new ZyxOrientedPosition(position, new Vector3(angleX, angleY, angleZ));
    }

    // this function assumes that the origin of this object is zeroed, and outputs the same object, but with the specified origin in parameters:
    // position of origin = (0, 0, 0),
    // orientation = ((1, 0, 0), (0, 0, 1))
    public OrientedPosition toGlobalPosition(OrientedPosition globalOrigin) {
        ZyxOrientedPosition zyxGlobalOrigin = globalOrigin.toZyxOrientedPosition();
        Vector3 rotatorZ = Vector3.UP_VECTOR.scaled(zyxGlobalOrigin.eulerZYX.z);
        Vector3 rotatorY = Vector3.Y_VECTOR.rotate(rotatorZ).scaled(zyxGlobalOrigin.eulerZYX.y);
        Vector3 rotatorXPosition = Vector3.X_VECTOR.rotate(rotatorZ).rotate(rotatorY).scaled(zyxGlobalOrigin.eulerZYX.x);
        Vector3 rotatorXOrientation = Vector3.X_VECTOR.rotate(rotatorZ).rotate(rotatorY).scaled(-zyxGlobalOrigin.eulerZYX.x);// wtf?

        Vector3 rotatedTranslatedPosition = position
                .rotate(rotatorZ)
                .rotate(rotatorY)
                .rotate(rotatorXPosition)
                .plus(globalOrigin.position);
        Orientation rotatedOrientation = orientation
                .rotate(rotatorZ)
                .rotate(rotatorY)
                .rotate(rotatorXOrientation);

        return new OrientedPosition(rotatedTranslatedPosition, rotatedOrientation);
    }
}
