package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.game_constants.RlConstants;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.Vector3;
import util.state_machine.State;

public class BossIdle1Phase1 implements State {

    private static final int FRAME_AT_WHICH_BOSS_STARTS_TO_MOVE_TO_THE_SIDE = 31;
    static final int FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE = 56;

    private static final double DISTANCE_TO_TRAVEL_IN_THE_AIR = 3000;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.quadrupedal_idle_2);
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
    }

    @Override
    public void exec(DataPacket input) {
        double valueOfParameterizedAutomation = animationAutomationClip.compute(CurrentGame.bossAi.animator.currentFrameIndex());
        CurrentGame.bossAi.orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossDashAttackPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
