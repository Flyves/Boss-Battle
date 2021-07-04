package util.resource_handling.electric_balls;

import com.sun.jmx.remote.internal.ArrayQueue;
import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;
import util.renderers.ShapeRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ElectricBall {

    public static final double RADII = 150;
    public static final double MAX_SPEED = 16000;
    public static final double LIFE_SPAN_IN_FRAMES = 0.5*60;
    public static final Color COLOR = new Color(4, 228, 248);

    public Vector3 position;
    public Vector3 targetDirection;
    public int amountOfFramesSpent;

    public ElectricBall(Vector3 initialPosition, ExtendedCarData target) {
        this.position = initialPosition;
        this.targetDirection = target.position.minus(position).normalized().plus(Vector3.generateRandomVector().scaled(0.05));
        this.amountOfFramesSpent = 0;
    }

    public void updatePosition() {
        position = position.plus(targetDirection.scaledToMagnitude(MAX_SPEED/RlConstants.BOT_REFRESH_RATE));
        amountOfFramesSpent++;
    }

    public void render(Renderer renderer) {
        ShapeRenderer shapeRenderer = new ShapeRenderer(renderer);
        shapeRenderer.renderSwerlingSphere(position, RADII, COLOR);
    }
}
