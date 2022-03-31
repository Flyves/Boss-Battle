package rlbotexample.app.physics.state_setter;

import rlbot.gamestate.*;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_situation.GameStateHelper;

public class CarStateSetter {

    public static void applyPhysics(PhysicsState alternativePhysics, ExtendedCarData carData) {
        GameState gameState = GameStateHelper.getCurrentGameState();
        final CarState defaultCarState = new CarState();
        final CarState alternativeCarState = defaultCarState.withPhysics(alternativePhysics);
        gameState.withCarState(carData.playerIndex, alternativeCarState);
        GameStateHelper.applyGameState(gameState);
    }

}