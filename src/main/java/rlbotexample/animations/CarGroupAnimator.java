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
import java.util.stream.Collectors;

public class CarGroupAnimator implements AutoCloseable {

    private static final ZyxOrientedPosition ZYX_ORIENTED_POSITION_TO_RESET_CAR_WHEELS_SO_THAT_THE_DEMOLITION_STATE_GETS_RESETED =
            new OrientedPosition(
                    new Vector3(0, 0, 2050),    // on the ceiling
                    new Orientation(
                            new Vector3(1, 0, 0),   // nose in some random orientation (any works)
                            new Vector3(0, 0, -1))  // upside down
            ).toZyxOrientedPosition();

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

        List<Integer> teamIds = meshAnimation.frames.get(0).carGroup.carObjects.stream()
                .map(carObject -> carObject.teamId)
                .collect(Collectors.toList());
        Optional<List<Integer>> allocatedCarsOpt = CarResourceHandler.alloc(teamIds);
        this.carIndexesUsedForTheAnimation.addAll(allocatedCarsOpt.orElseGet(ArrayList::new));
    }

    public void step(DataPacket input) {
        final AtomicInteger safeBotIndex = new AtomicInteger(0);

        List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, carIndexesUsedForTheAnimation);
        carsUsedForTheAnimation.forEach(carData -> {
            if(carData.isDemolished) {
                PhysicsOfBossBattle.setOrientedPosition(ZYX_ORIENTED_POSITION_TO_RESET_CAR_WHEELS_SO_THAT_THE_DEMOLITION_STATE_GETS_RESETED, carData);
            }
            else {
                // ... uhm, what am I doing?
                ZyxOrientedPosition localZyxOrientedPosition =
                        meshAnimation.queryFrame(frameCount)
                        .carObjects.stream()
                        .map(carObject -> carObject.zyxOrientedPosition)
                        .collect(Collectors.toList())
                        .get(safeBotIndex.get());
                OrientedPosition localOrientedPosition = localZyxOrientedPosition.toCarOrientedPosition();
                OrientedPosition orientedPosition = localOrientedPosition.toGlobalPosition(this.orientedPosition);

                if(carData.position.minus(orientedPosition.position).magnitude() < 10000) {
                    Vector3 positionWithRigidity = carData.position.plus(orientedPosition.position.minus(carData.position).scaled(carsRigidity));
                    orientedPosition = new OrientedPosition(positionWithRigidity, orientedPosition.orientation);
                }

                stateSetWithSnapPhysics(orientedPosition, carData);
            }
            safeBotIndex.incrementAndGet();
        });

        frameCount++;
        if(isFinished()) {
            if(isLooping) {
                reset();
            }
            else {
                close();
            }
        }
    }

    private void stateSetWithSnapPhysics(OrientedPosition orientedPosition, ExtendedCarData carData) {
        PhysicsOfBossBattle.setOrientedPosition(orientedPosition.toZyxOrientedPosition(), carData);
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
