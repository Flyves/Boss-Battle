package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.CurrentGame;
import util.math.vector.Vector2;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;

import java.awt.*;

public class BossHealthBar {

    //private static final Vector2 HEALTH_BARS_OFFSET = new Vector2(361, 0).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 HEALTH_BARS_OFFSET = new Vector2(360, 0).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 HP_BAR_1_POSITION = new Vector2(410, 105).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 HP_BAR_2_POSITION = HP_BAR_1_POSITION.plus(HEALTH_BARS_OFFSET);
    private static final Vector2 HP_BAR_3_POSITION = HP_BAR_2_POSITION.plus(HEALTH_BARS_OFFSET);

    private static final Color HEALTH_BAR_COLOR = new Color(229, 20, 20);

    public static void renderOnScreen(double hpRatio) {
        final double hpBar1Health = hpRatio > 2.0/3 ? (hpRatio - (2.0/3)) * 3 : 0;

        final double hpBar2Health;
        if(hpRatio > 2.0/3) {
            hpBar2Health = 1;
        }
        else if(hpRatio < 1.0/3) {
            hpBar2Health = 0;
        }
        else {
            hpBar2Health = (hpRatio - (1.0/3)) * 3;
        }

        final double hpBar3Health;
        if(hpRatio > 1.0/3) {
            hpBar3Health = 1;
        }
        else {
            hpBar3Health = hpRatio * 3;
        }
        
        RenderTasks.append(renderer -> renderer.drawString2d(Integer.toString(CurrentGame.bossAi.health), Color.CYAN, new Point(0, 0), 2, 2));

        //HealthBarSegment.printOnScreen(HP_BAR_1_POSITION, hpBar3Health, HEALTH_BAR_COLOR);
        //HealthBarSegment.printOnScreen(HP_BAR_2_POSITION, hpBar2Health, HEALTH_BAR_COLOR);
        //HealthBarSegment.printOnScreen(HP_BAR_3_POSITION, hpBar1Health, HEALTH_BAR_COLOR);
    }
}
