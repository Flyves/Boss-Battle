package util.resource_handling.electric_balls;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;
import util.renderers.IndexedRenderer;
import util.renderers.RenderTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElectricBallsResourceHandler {

    private static final List<ElectricBall> currentlyUsedElectricBalls = new ArrayList<>();

    public static void allocAt(Vector3 initialPosition, ExtendedCarData target) {
        final ElectricBall electricBall = new ElectricBall(initialPosition, target);
        currentlyUsedElectricBalls.add(electricBall);
    }

    public static void updateElectricBalls(DataPacket input) {
        final List<ElectricBall> livingBalls = currentlyUsedElectricBalls.stream()
                .filter(electricBall -> electricBall.amountOfFramesSpent < ElectricBall.LIFE_SPAN_IN_FRAMES)
                .collect(Collectors.toList());
        currentlyUsedElectricBalls.clear();
        currentlyUsedElectricBalls.addAll(livingBalls);

        currentlyUsedElectricBalls.forEach(ElectricBall::updatePosition);

        final Optional<ElectricBall> electricBallCollisionOpt = currentlyUsedElectricBalls.stream()
                .filter(electricBall -> input.humanCar.hitBox.isCollidingWith(electricBall))
                .findFirst();

        electricBallCollisionOpt.ifPresent(electricBall -> CurrentGame.humanPlayer.takeDamage(input, 2, input.humanCar.position.minus(electricBall.position).normalized().plus(new Vector3(0, 0, 0.5)).scaledToMagnitude(700)));
        electricBallCollisionOpt.ifPresent(currentlyUsedElectricBalls::remove);
    }

    public static void renderElectricBalls(DataPacket input) {
        currentlyUsedElectricBalls.forEach(electricBall -> {
            electricBall.render(input);
        });
    }
}
