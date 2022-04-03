package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.sound.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.Vector3;
import util.state_machine.State;
import util.tinysound.TinySound;

public class BossIdle1Phase1 implements State {

    private static final int FRAME_AT_WHICH_BOSS_STARTS_TO_MOVE_TO_THE_SIDE = 31;
    static final int FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE = 56;

    private static final double DISTANCE_TO_TRAVEL_IN_THE_AIR = 3000;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new DiscreteCarGroupAnimator(GameAnimations.quadrupedal_idle_2);
        CurrentGame.bossAi.animator.looping(false);

        animationAutomationClip = new LinearNormalizer(FRAME_AT_WHICH_BOSS_STARTS_TO_MOVE_TO_THE_SIDE, FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE);
        animationAutomationClip.isBounded = true;

        final Vector3 humanCarPosition = input.humanCar.position;
        final Vector3 flatHumanCarPosition = humanCarPosition.scaled(1, 1, 0).nonZeroOrElse(new Vector3(2000, 0, 0));
        final Vector3 jumpDestination = flatHumanCarPosition.scaledToMagnitude(-2000);
        final Vector3 badlyScaledJumpDirection = jumpDestination.minus(CurrentGame.bossAi.orientedPosition.position);
        final Vector3 projectedJumpDirection = badlyScaledJumpDirection.scaledToMagnitude(DISTANCE_TO_TRAVEL_IN_THE_AIR);
        final Vector3 bossAnimationPosition = CurrentGame.bossAi.orientedPosition.position;
        final Vector3 bossAnimationDestination = bossAnimationPosition.plus(projectedJumpDirection);
        parameterizedTrajectory = new ParameterizedSegment(bossAnimationPosition, bossAnimationDestination);

        final Orientation bossAnimationOrientation = CurrentGame.bossAi.orientedPosition.orientation;
        bossAnimationOrientation.noseVector = projectedJumpDirection.normalized();

        TinySound.init();
        TinySound.loadSound(GameSoundFiles.idle2_sweep).play(0.3);
    }

    @Override
    public void exec(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 55) {
            TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.12);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 64) {
            TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.07);
        }

        if(CurrentGame.bossAi.animator.currentFrameIndex() == 104) {
            TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.04);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 110) {
            TinySound.loadSound(GameSoundFiles.leg_step_2).play(0.04);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 119) {
            TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.04);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 129) {
            TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.04);
        }

        BasicRigidityTransitionHandler.handle(CurrentGame.bossAi.animator);
        double valueOfParameterizedAutomation = animationAutomationClip.compute(CurrentGame.bossAi.animator.currentFrameIndex());
        CurrentGame.bossAi.orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
        TinySound.shutdown();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossDashAttackPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
