package rlbotexample.dynamic_objects.car;

import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.Vector3;

public class CarData {

    public final Vector3 position;
    public final Vector3 velocity;
    public final Vector3 spin;
    public final double boost;
    public final double elapsedSeconds;

    public CarData(Vector3 position, Vector3 velocity, Vector3 spin, double boostAmount, double time) {
        this.position = position;
        this.velocity = velocity;
        this.spin = spin;
        this.boost = boostAmount;
        this.elapsedSeconds = time;
    }

    public CarData(rlbot.flat.PlayerInfo playerInfo, float elapsedSeconds) {
        this.position = new Vector3(playerInfo.physics().location());
        this.velocity = new Vector3(playerInfo.physics().velocity());
        this.spin = new Vector3(playerInfo.physics().angularVelocity());
        this.boost = playerInfo.boost();
        final Orientation orientation = Orientation.fromFlatbuffer(playerInfo);
        this.elapsedSeconds = elapsedSeconds;
    }
}
