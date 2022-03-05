package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.menu_and_game_over.ui_components.SphericalButton;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

import java.awt.*;

public class MainMenu implements State {

    private static final SphericalButton START_GAME_BUTTON = new SphericalButton(new Vector3(0, 0, 1200), 300, "Start", Color.green);
    private static final SphericalButton OPTIONS_BUTTON = new SphericalButton(new Vector3(1000, 600, 800),200,  "Options (in development)", Color.GRAY);
    private static final SphericalButton CREDITS_BUTTON = new SphericalButton(new Vector3(-900, 300, 1000),160,  "Credits (in development)", Color.GRAY);
    private static final SphericalButton QUIT_GAME_BUTTON = new SphericalButton(new Vector3(1700, 300, 1300), 180, "Quit", Color.red.brighter());

    @Override
    public void start(DataPacket input) {
        CurrentGame.isGameOver = false;
        BallStateSetter.setTarget(START_GAME_BUTTON.getPosition());
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(START_GAME_BUTTON.isPressed(input)) {
            return new GameActive();
        }
        else if(QUIT_GAME_BUTTON.isPressed(input)) {
            BallStateSetter.quitGame();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {
        START_GAME_BUTTON.render();
        OPTIONS_BUTTON.render();
        CREDITS_BUTTON.render();
        QUIT_GAME_BUTTON.render();
    }
}
