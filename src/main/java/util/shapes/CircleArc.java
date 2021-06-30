package util.shapes;

import util.math.vector.Ray2;
import util.math.vector.Vector2;

public class CircleArc {

    public Circle circle;
    public double rad0;
    public double rad1;

    public CircleArc(Circle circle, double rad0, double rad1) {
        this.circle = circle;
        this.rad0 = rad0;
        this.rad1 = rad1;
    }

    public double length() {
        return circle.radii * Math.abs(rad0 - rad1);
    }

    public Vector2 findPointFromSpeedAndTimeElapsed(double t, double speed) {
        double maxTime = length()/speed;
        double parameterizedT = t/maxTime;
        double resultingRads = (1-parameterizedT)*rad0 + parameterizedT*rad1;
        return circle.findPointOnCircle(resultingRads);
    }
}
