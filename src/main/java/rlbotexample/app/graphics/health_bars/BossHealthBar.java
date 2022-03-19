package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
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

    public static void renderOnScreen(double hpRatio, DataPacket input) {
        final int renderedHealth = (int) (CurrentGame.bossAi.health - (CurrentGame.BOSS_INITIAL_HP * (2 / 3.0))) * 3;
        hpRatio = renderedHealth/(double)CurrentGame.BOSS_INITIAL_HP;
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

        /*final int renderedHealth = (int) (CurrentGame.bossAi.health - (CurrentGame.BOSS_INITIAL_HP * (2 / 3.0))) * 3;
        final double stringScale = Math.atan(1000/CurrentGame.bossAi.centerOfMass.distance(input.humanCar.position));
        RenderTasks.append(renderer -> renderer.drawString3d("HP: " + renderedHealth,
                Color.CYAN, CurrentGame.bossAi.centerOfMass.plus(new Vector3(0, 0, 500)).toFlatVector(),
                (int)(5*stringScale), (int)(5*stringScale)));*/
        HealthBarSegment2.printOnScreen(HP_BAR_1_POSITION, hpBar3Health, HEALTH_BAR_COLOR);
        HealthBarSegment2.printOnScreen(HP_BAR_2_POSITION, hpBar2Health, HEALTH_BAR_COLOR);
        HealthBarSegment2.printOnScreen(HP_BAR_3_POSITION, hpBar1Health, HEALTH_BAR_COLOR);
        RenderTasks.append(renderer -> renderer.drawString2d("Rocket Slayer HP",
                new Color(0, 0, 0, 200), new Point((int)(ScreenSize.WIDTH*0.445), (int)(ScreenSize.HEIGHT*0.11)),
                1, 1));
    }
}
