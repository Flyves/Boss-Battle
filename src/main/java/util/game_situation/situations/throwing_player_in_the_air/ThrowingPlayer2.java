package util.game_situation.situations.throwing_player_in_the_air;

import rlbot.gamestate.*;
import util.game_constants.RlConstants;
import util.game_situation.GameSituation;
import util.math.vector.Vector3;
import util.timer.FrameTimer;

// this situation needs 0 gravity to work properly.
// we can modify gravity using bakkesmod, for example.
public class ThrowingPlayer2 extends GameSituation {

    public ThrowingPlayer2() {
        super(new FrameTimer(5*(int)RlConstants.BOT_REFRESH_RATE));
    }

    @Override
    public void loadGameState() {
        GameState gameState = getCurrentGameState();

        Vector3 carPosition = new Vector3(-3000, -4000, 1500);
        Vector3 carVelocity = new Vector3(0, 1300, 1000);
        Vector3 carOrientation = new Vector3(-Math.PI*9.5/10, -Math.PI/2, -Math.PI/4);

        gameState.withCarState(1, new CarState()
                .withPhysics(new PhysicsState()
                        .withLocation(carPosition.toDesiredVector3())
                        .withVelocity(carVelocity.toDesiredVector3())
                        .withRotation(new DesiredRotation((float)-carOrientation.x, (float)carOrientation.y, (float)carOrientation.z))
                        .withAngularVelocity(new DesiredVector3(0f, 0f, 0f)))
                .withBoostAmount(100f));

        applyGameState(gameState);
    }
}
