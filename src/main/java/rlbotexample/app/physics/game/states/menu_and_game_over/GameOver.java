package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class GameOver implements State {

    private boolean hasPlayerWon;

    @Override
    public void start(DataPacket input) {

    }

    @Override
    public void exec(DataPacket input) {
        // the only way we're here is if either the player or the boss has lost
        hasPlayerWon = CurrentGame.bossAi.hasLost();
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(hasPlayerWon) {
            return new GameOverPlayerWon();
        }
        return new GameOverPlayerLost();
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
