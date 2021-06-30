package rlbotexample.app.physics.state_setter;

import rlbot.gamestate.*;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_situation.GameSituation;
import util.math.vector.Ray3;
import util.math.vector.Vector3;

public class BallStateSetter {

    public static void handleBallState(DataPacket input) {
        BallStateSetter.useBallAsCameraFor(input.humanCar, input.car.position);

    }

    private static void useBallAsCameraFor(ExtendedCarData playerCar, Vector3 focusCar) {
        Ray3 camera = new Ray3(playerCar.position, focusCar.minus(playerCar.position));
        Vector3 ballPosition = camera.offset.plus(camera.direction.scaledToMagnitude(30000));
        if(ballPosition.z < -2000) {
            double howMuchTooMuchLengthIsZ = Math.abs(ballPosition.z - camera.offset.z)/(2000 + Math.abs(camera.offset.z));
            ballPosition = camera.offset.plus(camera.direction.scaledToMagnitude(30000).scaled(1/howMuchTooMuchLengthIsZ));
        }

        GameState gameState = GameSituation.getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState().withLocation(new DesiredVector3((float)-ballPosition.x, (float)ballPosition.y, (float)ballPosition.z))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(0f, 0f, 0f))));

        GameSituation.applyGameState(gameState);
    }
}