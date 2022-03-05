package util.shapes;

import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Rectangle2D {
    final Vector3 upperLeft;
    final Vector3 size;

    public Rectangle2D(final Vector3 upperLeft, final Vector3 size) {
        this.upperLeft = upperLeft;
        this.size = size;
    }

    public List<Rectangle2D> findEquivalent1x2rectangles(final Rectangle2D rectangle2D) {
        return new ArrayList<>();
    }
}
