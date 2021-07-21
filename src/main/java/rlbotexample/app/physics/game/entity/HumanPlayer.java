package rlbotexample.app.physics.game.entity;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;

public class HumanPlayer implements PlayableEntity {

    public int health;

    public HumanPlayer() {
        this.health = CurrentGame.PLAYER_INITIAL_HP;
    }

    @Override
    public void step(DataPacket input) {

    }

    public void takeDamage(DataPacket input, int amount, Vector3 newVelocity) {
        // ouch
        health -= amount;

        //knock back
        PhysicsOfBossBattle.setVelocity(input.humanCar.velocity.scaled(0.9).plus(newVelocity), input.humanCar);

        // TODO: add knock back for the angular velocity too!
    }
}
