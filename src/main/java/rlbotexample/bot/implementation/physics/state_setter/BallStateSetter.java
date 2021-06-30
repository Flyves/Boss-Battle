package rlbotexample.bot.implementation.physics.state_setter;

import rlbot.gamestate.*;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import rlbotexample.input.dynamic_data.car.hit_box.HitBox;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.math.vector.Ray3;
import util.math.vector.Vector3;

public class BallStateSetter {

    public static void handleBallState(DataPacket input) {
        //BallStateSetter.setBallOutOfTheMap();
        BallStateSetter.setBallAsOtherPlayerCam(input.humanCar, input.car);
        //BallStateSetter.setBallAsOtherPlayerCam2(input.allCars.get(1-input.playerIndex), input.car);
        //BallStateSetter.setBallAsOtherPlayerCam2(input.allCars.get(1-input.playerIndex), input.allCars.get(2-input.playerIndex));
    }

    private static void setBallAsOtherPlayerCam(ExtendedCarData playerCar, ExtendedCarData focusCar) {
        Ray3 camera = new Ray3(playerCar.position, focusCar.position.minus(playerCar.position));
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

    public static void setBallOutOfTheMap() {
        GameState gameState = GameSituation.getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState().withLocation(new DesiredVector3(0f, 0f, 2200f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(0f, 0f, 0f))));

        GameSituation.applyGameState(gameState);
    }
}