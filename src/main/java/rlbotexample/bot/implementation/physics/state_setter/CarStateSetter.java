package rlbotexample.bot.implementation.physics.state_setter;

import rlbot.gamestate.*;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.math.vector.Vector3;

public class CarStateSetter {

    private static Vector3 previousPosition = new Vector3();
    private static Vector3 previousOrientation = new Vector3();

    public static void moveTo(Vector3 position, Vector3 orientation, ExtendedCarData carData) {
        Vector3 generatedVelocity = position.minus(previousPosition).scaled(RlConstants.BOT_REFRESH_RATE);
        previousPosition = position;

        Vector3 generatedSpin = orientation.minus(previousOrientation).scaled(RlConstants.BOT_REFRESH_RATE);
        previousOrientation = orientation;

        GameState gameState = GameSituation.getCurrentGameState();
        gameState.withCarState(carData.playerIndex, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(new DesiredVector3((float)-position.x, (float)position.y, (float)position.z))
                        .withVelocity(new DesiredVector3((float)0, (float)0, (float)0))
                        .withRotation(new DesiredRotation((float)orientation.y, (float)-orientation.z, (float)orientation.x))
                        //.withAngularVelocity(new DesiredVector3((float)generatedSpin.y, (float)-generatedSpin.z, (float)generatedSpin.x))
                ));
        GameSituation.applyGameState(gameState);
    }
}