package rlbotexample.app.graphics.health_bars;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.ScreenSize;
import util.math.vector.Vector2;
import util.math.vector.Vector2Int;
import util.renderers.RenderTasks;

import java.awt.*;

public class HealthBarSegment {

    // see the naming dimensions gimp file for reference

    private static final Vector2 offsetAB = new Vector2(8, -16).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 offsetCD = new Vector2(8, 8).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 widthHeightXY = new Vector2(315, 16).scaled(ScreenSize.FULL_HD_RATIO);

    public static void printOnScreen(Renderer renderer, Vector2 position, double hpRatio, Color color) {
        if(hpRatio > 1) {
            hpRatio = 1;
        }
        else if(hpRatio < 0) {
            hpRatio = 0;
        }

        final Color bgColor = new Color(14, 14, 14);
        final Vector2Int bgPieceSize = widthHeightXY.plus(offsetCD.scaled(2)).toVector2Int();
        final Vector2Int coloredHealthPieceSize = new Vector2Int((int)(widthHeightXY.x * hpRatio), (int)widthHeightXY.y);


        // in progress
        new RenderTasks()
                .append(r -> r.drawRectangle2d(bgColor, position.toAwtPoint(), bgPieceSize.x, bgPieceSize.y, true))
                //.append(r -> r.drawRectangle2d(bgColor, position.plus(offsetAB).toAwtPoint(), bgPieceSize.x, bgPieceSize.y, true))
                //.append(r -> r.drawRectangle2d(color, position.plus(offsetCD).toAwtPoint(), coloredHealthPieceSize.x, coloredHealthPieceSize.y, true))
                //.append(r -> r.drawRectangle2d(color, position.plus(offsetAB).plus(offsetCD).toAwtPoint(), coloredHealthPieceSize.x, coloredHealthPieceSize.y, true))
                .render(renderer);
    }
}
