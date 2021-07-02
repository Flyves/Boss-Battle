package rlbotexample.app.physics;

import rlbot.gamestate.DesiredRotation;
import rlbot.gamestate.DesiredVector3;
import rlbot.gamestate.PhysicsState;
import rlbotexample.app.physics.assigned_quantities.AssignedOrientedPosition;
import rlbotexample.app.physics.assigned_quantities.AssignedVector3;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.app.physics.state_setter.CarStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.ZyxOrientedPosition;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhysicsOfBossBattle {

    private static final List<AssignedOrientedPosition> assignedOrientedPositions = new ArrayList<>();
    private static final List<AssignedVector3> assignedVelocities = new ArrayList<>();
    private static final List<AssignedVector3> assignedAccelerations = new ArrayList<>();
    private static final List<AssignedVector3> assignedPenetrations = new ArrayList<>();

    public static void execute(DataPacket input) {
        CurrentGame.step(input);

        applyImpulses(input);

        BallStateSetter.handleBallState(input);
    }

    public static void setOrientedPosition(ZyxOrientedPosition orientedPosition, ExtendedCarData carData) {
        assignedOrientedPositions.add(new AssignedOrientedPosition(carData, orientedPosition));
    }

    public static void setVelocity(Vector3 newVelocity, ExtendedCarData carData) {
        assignedVelocities.add(new AssignedVector3(carData, newVelocity));
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

    public static void applyImpulses(DataPacket input) {
        for(ExtendedCarData car: input.allCars) {
            Optional<ZyxOrientedPosition> orientedPositionOpt;
            Optional<Vector3> velocityOpt;
            Optional<Vector3> accelerationOpt;
            Optional<Vector3> penetrationOpt;

            final PhysicsState alternativePhysics = new PhysicsState();

            orientedPositionOpt = assignedOrientedPositions.stream()
                    .filter(assignedOrientedPosition -> assignedOrientedPosition.carData == car)
                    .map(assignedOrientedPosition -> assignedOrientedPosition.orientedPosition)
                    .findFirst();

            velocityOpt = assignedVelocities.stream()
                    .filter(assignedVector3 -> {
                        if(assignedVector3.carData == car) {
                        }
                        return assignedVector3.carData == car;
                    })
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();
            accelerationOpt = assignedAccelerations.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();
            penetrationOpt = assignedPenetrations.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();

            final double dt = 1/RlConstants.BOT_REFRESH_RATE;
            penetrationOpt.ifPresent(penetration -> {
                final Vector3 newLocation = car.position.minus(penetration);
                final DesiredVector3 newLocationAsDesiredVector3 = newLocation.toFlippedDesiredVector3();
                alternativePhysics.withLocation(newLocationAsDesiredVector3);
            });

            velocityOpt.ifPresent(velocity -> {
                alternativePhysics.withVelocity(velocity.toFlippedDesiredVector3());
            });

            orientedPositionOpt.ifPresent(orientedPosition -> {
                if(!penetrationOpt.isPresent()) {
                    alternativePhysics.withLocation(orientedPosition.position.toFlippedDesiredVector3());
                }
                else {
                    alternativePhysics.withLocation(orientedPosition.position.minus(penetrationOpt.get()).toFlippedDesiredVector3());
                }
                alternativePhysics.withRotation(new DesiredRotation(
                        (float)orientedPosition.eulerZYX.y,
                        (float)-orientedPosition.eulerZYX.z,
                        (float)orientedPosition.eulerZYX.x));
                if(!velocityOpt.isPresent() && !accelerationOpt.isPresent()) {
                    alternativePhysics.withVelocity(new Vector3(0, 0, 0).toFlippedDesiredVector3());
                }
            });

            accelerationOpt.ifPresent(acceleration -> {
                final Vector3 newVelocity = car.velocity.plus(acceleration.scaled(dt));
                final DesiredVector3 newVelocityAsDesiredVector3 = newVelocity.toFlippedDesiredVector3();
                alternativePhysics.withVelocity(newVelocityAsDesiredVector3);
            });

            CarStateSetter.applyPhysics(alternativePhysics, car);
        }

        assignedOrientedPositions.clear();
        assignedVelocities.clear();
        assignedAccelerations.clear();
        assignedPenetrations.clear();
    }
}