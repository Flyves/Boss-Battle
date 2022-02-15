package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;
import util.renderers.ShapeRenderer;
import util.state_machine.State;

import java.awt.*;

public class GameOverPlayerLost implements State {

    private static final int SPHERE_RADII = 300;
    private static final Vector3 SPHERE_BUTTON_POSITION = new Vector3();

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
        if(input.humanCar.position.magnitudeSquared() < SPHERE_RADII*SPHERE_RADII) {
            return new WaitForAssetsToLoad();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer unused) {
        RenderTasks.append(renderer -> renderer.drawString2d("You lost!",
                Color.red, new Point((int)(ScreenSize.WIDTH/2.3), ScreenSize.HEIGHT/6),
                3, 3));
        RenderTasks.append(renderer -> renderer.drawString3d("Back to main menu",
                Color.WHITE,
                SPHERE_BUTTON_POSITION.plus(new Vector3(0, 0, 400)).toFlatVector(),
                1, 1));
        RenderTasks.append(renderer -> new ShapeRenderer(renderer).renderChaoticSphere(SPHERE_BUTTON_POSITION, SPHERE_RADII, Color.green));
    }
}
