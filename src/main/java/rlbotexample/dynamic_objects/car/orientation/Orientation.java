package rlbotexample.dynamic_objects.car.orientation;


import rlbot.flat.PlayerInfo;
import util.math.vector.Vector3;

/**
 * The car's orientation in space, a.k.a. what direction it's pointing.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class Orientation {

    /** The direction that the front of the car is facing */
    public Vector3 noseVector;

    /** The direction the roof of the car is facing. (0, 0, 1) means the car is upright. */
    public Vector3 roofVector;

    /** The direction that the right side of the car is facing. */
    public Vector3 rightVector;

    public Orientation() {
        this.noseVector = new Vector3(Vector3.X_VECTOR);
        this.roofVector = new Vector3(Vector3.UP_VECTOR);
        this.rightVector = noseVector.crossProduct(roofVector);
    }

    public Orientation(Vector3 noseVector, Vector3 roofVector) {

        this.noseVector = noseVector;
        this.roofVector = roofVector;
        this.rightVector = noseVector.crossProduct(roofVector);
    }

    public static Orientation fromFlatbuffer(PlayerInfo playerInfo) {
        return convert(
                playerInfo.physics().rotation().pitch(),
                playerInfo.physics().rotation().yaw(),
                playerInfo.physics().rotation().roll());
    }

    /**
     * All params are in radians.
     */
    private static Orientation convert(double pitch, double yaw, double roll) {

        double noseX = -1 * Math.cos(pitch) * Math.cos(yaw);
        double noseY = Math.cos(pitch) * Math.sin(yaw);
        double noseZ = Math.sin(pitch);

        double roofX = Math.cos(roll) * Math.sin(pitch) * Math.cos(yaw) + Math.sin(roll) * Math.sin(yaw);
        double roofY = Math.cos(yaw) * Math.sin(roll) - Math.cos(roll) * Math.sin(pitch) * Math.sin(yaw);
        double roofZ = Math.cos(roll) * Math.cos(pitch);

        return new Orientation(new Vector3(noseX, noseY, noseZ), new Vector3(roofX, roofY, roofZ));
    }

    public Orientation rotate(Vector3 orientationRotator) {
        return new Orientation(noseVector.rotate(orientationRotator), roofVector.rotate(orientationRotator));
    }

    public Orientation matrixRotation(Orientation orientation) {
        return new Orientation(noseVector.matrixRotation(orientation), roofVector.matrixRotation(orientation));
    }
}
