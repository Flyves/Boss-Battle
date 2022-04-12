package rlbotexample.app.physics.game.states.boss_moves.phase2;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.game_constants.RlConstants;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector2;
import util.math.vector.Vector3;
import util.state_machine.State;

import javax.xml.ws.Holder;

public class InitPhase2 implements State {
    private int amountOfFramesSpend;
    private final AnimationPlayer animationPlayer;
    private final Holder<DataPacket> gameStateHolder;
    private final Holder<Vector2> virtualPosition;
    private final Holder<Vector3> virtualOrientation;
    private final Holder<Vector3> bossOrientation;

    public InitPhase2() {
        amountOfFramesSpend = 0;
        gameStateHolder = new Holder<>();
        virtualPosition = new Holder<>(new Vector2());
        virtualOrientation = new Holder<>(Vector3.Z_VECTOR);
        bossOrientation = new Holder<>(Vector3.Z_VECTOR);
        this.animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.lamp_glados_head)
                .withLooping(true)
                .withRigidity((carId) -> 1d)
                .withAnimationOffset(this::findDestinationOffset)
                .build());
    }

    @Override
    public void start(DataPacket input) {
        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {
        gameStateHolder.value = input;
        BasicRigidityTransitionHandler.handle(CurrentGame.bossAi.animator, 0.05, 5000, amountOfFramesSpend);
        BallStateSetter.setTarget(animationPlayer.getCenterOfMass());
        amountOfFramesSpend++;
    }

    private OrientedPosition findDestinationOffset() {
        final DataPacket input = gameStateHolder.value;
        final Vector2 playerPosition = input.humanCar.position.flatten();
        final Vector2 playerVelocity = input.humanCar.velocity.flatten();
        final Vector2 bossPosition = animationPlayer.getCenterOfMass().flatten();
        final Vector2 destination = playerPosition.scaled(-1).scaledToMagnitude(4000).plus(playerPosition).plus(playerVelocity.scaled(0.5));
        final double convergenceRate = 0.01;
        final double maxSpeed = 1600;
        virtualPosition.value = virtualPosition.value.plus(destination.minus(virtualPosition.value).scaledToMagnitude(maxSpeed*RlConstants.BOT_REFRESH_TIME_PERIOD));
        Vector2 nextFlatPosition = virtualPosition.value.minus(bossPosition).scaled(convergenceRate).plus(bossPosition);
        final Vector3 destinationOnCeiling = new Vector3(nextFlatPosition, 1300);
        final Vector3 desiredRotator = animationPlayer.getCenterOfMass().minus(input.humanCar.position).findRotator(Vector3.Z_VECTOR).scaled(-1);
        final double orientationConvergenceRate = 0.05;
        final double maxOrientationSpeed = 1.5;
        virtualOrientation.value = virtualOrientation.value.plus(desiredRotator.minus(virtualOrientation.value).scaledToMagnitude(maxOrientationSpeed*RlConstants.BOT_REFRESH_TIME_PERIOD));
        bossOrientation.value = virtualOrientation.value.minus(bossOrientation.value).scaled(orientationConvergenceRate).plus(bossOrientation.value);
        return new OrientedPosition(destinationOnCeiling, new Orientation().rotate(bossOrientation.value));
    }

    @Override
    public void stop(DataPacket input) {
    }

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
