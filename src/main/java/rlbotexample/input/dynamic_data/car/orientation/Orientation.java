package rlbotexample.input.dynamic_data.car.orientation;

import util.math.vector.Vector2;
import util.math.vector.Vector3;

public class Orientation {
    public final Vector3 nose;
    public final Vector3 roof;

    public Orientation() {
        this.nose = new Vector3(1, 0, 0);
        this.roof = new Vector3(0, 0, 1);
    }

    public Orientation(Vector3 nose, Vector3 roof) {
        this.nose = nose;
        this.roof = roof;
    }

    public Vector3 getNose() {
        return nose;
    }

    public Vector3 getRoof() {
        return roof;
    }
}
