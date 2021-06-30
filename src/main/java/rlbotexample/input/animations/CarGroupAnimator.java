package rlbotexample.input.animations;

import rlbotexample.bot.implementation.physics.PhysicsOfBossBattle;
import rlbotexample.input.dynamic_data.DataPacket;
import util.math.vector.CarOrientedPosition;
import util.math.vector.ZyxOrientedPosition;

import java.util.concurrent.atomic.AtomicInteger;

public class CarGroupAnimator {
    private final CarGroupAnimation meshAnimation;
    private int frameCount;
    private final AtomicInteger safeBotIndex;
    public boolean isLooping;
    public CarOrientedPosition orientedPosition;

    public CarGroupAnimator(CarGroupAnimation meshAnimation) {
        this.meshAnimation = meshAnimation;
        this.frameCount = 0;
        this.safeBotIndex = new AtomicInteger(0);
        this.isLooping = true;
        this.orientedPosition = new CarOrientedPosition();
    }

    public void step(DataPacket input) {
        safeBotIndex.set(0);
        input.allCars.forEach(carData -> {
            if(input.humanCar.playerIndex != carData.playerIndex) {
                try {
                    ZyxOrientedPosition localZyxOrientedPosition = meshAnimation.get(frameCount)
                            .orientedPositions.get(safeBotIndex.get());
                    CarOrientedPosition localCarOrientedPosition = localZyxOrientedPosition.toCarOrientedPosition();
                    CarOrientedPosition carOrientedPosition = localCarOrientedPosition.toGlobalPosition(orientedPosition);

                    PhysicsOfBossBattle.setOrientedPosition(carOrientedPosition.toZyxOrientedPosition(), carData);
                    safeBotIndex.incrementAndGet();
                }
                catch (Exception ignored) {}
            }
        });
        frameCount++;
        if(isFinished() && isLooping) {
            reset();
        }
    }

    public boolean isFinished() {
        return frameCount >= meshAnimation.frames.size();
    }

    public void reset() {
        frameCount = 0;
    }
}
