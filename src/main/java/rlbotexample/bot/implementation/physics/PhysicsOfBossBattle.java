package rlbotexample.bot.implementation.physics;

import rlbotexample.bot.implementation.physics.assigned_quantities.AssignedOrientedPosition;
import rlbotexample.bot.implementation.physics.assigned_quantities.AssignedVector3;
import rlbotexample.bot.implementation.physics.game.CurrentGame;
import rlbotexample.bot.implementation.physics.state_setter.BallStateSetter;
import rlbotexample.bot.implementation.physics.state_setter.CarStateSetter;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.ZyxOrientedPosition;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhysicsOfBossBattle {

    private static final List<AssignedVector3> assignedAccelerations = new ArrayList<>();
    private static final List<AssignedVector3> assignedPenetrations = new ArrayList<>();
    private static final List<AssignedOrientedPosition> assignedOrientedPositions = new ArrayList<>();

    public static void execute(DataPacket input) {
        CurrentGame.step(input);

        BallStateSetter.handleBallState(input);

        findAdditionalImpulses(input);
        applyImpulses(input);
    }

    public static void setOrientedPosition(ZyxOrientedPosition orientedPosition, ExtendedCarData carData) {
        assignedOrientedPositions.add(new AssignedOrientedPosition(carData, orientedPosition));
    }

    // all forces are applied to the center of mass for now
    public static void addImpulse(Vector3 force, ExtendedCarData carData) {
        assignedAccelerations.add(new AssignedVector3(carData, force.scaled(1 / RlConstants.CAR_MASS)));
    }

    public static void addAcceleration(Vector3 acceleration, ExtendedCarData carData) {
        assignedAccelerations.add(new AssignedVector3(carData, acceleration));
    }

    public static void addPenetrationError(Vector3 penetration, ExtendedCarData carData) {
        assignedPenetrations.add(new AssignedVector3(carData, penetration));
    }

    private static void findAdditionalImpulses(DataPacket input) {
        // no additional impulses for now
    }

    public static void applyImpulses(DataPacket input) {
        for(ExtendedCarData car: input.allCars) {
            Optional<Vector3> accelerationOpt;
            Optional<Vector3> penetrationOpt;
            Optional<ZyxOrientedPosition> orientedPositionOpt;

            accelerationOpt = assignedAccelerations.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();
            penetrationOpt = assignedPenetrations.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();

            orientedPositionOpt = assignedOrientedPositions.stream()
                    .filter(assignedOrientedPosition -> assignedOrientedPosition.carData == car)
                    .map(assignedOrientedPosition -> assignedOrientedPosition.orientedPosition)
                    .findFirst();

            accelerationOpt.ifPresent(impulse -> {});
            penetrationOpt.ifPresent(impulse -> {});

            orientedPositionOpt.ifPresent(orientedPosition -> CarStateSetter.moveTo(orientedPosition.position, orientedPosition.eulerZYX, car));
        }
        assignedAccelerations.clear();
        assignedPenetrations.clear();
        assignedOrientedPositions.clear();
    }
}