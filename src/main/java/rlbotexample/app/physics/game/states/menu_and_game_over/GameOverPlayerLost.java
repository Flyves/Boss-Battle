package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.states.menu_and_game_over.ui_components.SphericalButton;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.assets.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;
import util.renderers.ShapeRenderer;
import util.state_machine.State;
import util.tinysound.TinySound;

import java.awt.*;

public class GameOverPlayerLost implements State {

    private static final Vector3 SPHERE_BUTTON_POSITION = new Vector3();
    private static final SphericalButton BACK_TO_MENU_BUTTON = new SphericalButton(SPHERE_BUTTON_POSITION, 300, "Back to main menu", Color.green);

    @Override
    public void start(DataPacket input) {
        TinySound.init();
        TinySound.loadSound(GameSoundFiles.game_lost).play(0.15);
    }

    @Override
    public void exec(DataPacket input) {
        BallStateSetter.setTarget(SPHERE_BUTTON_POSITION);
    }

    @Override
    public void stop(DataPacket input) {
        TinySound.shutdown();
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
        RenderTasks.append(renderer -> renderer.drawString2d("You lost!",
                Color.red, new Point((int)(ScreenSize.WIDTH/2.3), ScreenSize.HEIGHT/6),
                3, 3));
        BACK_TO_MENU_BUTTON.render();
    }
}
