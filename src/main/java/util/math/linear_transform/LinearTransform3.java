package util.math.linear_transform;

import util.math.vector.Vector3;

import java.io.Serializable;

// y = ax + b
// and x = (y - b) / a
public class LinearTransform3 implements Serializable {

    private final Vector3 scalar;
    private final Vector3 offset;

    public LinearTransform3(Vector3 scalar, Vector3 offset) {
        this.scalar = scalar;
        this.offset = offset;
    }

    public Vector3 compute(Vector3 x) {
        return scalar.scaled(x).plus(offset);
    }

    public Vector3 inverse(Vector3 y) {
        return y.minus(offset).scaled(scalar.inverse());
    }
}
