package rlbotexample.animations;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.math.vector.ZyxOrientedPosition;
import util.resource_handling.cars.CarResourceHandler;
import util.resource_handling.cars.PlayerAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CarGroupAnimator implements AutoCloseable {

    private static final double MAX_CAR_SPEED_FOR_CONVERGING_INTO_ANIMATION = 2300;
    private static final double FACTOR_OF_DISTANCE_FROM_ANIMATION_TO_FIND_DESIRED_SPEED = 10;
    private static final double MAX_CAR_ACCELERATION_FOR_CONVERGING_INTO_ANIMATION = 700;

    private static final double MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION = 4;
    private static final double FACTOR_OF_ORIENTATION_DISTANCE_FROM_ANIMATION_TO_FIND_DESIRED_SPIN = 2;

    private final CarGroupAnimation meshAnimation;
    public OrientedPosition orientedPosition;
    private int frameCount;
    private boolean isLooping;
    private boolean isClosed;
    public double carsRigidity;

    public final List<Integer> carIndexesUsedForTheAnimation = new ArrayList<>();

    public CarGroupAnimator(CarGroupAnimation meshAnimation) {
        this.meshAnimation = meshAnimation;
        this.orientedPosition = new OrientedPosition();
        this.frameCount = 0;
        this.isLooping = true;
        this.isClosed = false;
        this.carsRigidity = 1;

        int amountOfCarIndexesNeeded = meshAnimation.frames.get(0).carGroup.orientedPositions.size();
        Optional<List<Integer>> allocatedCarsOpt = CarResourceHandler.alloc(new PlayerAmount(amountOfCarIndexesNeeded));
        this.carIndexesUsedForTheAnimation.addAll(allocatedCarsOpt.orElseGet(ArrayList::new));
    }

    public void step(DataPacket input) {
        final AtomicInteger safeBotIndex = new AtomicInteger(0);

        List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, carIndexesUsedForTheAnimation);
        carsUsedForTheAnimation.forEach(carData -> {
            if(carData.isDemolished) {
                OrientedPosition positionToResetCarWheelsSoThatTheDemolishedStateGetsReseted = new OrientedPosition(new Vector3(0, 0, 2050), new Orientation(new Vector3(1, 0, 0), new Vector3(0, 0, -1)));
                PhysicsOfBossBattle.setOrientedPosition(positionToResetCarWheelsSoThatTheDemolishedStateGetsReseted.toZyxOrientedPosition(), carData);
            }
            else {
                try {
                    ZyxOrientedPosition localZyxOrientedPosition = meshAnimation.queryFrame(frameCount)
                            .orientedPositions.get(safeBotIndex.get());
                    OrientedPosition localOrientedPosition = localZyxOrientedPosition.toCarOrientedPosition();
                    OrientedPosition orientedPosition = localOrientedPosition.toGlobalPosition(this.orientedPosition);

                    if(carData.position.minus(orientedPosition.position).magnitude() < 10000) {
                        Vector3 positionWithRigidity = carData.position.plus(orientedPosition.position.minus(carData.position).scaled(carsRigidity));
                        orientedPosition = new OrientedPosition(positionWithRigidity, orientedPosition.orientation);
                    }

                    stateSetWithSnapPhysics(orientedPosition, carData);
                }
                catch(Exception ignored) {
                }
            }
            safeBotIndex.incrementAndGet();
        });
        frameCount++;
        if(isFinished()) {
            if(isLooping) {
                reset();
            }
            else {
                try {
                    close();
                }
                catch(RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stateSetWithSnapPhysics(OrientedPosition orientedPosition, ExtendedCarData carData) {
        PhysicsOfBossBattle.setOrientedPosition(orientedPosition.toZyxOrientedPosition(), carData);
    }

    private void stateSetWithSmoothPhysics(OrientedPosition orientedPosition, ExtendedCarData carData) {
        Vector3 deltaVelocity = findDesiredVelocityTowardsAnimationDestination(orientedPosition, carData).minus(carData.velocity)
                .scaled(carsRigidity);
        Vector3 deltaSpin = findDesiredSpinTowardsAnimationDestination(orientedPosition, carData).minus(carData.spin)
                .scaled(carsRigidity);

        PhysicsOfBossBattle.setVelocity(carData.velocity.minus(deltaVelocity), carData);
        PhysicsOfBossBattle.setSpin(deltaSpin.plus(carData.spin), carData);
    }

    private Vector3 findDesiredVelocityTowardsAnimationDestination(OrientedPosition orientedPosition, ExtendedCarData carData) {
        Vector3 desiredVelocityTowardsAnimationDestination = carData.position.minus(orientedPosition.position).scaled(FACTOR_OF_DISTANCE_FROM_ANIMATION_TO_FIND_DESIRED_SPEED);
        if(desiredVelocityTowardsAnimationDestination.magnitudeSquared() > MAX_CAR_SPEED_FOR_CONVERGING_INTO_ANIMATION * MAX_CAR_SPEED_FOR_CONVERGING_INTO_ANIMATION) {
            desiredVelocityTowardsAnimationDestination = desiredVelocityTowardsAnimationDestination.scaledToMagnitude(MAX_CAR_SPEED_FOR_CONVERGING_INTO_ANIMATION);
        }
        return desiredVelocityTowardsAnimationDestination;
    }

    private Vector3 findDesiredSpinTowardsAnimationDestination(OrientedPosition orientedPosition, ExtendedCarData carData) {
        Vector3 desiredSpinTowardsAnimationDestination = carData.orientation.noseVector.findRotator(orientedPosition.orientation.noseVector)
                .scaled(FACTOR_OF_ORIENTATION_DISTANCE_FROM_ANIMATION_TO_FIND_DESIRED_SPIN);
        if(desiredSpinTowardsAnimationDestination.magnitudeSquared() < MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION/2) {
            desiredSpinTowardsAnimationDestination = desiredSpinTowardsAnimationDestination.scaled(1/MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION)
                    .plus(carData.orientation.roofVector.findRotator(orientedPosition.orientation.roofVector))
                    .scaled(MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION);
        }
        if(desiredSpinTowardsAnimationDestination.magnitudeSquared() > MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION * MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION) {
            desiredSpinTowardsAnimationDestination = desiredSpinTowardsAnimationDestination.scaledToMagnitude(MAX_CAR_SPIN_FOR_CONVERGING_INTO_ANIMATION);
        }
        return desiredSpinTowardsAnimationDestination;
    }

    public boolean isFinished() {
        return frameCount >= meshAnimation.frames.size();
    }

    public void reset() {
        frameCount = 0;
    }

    @Override
    public void close() throws RuntimeException {
        if(!isClosed) {
            CarResourceHandler.free(carIndexesUsedForTheAnimation);
            frameCount = meshAnimation.frames.size();
            isClosed = true;
        }
    }

    public int currentFrameIndex() {
        return frameCount;
    }

    public void setCurrentFrameIndex(int newFrameCount) {
        frameCount = newFrameCount;
    }

    public void looping(boolean isLooping) {
        this.isLooping = isLooping;
    }
}
