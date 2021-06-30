package util.math.linear_transform;

import util.math.vector.Vector2;

import java.io.Serializable;

// y = ax + b
// and x = (y - b) / a
public class LinearTransform2 implements Serializable {

    private final Vector2 scalar;
    private final Vector2 offset;

    public LinearTransform2(Vector2 scalar, Vector2 offset) {
        this.scalar = scalar;
        this.offset = offset;
    }

    public Vector2 compute(Vector2 x) {
        return scalar.scaled(x).plus(offset);
    }

    public Vector2 inverse(Vector2 y) {
        return y.minus(offset).scaled(scalar.inverse());
    }
}
