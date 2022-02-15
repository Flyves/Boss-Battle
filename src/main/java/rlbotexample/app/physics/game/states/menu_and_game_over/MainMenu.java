package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class MainMenu implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.isGameOver = false;
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        return new GameActive();
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
