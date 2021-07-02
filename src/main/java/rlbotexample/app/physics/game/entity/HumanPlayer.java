package rlbotexample.app.physics.game.entity;

import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;

public class HumanPlayer implements PlayableEntity {

    public int health;

    public HumanPlayer(ExtendedCarData humanCar) {
        this.health = 5;
    }

    @Override
    public void step(DataPacket input) {
    }

    public void takeDamage(int amount, Vector3 directionOfHit) {

    }
}
