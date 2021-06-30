package util.game_situation.situations.ground_dribble;

import rlbot.gamestate.*;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class GroundDribbleSetup1 extends GameSituation {

    public GroundDribbleSetup1() {
        super(new FrameTimer((int)(30*RlConstants.BOT_REFRESH_RATE)));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState()
                .withLocation(new DesiredVector3(0f, 4900f/*3980f*/, 100f))
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withRotation(new DesiredRotation(0f, 0f, 0f))
                .withVelocity(new DesiredVector3(0f, -100f, -100f))));

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withRotation(new DesiredRotation(0f, (float)-Math.PI/2, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withLocation(new DesiredVector3(0f, 5500f, 0f))
                        .withVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
