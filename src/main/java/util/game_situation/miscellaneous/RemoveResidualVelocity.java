package util.game_situation.miscellaneous;

import rlbot.gamestate.*;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class RemoveResidualVelocity extends GameSituation {

    public RemoveResidualVelocity() {
        super(new FrameTimer(1*30));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState().withLocation(new DesiredVector3(0f, 0f, 0f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(0f, 0f, 0f))));

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withRotation(new DesiredRotation(0f, 0f, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withLocation(new DesiredVector3(0f, 0f, 1000f))
                        .withVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
