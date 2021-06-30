package util.math.linear_transform;

import util.math.vector.Vector3;

import java.io.Serializable;

// y = ax + b
// and x = (y - b) / a
public class LinearTransform implements Serializable {

    private final Double[] scalar;
    private final Double[] offset;

    public LinearTransform(Double[] scalar, Double[] offset) {
        if(scalar.length != offset.length) throw new RuntimeException();
        this.scalar = scalar;
        this.offset = offset;
    }

    public LinearTransform(Double[] scalar) {
        this.scalar = scalar;
        this.offset = new Double[scalar.length];
    }

    public Double[] compute(Double[] x) {
        if(x.length != scalar.length) throw new RuntimeException();
        Double[] result = new Double[x.length];

        for(int i = 0; i < x.length; i++) {
            result[i] = x[i]*scalar[i] + offset[i];
        }

        return result;
    }

    public Double[] inverse(Double[] y) {
        if(y.length != scalar.length) throw new RuntimeException();
        Double[] result = new Double[y.length];

        for(int i = 0; i < y.length; i++) {
            result[i] = (y[i]-offset[i]) / scalar[i];
        }

        return result;
    }
}
