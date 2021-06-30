package util.game_situation.situations.car_orientation;

import rlbot.gamestate.*;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class AerialOrientationTesterSetup extends GameSituation {

    public AerialOrientationTesterSetup() {
        super(new FrameTimer(10000*30));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState()
                .withLocation(new DesiredVector3(0f, 0f, 100f))
                .withVelocity(new DesiredVector3(0f, 0f, 0f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))));

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(new DesiredVector3(0f, 0f, 500f))
                        .withVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withRotation(new DesiredRotation(0f, 0f, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
