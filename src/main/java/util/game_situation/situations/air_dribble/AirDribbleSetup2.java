package util.game_situation.situations.air_dribble;

import rlbot.gamestate.*;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class AirDribbleSetup2 extends GameSituation {

    public AirDribbleSetup2() {
        super(new FrameTimer(10*30));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState().withLocation(new DesiredVector3(0f, 2000f, 500f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(0f, -100f, 500f))));

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withRotation(new DesiredRotation((float)Math.PI/2, (float)-Math.PI/2, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withLocation(new DesiredVector3(0f, 2000f, 300f))
                        .withVelocity(new DesiredVector3(0f, -100f, 500f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
