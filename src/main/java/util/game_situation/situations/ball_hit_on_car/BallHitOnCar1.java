package util.game_situation.situations.ball_hit_on_car;

import rlbot.gamestate.*;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.math.vector.Vector3;
import util.timer.FrameTimer;

// this situation needs 0 gravity to work properly.
// we can modify gravity using bakkesmod, for example.
public class BallHitOnCar1 extends GameSituation {

    public BallHitOnCar1() {
        super(new FrameTimer(4*(int)RlConstants.BOT_REFRESH_RATE));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();

        Vector3 carPosition = new Vector3(0, 0, 1000);
        Vector3 ballPosition = Vector3.UP_VECTOR.scaled(1000)
                .rotate(Vector3.X_VECTOR.scaled(Math.random()*Math.PI))
                .rotate(Vector3.UP_VECTOR.scaled((Math.random()-0.5)*Math.PI))
                .plus(carPosition);
        Vector3 ballVelocity = carPosition.minus(ballPosition).scaledToMagnitude(100 + Math.random()*5000);

        // ball
        gameState.withBallState(new BallState(new PhysicsState()
                .withLocation(ballPosition.toDesiredVector3())
                .withVelocity(ballVelocity.toDesiredVector3())
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))));

        // player 0
        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(carPosition.toDesiredVector3())
                        .withVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withRotation(new DesiredRotation(0f, 0f, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
