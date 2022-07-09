package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.state_machine.State;
import util.tinysound.TinySound;

public class BossIdleTemplatePhase1 implements State {

    private static final int FRAME_AT_WHICH_BOSS_STARTS_TO_MOVE_TO_THE_SIDE = 31;
    static final int FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE = 56;

    private static final double DISTANCE_TO_TRAVEL_IN_THE_AIR = 3000;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;
    public AnimationPlayer animationPlayer;

    public OrientedPosition orientedPosition;

    public BossIdleTemplatePhase1(final OrientedPosition initialOrientedPosition) {
        this.orientedPosition = initialOrientedPosition;
    }

    @Override
    public void start(DataPacket input) {
        this.animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.quadrupedal_idle_2)
                .withRigidity(this::rigidityFunction)
                .withAnimationOffset(this::orientedPositionFunction)
                .withPlaybackSpeed(() -> 50d)
                .withFrameEvent(55, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.12))
                .withFrameEvent(64, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.07))
                .withFrameEvent(104, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.04))
                .withFrameEvent(110, () -> TinySound.loadSound(GameSoundFiles.leg_step_2).play(0.04))
                .withFrameEvent(119, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.04))
                .withFrameEvent(129, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.04))
                .build());

        // Basic interpolation stuff for the movement.
        animationAutomationClip = new LinearNormalizer(FRAME_AT_WHICH_BOSS_STARTS_TO_MOVE_TO_THE_SIDE, FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE);
        animationAutomationClip.isBounded = true;

        final Vector3 humanCarPosition = input.humanCar.position;
        final Vector3 flatHumanCarPosition = humanCarPosition.scaled(1, 1, 0).nonZeroOrElse(new Vector3(2000, 0, 0));
        final Vector3 jumpDestination = flatHumanCarPosition.scaledToMagnitude(-2000);
        final Vector3 badlyScaledJumpDirection = jumpDestination.minus(orientedPosition.position);
        final Vector3 projectedJumpDirection = badlyScaledJumpDirection.scaledToMagnitude(DISTANCE_TO_TRAVEL_IN_THE_AIR);
        final Vector3 bossAnimationPosition = orientedPosition.position;
        final Vector3 bossAnimationDestination = bossAnimationPosition.plus(projectedJumpDirection);
        parameterizedTrajectory = new ParameterizedSegment(bossAnimationPosition, bossAnimationDestination);

        // I just realized this breaks the orientation representation.
        // We should 100% create the "setNoseVector()" setter inside the Orientation class.
        // I'm lazy, so I'm probably going to wait indefinitely before I do it :3
        orientedPosition.orientation.noseVector = projectedJumpDirection.normalized();

        TinySound.init();
        TinySound.loadSound(GameSoundFiles.idle2_sweep).play(0.3);

        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {
        BallStateSetter.setTarget(animationPlayer.getCenterOfMass());
    }

    private OrientedPosition orientedPositionFunction() {
        double valueOfParameterizedAutomation = animationAutomationClip.compute(animationPlayer.getCurrentAnimationFrame());
        orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
        return orientedPosition;
    }

    private Double rigidityFunction(final Integer frameIndex) {
        return 0.2 + frameIndex/180.0;
    }

    @Override
    public void stop(DataPacket input) {
        TinySound.shutdown();
    }

    @Override
    public State next(DataPacket input) {
        if(animationPlayer.isFinished()) {
            return new BossDashAttackPhase1(orientedPosition);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
