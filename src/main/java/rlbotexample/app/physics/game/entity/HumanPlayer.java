package rlbotexample.app.physics.game.entity;

import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;

public class HumanPlayer implements PlayableEntity {

    public ExtendedCarData humanCar;
    public int health;

    public HumanPlayer(ExtendedCarData humanCar) {
        this.humanCar = humanCar;
        this.health = 5;
    }

    @Override
    public void step(DataPacket input) {

    }
}
