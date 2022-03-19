package rlbotexample.app.graphics.health_bars;

import rlbotexample.app.graphics.ScreenSize;
import util.math.vector.Vector2;
import util.math.vector.Vector2Int;
import util.math.vector.Vector3;
import util.renderers.IndexedRenderer;
import util.renderers.RenderTasks;
import util.shapes.Rectangle2D;

import java.awt.*;

public class HealthBarSegment {

    // see the naming dimensions gimp file for reference

    private static final Vector2 offsetAB = new Vector2(8, -16).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 offsetCD = new Vector2(8, 8).scaled(ScreenSize.FULL_HD_RATIO);
    private static final Vector2 widthHeightXY = new Vector2(315, 16).scaled(ScreenSize.FULL_HD_RATIO);

    public static void printOnScreen(Vector2 position, double hpRatio, Color color) {
        if(hpRatio > 1) {
            hpRatio = 1;
        }
        else if(hpRatio < 0) {
            hpRatio = 0;
        }

        final Color bgColor = new Color(14, 14, 14, 127);
        final Vector2Int bgPieceSize = widthHeightXY.plus(offsetCD.scaled(2)).toVector2Int();
        final Vector2Int coloredHealthPieceSize = new Vector2Int((int)(widthHeightXY.x * hpRatio), (int)widthHeightXY.y);

        final Rectangle2D bg1 = new Rectangle2D(position, bgPieceSize.toVector2());
        final Rectangle2D bg2 = new Rectangle2D(position.plus(offsetAB), bgPieceSize.toVector2());
        final Rectangle2D fg1 = new Rectangle2D(position.plus(offsetCD), coloredHealthPieceSize.toVector2());
        final Rectangle2D fg2 = new Rectangle2D(position.plus(offsetAB).plus(offsetCD), coloredHealthPieceSize.toVector2());

        render(bg1, bgColor);
        render(bg2, bgColor);
        render(fg1, color);
        render(fg2, color);
    }

    private static void render(final Rectangle2D rectangle, final Color color) {
        try {
            rectangle.decomposeIntoSmallerRectangles(0.5)
                    .stream()
                    .filter(r -> r.area() > 2)
                    .forEach(r -> r.render(color));
        }
        catch (final Exception ignored) {}
    }
}
