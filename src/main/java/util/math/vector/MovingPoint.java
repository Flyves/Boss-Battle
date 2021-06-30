package util.math.vector;

public class MovingPoint {
    public Ray3 physicsState;
    public double time;

    public MovingPoint() {
        this(new Ray3(), 0);
    }

    public MovingPoint(Ray3 currentState, double time) {
        this.physicsState = currentState;
        this.time = time;
    }
}
