package god_damn_it;

import util.math.vector.Vector2;
import util.shapes.Rectangle2D;

public class Recttttt {
    public static void main(String[] args) {
        final Rectangle2D rectangle2D = new Rectangle2D(new Vector2(), new Vector2(10001, 10000));
        rectangle2D.decomposeIntoSmallerRectangles(0.5)
                .forEach(r -> System.out.println(r.size));
    }
}
