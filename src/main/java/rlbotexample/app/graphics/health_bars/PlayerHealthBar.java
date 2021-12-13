package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import util.math.vector.Vector2;

import java.awt.*;

public class PlayerHealthBar {

    private static final Vector2 UPPER_LEFT_POSITION = new Vector2(43, 962);
    private static final Color HEALTH_BAR_COLOR = new Color(49, 231, 52);

    public static void renderOnScreen(Renderer renderer, double hpRatio) {
        HealthBarSegment.printOnScreen(UPPER_LEFT_POSITION, hpRatio, HEALTH_BAR_COLOR);
    }

}
