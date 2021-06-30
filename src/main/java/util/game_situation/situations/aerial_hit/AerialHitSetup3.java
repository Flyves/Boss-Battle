package util.game_situation.situations.aerial_hit;

import rlbot.gamestate.*;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class AerialHitSetup3 extends GameSituation {

    public AerialHitSetup3() {
        super(new FrameTimer(6*30));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState().withLocation(new DesiredVector3(3000f, 0f, 200f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(-800f, -200f, 1400f))));

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withRotation(new DesiredRotation(0f, (float)Math.PI/2, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withLocation(new DesiredVector3(500f, -3000f, 100f))
                        .withVelocity(new DesiredVector3(200f, 600f, 600f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
