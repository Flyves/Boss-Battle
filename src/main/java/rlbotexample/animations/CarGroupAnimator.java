package rlbotexample.animations;

import jdk.internal.org.jline.utils.ClosedException;
import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.CarOrientedPosition;
import util.math.vector.Vector3;
import util.math.vector.ZyxOrientedPosition;
import util.resource_handling.CarResourceHandler;
import util.resource_handling.PlayerAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CarGroupAnimator implements AutoCloseable {
    private final CarGroupAnimation meshAnimation;
    public CarOrientedPosition orientedPosition;
    private int frameCount;
    public boolean isLooping;
    private boolean isClosed = false;

    public final List<Integer> carIndexesUsedForTheAnimation = new ArrayList<>();

    public CarGroupAnimator(CarGroupAnimation meshAnimation) {
        this.meshAnimation = meshAnimation;
        this.orientedPosition = new CarOrientedPosition();
        this.frameCount = 0;
        this.isLooping = true;

        int amountOfCarIndexesNeeded = meshAnimation.frames.get(0).carGroup.orientedPositions.size();
        Optional<List<Integer>> allocatedCarsOpt = CarResourceHandler.alloc(new PlayerAmount(amountOfCarIndexesNeeded));
        this.carIndexesUsedForTheAnimation.addAll(allocatedCarsOpt.orElseGet(ArrayList::new));
    }

    public void step(DataPacket input) {
        final AtomicInteger safeBotIndex = new AtomicInteger(0);

        List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, carIndexesUsedForTheAnimation);
        carsUsedForTheAnimation.forEach(carData -> {
            if(carData.isDemolished) {
                CarOrientedPosition positionToResetCarWheelsSoThatTheDemolishedStateGetsReseted = new CarOrientedPosition(new Vector3(0, 0, 2050), new Orientation(new Vector3(1, 0, 0), new Vector3(0, 0, -1)));
                PhysicsOfBossBattle.setOrientedPosition(positionToResetCarWheelsSoThatTheDemolishedStateGetsReseted.toZyxOrientedPosition(), carData);
            }
            else {
                try {
                    ZyxOrientedPosition localZyxOrientedPosition = meshAnimation.queryFrame(frameCount)
                            .orientedPositions.get(safeBotIndex.get());
                    CarOrientedPosition localCarOrientedPosition = localZyxOrientedPosition.toCarOrientedPosition();
                    CarOrientedPosition carOrientedPosition = localCarOrientedPosition.toGlobalPosition(orientedPosition);

                    PhysicsOfBossBattle.setOrientedPosition(carOrientedPosition.toZyxOrientedPosition(), carData);
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
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
            isClosed = true;
        }
        else {
            throw new RuntimeException();
        }
    }
}
