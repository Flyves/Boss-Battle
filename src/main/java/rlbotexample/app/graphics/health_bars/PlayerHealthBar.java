package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.CurrentGame;
import util.math.vector.Vector2;
import util.renderers.RenderTasks;

import java.awt.*;

public class PlayerHealthBar {
    private static final Vector2 UPPER_LEFT_POSITION = new Vector2(43, 920);

    public static void renderOnScreen() {
        Color lifeColor = Color.CYAN;
        if(CurrentGame.humanPlayer.health < 20) {
            lifeColor = Color.red;
        }
        else if(CurrentGame.humanPlayer.health < 50) {
            lifeColor = Color.yellow;
        }
        final Color finalLifeColor = lifeColor;
        HealthBarSegment2.printOnScreen(UPPER_LEFT_POSITION, CurrentGame.humanPlayer.health/(double)CurrentGame.PLAYER_INITIAL_HP, finalLifeColor);
        RenderTasks.append(renderer -> renderer.drawString2d("Player HP",
                new Color(0, 0, 0, 200), new Point((int)(ScreenSize.WIDTH*0.085), (int)(ScreenSize.HEIGHT*0.865)),
                1, 1));
    }

}
