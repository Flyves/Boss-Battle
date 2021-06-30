package rlbotexample.bot.implementation.physics.game.entity;

import rlbotexample.input.animations.CarGroupAnimator;
import rlbotexample.input.animations.GameAnimations;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import util.math.vector.CarOrientedPosition;

public class BossAi implements PlayableEntity {

    public ExtendedCarData mainCar;
    public int health;
    public CarOrientedPosition orientedPosition;

    public CarGroupAnimator animator = new CarGroupAnimator(GameAnimations.quadrupedal_boss_rigged_walk);

    public BossAi(ExtendedCarData mainCar) {
        this.mainCar = mainCar;
        this.health = 1000;
        this.orientedPosition = new CarOrientedPosition();
    }

    @Override
    public void step(DataPacket input) {
        animator.orientedPosition = this.orientedPosition;
        animator.step(input);
    }
}
