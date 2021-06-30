package util.game_situation.situations.air_dribble;

import rlbot.gamestate.*;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.timer.FrameTimer;

public class AirDribbleSetup4 extends GameSituation {

    public AirDribbleSetup4() {
        super(new FrameTimer((int)(5*RlConstants.BOT_REFRESH_RATE)));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();
        gameState.withCarState(0, new CarState()

                .withPhysics(new PhysicsState()
                        .withLocation(new DesiredVector3(0f, 0f, 300f))
                        .withVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withRotation(new DesiredRotation((float)Math.PI/2, (float)-Math.PI/2, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        gameState.withCarState(1, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(new DesiredVector3(0f, 0f, 450f))
                        .withVelocity(new DesiredVector3(0f, 0f, 0f))
                        .withRotation(new DesiredRotation(-(float)Math.PI, -(float)Math.PI/2, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
