package rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.Vector3;
import util.state_machine.State;

public class EndSpinAttack implements State {

    private static final int FRAME_INDEX_AT_WHICH_END_OF_SPIN_ATTACK_BEGINS = 457;
    private static final int FRAME_INDEX_AT_WHICH_DISPLACEMENT_TOWARDS_CENTER_ENDS = 497;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;


    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator.setCurrentFrameIndex(FRAME_INDEX_AT_WHICH_END_OF_SPIN_ATTACK_BEGINS);

        animationAutomationClip = new LinearNormalizer(FRAME_INDEX_AT_WHICH_END_OF_SPIN_ATTACK_BEGINS, FRAME_INDEX_AT_WHICH_DISPLACEMENT_TOWARDS_CENTER_ENDS);
        animationAutomationClip.isBounded = true;

        Vector3 initialAnimationPosition = CurrentGame.bossAi.orientedPosition.position;
        Vector3 finalAnimationPosition = new Vector3();     // center of the map
        parameterizedTrajectory = new ParameterizedSegment(initialAnimationPosition, finalAnimationPosition);
    }

    @Override
    public void exec(DataPacket input) {
        double valueOfParameterizedAutomation = animationAutomationClip.compute(CurrentGame.bossAi.animator.currentFrameIndex());
        valueOfParameterizedAutomation = SpinToPredictedPlayerPosition.parameterizedSmoothedDisplacementFunction(valueOfParameterizedAutomation);
        CurrentGame.bossAi.orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
