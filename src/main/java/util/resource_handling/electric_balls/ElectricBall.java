package util.resource_handling.electric_balls;

import rlbot.render.NamedRenderer;
import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;
import util.renderers.ShapeRenderer;

import java.awt.*;

public class ElectricBall {

    public static final double RADII = 200;
    public static final double MAX_SPEED = 14000;
    public static final double LIFE_SPAN_IN_FRAMES = 0.5*60;
    public static final Color COLOR = new Color(4, 228, 248);

    private static int amountOfGeneratedElectricBallsInTotal = 0;

    public Vector3 position;
    public Vector3 targetDirection;
    public int amountOfFramesSpent;

    public ElectricBall(Vector3 initialPosition, ExtendedCarData target) {
        this.position = initialPosition;
        this.targetDirection = target.position.minus(position).normalized().plus(Vector3.generateRandomVector().scaled(0.05));
        this.amountOfFramesSpent = 0;

        amountOfGeneratedElectricBallsInTotal++;
    }

    public void updatePosition() {
        position = position.plus(targetDirection.scaledToMagnitude(MAX_SPEED/RlConstants.BOT_REFRESH_RATE));
        amountOfFramesSpent++;
    }

    public void render(DataPacket input) {
        RenderTasks.append(r -> {
            ShapeRenderer shapeRenderer = new ShapeRenderer(r);
            shapeRenderer.renderSwerlingSphere(position, RADII, COLOR);
        });
    }
}
