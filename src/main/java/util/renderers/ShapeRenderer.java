package util.renderers;

import rlbot.render.Renderer;
import rlbotexample.physics_prediction.Trajectory3D;
import util.math.vector.*;
import util.shapes.Circle;
import util.shapes.Circle3D;
import util.shapes.Triangle3D;

import java.awt.*;
import java.util.function.Function;

public class ShapeRenderer {

    private final Renderer renderer;

    public ShapeRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void renderCross(Vector3 position, Color color) {
        renderer.drawLine3d(color, position.plus(new Vector3(20, 20, 20)).toFlatVector(), position.plus(new Vector3(-20, -20, -20)).toFlatVector());
        renderer.drawLine3d(color, position.plus(new Vector3(-20, 20, 20)).toFlatVector(), position.plus(new Vector3(20, -20, -20)).toFlatVector());
        renderer.drawLine3d(color, position.plus(new Vector3(20, -20, 20)).toFlatVector(), position.plus(new Vector3(-20, 20, -20)).toFlatVector());
        renderer.drawLine3d(color, position.plus(new Vector3(20, 20, -20)).toFlatVector(), position.plus(new Vector3(-20, -20, 20)).toFlatVector());
    }

    public void renderRay3(Ray3 ray, Color color) {
        renderer.drawLine3d(color, ray.offset.toFlatVector(), ray.offset.plus(ray.direction).toFlatVector());
    }

    public void renderOrientedPosition(Color color, CarOrientedPosition orientedPosition) {
        renderer.drawLine3d(
                color,
                orientedPosition.position.toFlatVector(),
                orientedPosition.position.plus(orientedPosition.orientation.noseVector.scaledToMagnitude(300))
                        .toFlatVector());
        renderer.drawLine3d(
                color,
                orientedPosition.position.toFlatVector(),
                orientedPosition.position.plus(orientedPosition.orientation.roofVector.scaledToMagnitude(300))
                        .toFlatVector());
    }

    public void renderOrientedPosition(Color color, ZyxOrientedPosition orientedPosition) {
        renderOrientedPosition(color, orientedPosition.toCarOrientedPosition());
    }

    public void renderTriangle(Triangle3D triangle, Color color) {
        renderer.drawLine3d(color, triangle.point0.toFlatVector(), triangle.point1.toFlatVector());
        renderer.drawLine3d(color, triangle.point1.toFlatVector(), triangle.point2.toFlatVector());
        renderer.drawLine3d(color, triangle.point2.toFlatVector(), triangle.point0.toFlatVector());
    }

    public void renderCircle(Circle circle, double zOffset, Color color) {
        int amountOfPoints = 100;
        double precision = Math.PI*2/amountOfPoints;
        Vector2 point = circle.findPointOnCircle(0);
        Vector2 previousPoint;

        for(int i = 1; i < amountOfPoints; i++) {
            previousPoint = point;
            point = circle.findPointOnCircle(i*precision);
            renderer.drawLine3d(color, new Vector3(previousPoint, zOffset).toFlatVector(), new Vector3(point, zOffset).toFlatVector());
        }
    }

    public void renderCircle3D(Circle3D circle, Color color) {
        int amountOfPoints = 100;
        double precision = Math.PI*2/amountOfPoints;
        Vector3 point = circle.findPointOnCircle(0);
        Vector3 previousPoint;

        for(int i = 1; i <= amountOfPoints; i++) {
            previousPoint = point;
            point = circle.findPointOnCircle(i*precision);
            renderer.drawLine3d(color, previousPoint.toFlatVector(), point.toFlatVector());
        }
    }

    public void renderTrajectory(Trajectory3D parabola, double amountOfTimeToRender, Color color) {
        Vector3 previousPosition = parabola.apply(0.0);
        for(int i = 1; i < 200; i++) {
            Vector3 nextPosition = parabola.apply(i*amountOfTimeToRender/600);
            if(nextPosition != null && previousPosition != null) {
                renderer.drawLine3d(color, nextPosition.toFlatVector(), previousPosition.toFlatVector());
            }
            previousPosition = nextPosition;
        }
    }

    public void renderTrajectory(Function<Double, Vector3> parabola, double amountOfTimeToRender, Color color) {
        Vector3 previousPosition = parabola.apply(0.0);
        for(int i = 1; i < 200; i++) {
            Vector3 nextPosition = parabola.apply(i*amountOfTimeToRender/600);
            if(nextPosition != null && previousPosition != null) {
                renderer.drawLine3d(color, nextPosition.toFlatVector(), previousPosition.toFlatVector());
            }
            previousPosition = nextPosition;
        }
    }

    public void renderTrajectory(Trajectory3D parabola, double amountOfTimeToRender, Color fromColor, Color toColor) {
        Vector3 previousPosition = parabola.apply(0.0);
        for(int i = 1; i < 200; i++) {
            Color color = new Color((int)(fromColor.getRed()*(1-(i/200.0)) + toColor.getRed()*(i/200.0)),
                    (int)(fromColor.getGreen()*(1-(i/200.0)) + toColor.getGreen()*(i/200.0)),
                    (int)(fromColor.getBlue()*(1-(i/200.0)) + toColor.getBlue()*(i/200.0)));

            Vector3 nextPosition = parabola.apply(i*amountOfTimeToRender/200);
            if(nextPosition != null && previousPosition != null) {
                renderer.drawLine3d(color, nextPosition.toFlatVector(), previousPosition.toFlatVector());
            }
            previousPosition = nextPosition;
        }
    }

    public void renderTrajectory(Trajectory3D parabola, double fromTime, double toTime, Color color) {
        Vector3 previousPosition = parabola.apply(fromTime);
        for(int i = 1; i < 40; i++) {
            double timeToCompute = fromTime + ((i/40.0)*(toTime-fromTime));
            Vector3 nextPosition = parabola.apply(timeToCompute);
            renderer.drawLine3d(color, nextPosition.toFlatVector(), previousPosition.toFlatVector());
            previousPosition = nextPosition;
        }
    }
}