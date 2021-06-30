package util.game_situation.situations.ball_hit;

import rlbot.gamestate.*;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class CeilingHit1 extends GameSituation {

    public CeilingHit1() {
        super(new FrameTimer(4*30));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState().withLocation(new DesiredVector3(0f, 0f, 1300f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 2f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(0f, 0f, 1000f))));

        applyGameState(gameState);
    }
}
