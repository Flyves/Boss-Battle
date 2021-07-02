package rlbotexample.app.physics.game.entity;

import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.CarOrientedPosition;
import util.math.vector.Vector3;
import util.resource_handling.CarResourceHandler;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BossAi implements PlayableEntity, AutoCloseable {

    public ExtendedCarData mainCar;
    public int health;
    public CarOrientedPosition orientedPosition;
    public Vector3 centerOfMass;

    public CarGroupAnimator animator = new CarGroupAnimator(GameAnimations.quadrupedal_boss_rigged_walk);

    public BossAi(ExtendedCarData mainCar) {
        this.mainCar = mainCar;
        this.health = CurrentGame.BOSS_INITIAL_HP;
        this.orientedPosition = new CarOrientedPosition();
        this.orientedPosition.position = new Vector3();
        this.centerOfMass = mainCar.position;
    }

    @Override
    public void step(DataPacket input) {
        List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, animator.carIndexesUsedForTheAnimation);
        updateAverageCenterPositionOfCars(input, carsUsedForTheAnimation);
        animator.orientedPosition = this.orientedPosition;
        animator.step(input);
    }

    private void updateAverageCenterPositionOfCars(DataPacket input, List<ExtendedCarData> carsUsedForTheAnimation) {
        AtomicReference<Vector3> vector3Ref = new AtomicReference<>(new Vector3());
        carsUsedForTheAnimation.forEach(car -> {
            if(car != input.humanCar) {
                vector3Ref.set(vector3Ref.get().plus(car.position));
            }
        });
        this.centerOfMass = vector3Ref.get().scaled(1.0/carsUsedForTheAnimation.size());
    }

    @Override
    public void close() throws RuntimeException {
        animator.close();
    }
}
