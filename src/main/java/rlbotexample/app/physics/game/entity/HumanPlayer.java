package rlbotexample.app.physics.game.entity;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;

public class HumanPlayer implements PlayableEntity {

    public int health;

    public HumanPlayer(ExtendedCarData humanCar) {
        this.health = 100;
    }

    @Override
    public void step(DataPacket input) {

    }

    public void takeDamage(DataPacket input, int amount, Vector3 newVelocity) {
        // ouch
        health -= amount;

        System.out.println("Player Health: " + health);

        //knock back
        PhysicsOfBossBattle.setVelocity(newVelocity, input.humanCar);

        // TODO: add knock back for the angular velocity too!
    }
}
