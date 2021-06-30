package rlbotexample.input.dynamic_data.car;

import rlbotexample.input.dynamic_data.car.hit_box.HitBox;
import rlbotexample.input.dynamic_data.car.hit_box.wheels.OctaneWheelBox;
import rlbotexample.input.dynamic_data.car.hit_box.wheels.WheelBox;
import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import util.math.vector.Vector3;

public class CarData {

    public final Vector3 position;
    public final Vector3 velocity;
    public final Vector3 spin;
    public final double boost;
    public final HitBox hitBox;
    public final WheelBox wheelBox;
    public final double elapsedSeconds;

    public CarData(Vector3 position, Vector3 velocity, Vector3 spin, double boostAmount, HitBox hitBox, double time) {
        this.position = position;
        this.velocity = velocity;
        this.spin = spin;
        this.boost = boostAmount;
        this.hitBox = hitBox;
        this.wheelBox = new OctaneWheelBox(position, new CarOrientation(hitBox.frontOrientation, hitBox.roofOrientation));
        this.elapsedSeconds = time;
    }

    public CarData(rlbot.flat.PlayerInfo playerInfo, float elapsedSeconds) {
        this.position = new Vector3(playerInfo.physics().location());
        this.velocity = new Vector3(playerInfo.physics().velocity());
        this.spin = new Vector3(playerInfo.physics().angularVelocity());
        this.boost = playerInfo.boost();
        final CarOrientation orientation = CarOrientation.fromFlatbuffer(playerInfo);
        this.hitBox = new HitBox(position, playerInfo.hitboxOffset(), playerInfo.hitbox(), orientation.noseVector, orientation.roofVector);
        this.wheelBox = new OctaneWheelBox(position, orientation);
        this.elapsedSeconds = elapsedSeconds;
    }

    public final Vector3 surfaceVelocity(final Vector3 normal) {
        return spin.crossProduct(normal).scaled(hitBox.closestPointOnSurface(normal.scaled(200)).minus(hitBox.centerPositionOfHitBox).magnitude());
    }
}
