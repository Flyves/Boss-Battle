package rlbotexample.bot.implementation.physics.game.entity;

import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;

public class HumanPlayer implements PlayableEntity {

    public ExtendedCarData humanCar;
    public int health;

    public HumanPlayer(ExtendedCarData humanCar) {
        this.humanCar = humanCar;
        this.health = 100;
    }

    @Override
    public void step(DataPacket input) {

    }
}
