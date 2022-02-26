package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.menu_and_game_over.ui_components.SphericalButton;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;
import util.state_machine.State;

import java.awt.*;

public class GameOverPlayerWon implements State {

    private static final Vector3 SPHERE_BUTTON_POSITION = new Vector3();
    private static final SphericalButton BACK_TO_MENU_BUTTON = new SphericalButton(SPHERE_BUTTON_POSITION, 300, "Back to main menu", Color.green);

    @Override
    public void start(DataPacket input) {

    }

    @Override
    public void exec(DataPacket input) {
        BallStateSetter.setTarget(SPHERE_BUTTON_POSITION);
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(BACK_TO_MENU_BUTTON.isPressed(input)) {
            return new WaitForAssetsToLoad();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {
        RenderTasks.append(renderer -> renderer.drawString2d("You Won!",
                Color.green, new Point((int)(ScreenSize.WIDTH/2.3), ScreenSize.HEIGHT/6),
                3, 3));
        BACK_TO_MENU_BUTTON.render();
    }
}
