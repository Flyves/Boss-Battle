package rlbotexample.input.dynamic_data.car.orientation;


import rlbot.flat.PlayerInfo;
import rlbot.gamestate.DesiredRotation;
import util.math.vector.CarOrientedPosition;
import util.math.vector.Vector3;

/**
 * The car's orientation in space, a.k.a. what direction it's pointing.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class CarOrientation {

    /** The direction that the front of the car is facing */
    public final Vector3 noseVector;

    /** The direction the roof of the car is facing. (0, 0, 1) means the car is upright. */
    public final Vector3 roofVector;

    /** The direction that the right side of the car is facing. */
    public final Vector3 rightVector;

    public CarOrientation() {
        this.noseVector = new Vector3(Vector3.X_VECTOR);
        this.roofVector = new Vector3(Vector3.UP_VECTOR);
        this.rightVector = noseVector.crossProduct(roofVector);
    }

    public CarOrientation(Vector3 noseVector, Vector3 roofVector) {

        this.noseVector = noseVector;
        this.roofVector = roofVector;
        this.rightVector = noseVector.crossProduct(roofVector);
    }

    public static CarOrientation fromFlatbuffer(PlayerInfo playerInfo) {
        return convert(
                playerInfo.physics().rotation().pitch(),
                playerInfo.physics().rotation().yaw(),
                playerInfo.physics().rotation().roll());
    }

    /**
     * All params are in radians.
     */
    private static CarOrientation convert(double pitch, double yaw, double roll) {

        double noseX = -1 * Math.cos(pitch) * Math.cos(yaw);
        double noseY = Math.cos(pitch) * Math.sin(yaw);
        double noseZ = Math.sin(pitch);

        double roofX = Math.cos(roll) * Math.sin(pitch) * Math.cos(yaw) + Math.sin(roll) * Math.sin(yaw);
        double roofY = Math.cos(yaw) * Math.sin(roll) - Math.cos(roll) * Math.sin(pitch) * Math.sin(yaw);
        double roofZ = Math.cos(roll) * Math.cos(pitch);

        return new CarOrientation(new Vector3(noseX, noseY, noseZ), new Vector3(roofX, roofY, roofZ));
    }

    public CarOrientation rotate(Vector3 orientationRotator) {
        return new CarOrientation(noseVector.rotate(orientationRotator), roofVector.rotate(orientationRotator));
    }

    public CarOrientation matrixRotation(CarOrientation orientation) {
        return new CarOrientation(noseVector.matrixRotation(orientation), roofVector.matrixRotation(orientation));
    }
}
