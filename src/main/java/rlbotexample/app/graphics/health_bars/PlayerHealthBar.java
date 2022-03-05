package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.CurrentGame;
import util.math.vector.Vector2;
import util.renderers.RenderTasks;

import java.awt.*;

public class PlayerHealthBar {
    private static final Vector2 UPPER_LEFT_POSITION = new Vector2(43, 962);
    private static final Color HEALTH_BAR_COLOR = new Color(49, 231, 52);

    public static void renderOnScreen() {
        Color lifeColor = Color.CYAN;
        if(CurrentGame.humanPlayer.health < 20) {
            lifeColor = Color.red;
        }
        else if(CurrentGame.humanPlayer.health < 50) {
            lifeColor = Color.yellow;
        }
        final Color finalLifeColor = lifeColor;
        RenderTasks.append(renderer -> renderer.drawString2d("Player HP: " + CurrentGame.humanPlayer.health,
                finalLifeColor, new Point((int)(ScreenSize.WIDTH*0.03), (int)(ScreenSize.HEIGHT*0.85)),
                2, 2));
    }

}
