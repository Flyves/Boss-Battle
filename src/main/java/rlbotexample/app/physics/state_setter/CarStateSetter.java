package rlbotexample.app.physics.state_setter;

import rlbot.gamestate.*;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.math.vector.Vector3;

public class CarStateSetter {

    public static void applyPhysics(PhysicsState alternativePhysics, ExtendedCarData carData) {
        GameState gameState = GameSituation.getCurrentGameState();
        final CarState defaultCarState = new CarState();
        final CarState alternativeCarState = defaultCarState.withPhysics(alternativePhysics);
        gameState.withCarState(carData.playerIndex, alternativeCarState);
        GameSituation.applyGameState(gameState);
    }

}