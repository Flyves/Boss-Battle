package util.game_situation.situations.throwing_player_in_the_air;

import rlbot.gamestate.*;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.math.vector.Vector3;
import util.timer.FrameTimer;

// this situation needs 0 gravity to work properly.
// we can modify gravity using bakkesmod, for example.
public class ThrowingPlayer1 extends GameSituation {

    public ThrowingPlayer1() {
        super(new FrameTimer(4*(int)RlConstants.BOT_REFRESH_RATE));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();

        Vector3 carPosition = new Vector3(0, 0, 1000);
        Vector3 carVelocity = new Vector3((Math.random()-0.5) * 4600, (Math.random()-0.5) * 4600, (Math.random()-0.5) * 4600);

        gameState.withCarState(0, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(carPosition.toDesiredVector3())
                        .withVelocity(carVelocity.toDesiredVector3())
                        .withRotation(new DesiredRotation(0f, 0f, 0f))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
