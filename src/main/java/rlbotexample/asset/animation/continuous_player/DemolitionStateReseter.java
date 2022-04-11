package rlbotexample.asset.animation.continuous_player;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.math.vector.ZyxOrientedPosition;

public class DemolitionStateReseter {
    private static final ZyxOrientedPosition ZYX_ORIENTED_POSITION_TO_RESET_CAR_WHEELS_SO_THAT_THE_DEMOLITION_STATE_GETS_RESETED =
            new OrientedPosition(
                    new Vector3(0, 0, 2050),    // on the ceiling
                    new Orientation(
                            new Vector3(1, 0, 0),   // nose in some random orientation (any on xy plane works)
                            new Vector3(0, 0, -1))  // upside down
            ).toZyxOrientedPosition();

    public static void stateSet(final ExtendedCarData carData) {
        PhysicsOfBossBattle.setOrientedPosition(ZYX_ORIENTED_POSITION_TO_RESET_CAR_WHEELS_SO_THAT_THE_DEMOLITION_STATE_GETS_RESETED, carData);
    }
}