package rlbotexample.app.physics.game.entity;

import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.CarOrientedPosition;
import util.math.vector.Vector3;

public class BossAi implements PlayableEntity {

    public ExtendedCarData mainCar;
    public int health;
    public CarOrientedPosition orientedPosition;

    public CarGroupAnimator animator = new CarGroupAnimator(GameAnimations.quadrupedal_boss_rigged_walk);

    public BossAi(ExtendedCarData mainCar) {
        this.mainCar = mainCar;
        this.health = 1000;
        this.orientedPosition = new CarOrientedPosition();
        orientedPosition.position = new Vector3(0, 500, 0);
    }

    @Override
    public void step(DataPacket input) {
        animator.orientedPosition = this.orientedPosition;
        animator.step(input);
    }
}
