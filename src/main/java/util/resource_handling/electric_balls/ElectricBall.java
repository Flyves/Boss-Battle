package util.resource_handling.electric_balls;

import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;
import util.renderers.RenderTasks;
import util.renderers.ShapeRenderer;

import java.awt.*;

public class ElectricBall {

    public static final double RADII = 200;
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
        position = position.plus(targetDirection.scaledToMagnitude(findBallSpeed()/RlConstants.BOT_REFRESH_RATE));
        amountOfFramesSpent++;
    }

    private int findBallSpeed() {
        switch (GameOptions.gameDifficulty) {
            case ROCKET_SLEDGE: return 3000;
            case TRIVIAL: return 3000;
            case EASY: return 6000;
            case MEDIUM: return 9000;
            case HARD: return 12000;
            case EXPERT: return 15000;
            case IMPOSSIBLE: return 17000;
            case WTF: return 20000;
        }
        throw new RuntimeException("No game difficulty selected!");
    }

    public void render() {
        RenderTasks.append(r -> {
            ShapeRenderer shapeRenderer = new ShapeRenderer(r);
            shapeRenderer.renderSwerlingSphere(position, RADII, COLOR);
        });
    }
}
