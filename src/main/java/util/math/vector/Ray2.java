package util.math.vector;

public class Ray2 {
    public Vector2 offset;
    public Vector2 direction;

    public Ray2() {
        this.offset = new Vector2();
        this.direction = new Vector2();
    }

    public Ray2(Vector2 offset, Vector2 direction) {
        this.offset = offset;
        this.direction = direction;
    }

    public Vector2 findPointFromSpeedAndTimeElapsed(double t, double speed) {
        double maxTime = direction.magnitude()/speed;
        double parameterizedT = t/maxTime;
        return offset.scaled(1-parameterizedT).plus(offset.plus(direction).scaled(parameterizedT));
    }
}
