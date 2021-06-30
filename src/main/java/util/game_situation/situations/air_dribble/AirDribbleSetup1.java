package util.game_situation.situations.air_dribble;

import rlbot.gamestate.*;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class AirDribbleSetup1 extends GameSituation {

    public AirDribbleSetup1() {
        super(new FrameTimer((int)(10000*RlConstants.BOT_REFRESH_RATE)));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState()
                .withLocation(new DesiredVector3(3f, 0f, 450f))
                .withVelocity(new DesiredVector3(0f, 0f, 0f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))));

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(new DesiredVector3(0f, 0f, 300f))
                        .withVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withRotation(new DesiredRotation((float)Math.PI/2, (float)-Math.PI/2, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
