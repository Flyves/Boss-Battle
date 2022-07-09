package rlbotexample.app.physics.state_setter;

import rlbot.flat.FieldInfo;
import rlbot.gamestate.*;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_situation.GameStateHelper;

public class CarStateSetter {

    public static void applyPhysics(PhysicsState alternativePhysics, float boostAmount, ExtendedCarData carData) {
        GameState gameState = GameStateHelper.getCurrentGameState();
        final CarState defaultCarState = new CarState();
        final CarState alternativeCarState = defaultCarState.withPhysics(alternativePhysics);
        if(carData.isBot) {
            defaultCarState.withBoostAmount(boostAmount);
        }
        gameState.withCarState(carData.playerIndex, alternativeCarState);
        GameStateHelper.applyGameState(gameState);
    }

}