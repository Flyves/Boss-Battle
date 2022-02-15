package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import util.math.vector.Vector2;
import util.renderers.RenderTasks;

import java.awt.*;

public class PlayerHealthBar {

    private static final Vector2 UPPER_LEFT_POSITION = new Vector2(43, 962);
    private static final Color HEALTH_BAR_COLOR = new Color(49, 231, 52);

    public static void renderOnScreen() {
        RenderTasks.append(renderer -> renderer.drawString2d(Integer.toString(CurrentGame.humanPlayer.health), Color.CYAN, new Point(0, 40), 2, 2));
    }

}
