package rlbotexample.input.dynamic_data.car.hit_box.wheels;

import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import util.math.vector.Ray3;
import util.math.vector.Vector3;
import util.renderers.ShapeRenderer;
import util.shapes.Circle3D;
import util.shapes.Plane3D;

import java.awt.*;

public class WheelBox {

    private final Circle3D frontRightWheel;
    private final Circle3D frontLeftWheel;
    private final Circle3D backRightWheel;
    private final Circle3D backLeftWheel;

    private final Vector3 position;
    private final CarOrientation orientation;

    public WheelBox(Circle3D frontRightWheel, Circle3D frontLeftWheel, Circle3D backRightWheel, Circle3D backLeftWheel, Vector3 position, CarOrientation orientation) {
        this.frontRightWheel = frontRightWheel;
        this.frontLeftWheel = frontLeftWheel;
        this.backRightWheel = backRightWheel;
        this.backLeftWheel = backLeftWheel;

        this.position = position;
        this.orientation = orientation;
    }

    /*
    public boolean isColliding(Ray3 normal) {
        Ray3 localNormal = globalToLocal(normal);
        Plane3D localPlane = new Plane3D(localNormal);
        Vector3 pFR = frontRightWheel.findClosestPointFromNonIntersecting(localPlane);
        Vector3 pFL = frontLeftWheel.findClosestPointFromNonIntersecting(localPlane);
        Vector3 pBR = backRightWheel.findClosestPointFromNonIntersecting(localPlane);
        Vector3 pBL = backLeftWheel.findClosestPointFromNonIntersecting(localPlane);

        return false;
    }*/

    public void render(Renderer renderer, Color color) {
        ShapeRenderer shapeRenderer = new ShapeRenderer(renderer);
        Circle3D realWheelPositionFR = localToGlobal(frontRightWheel);
        Circle3D realWheelPositionFL = localToGlobal(frontLeftWheel);
        Circle3D realWheelPositionBR = localToGlobal(backRightWheel);
        Circle3D realWheelPositionBL = localToGlobal(backLeftWheel);

        shapeRenderer.renderCircle3D(realWheelPositionFR, color);
        shapeRenderer.renderCircle3D(realWheelPositionFL, color);
        shapeRenderer.renderCircle3D(realWheelPositionBR, color);
        shapeRenderer.renderCircle3D(realWheelPositionBL, color);
    }

    public Circle3D localToGlobal(Circle3D circle) {
        return new Circle3D(
                new Ray3(
                        circle.center.offset.matrixRotation(orientation).plus(position),
                        circle.center.direction.matrixRotation(orientation)),
                circle.radii);
    }

    public Ray3 globalToLocal(Ray3 ray) {
        return new Ray3(
                ray.offset.minus(position).toFrameOfReference(orientation),
                ray.direction.toFrameOfReference(orientation));
    }
}
