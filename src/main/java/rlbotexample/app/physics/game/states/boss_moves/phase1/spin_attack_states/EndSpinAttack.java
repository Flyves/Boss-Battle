package rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossSpinAttackPhase1;
import rlbotexample.asset.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.Vector3;
import util.state_machine.State;
import util.tinysound.TinySound;

public class EndSpinAttack implements State {

    private static final int FRAME_INDEX_AT_WHICH_END_OF_SPIN_ATTACK_BEGINS = 457;
    private static final int FRAME_INDEX_AT_WHICH_DISPLACEMENT_TOWARDS_CENTER_ENDS = 497;
    private final BossSpinAttackPhase1 bossSpinAttackPhase1;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;

    public EndSpinAttack(final BossSpinAttackPhase1 bossSpinAttackPhase1) {
        this.bossSpinAttackPhase1 = bossSpinAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        bossSpinAttackPhase1.animationPlayer.setCurrentAnimationFrame(FRAME_INDEX_AT_WHICH_END_OF_SPIN_ATTACK_BEGINS);

        animationAutomationClip = new LinearNormalizer(FRAME_INDEX_AT_WHICH_END_OF_SPIN_ATTACK_BEGINS, FRAME_INDEX_AT_WHICH_DISPLACEMENT_TOWARDS_CENTER_ENDS);
        animationAutomationClip.isBounded = true;

        Vector3 initialAnimationPosition = bossSpinAttackPhase1.orientedPosition.position;
        Vector3 finalAnimationPosition = new Vector3();     // center of the map
        parameterizedTrajectory = new ParameterizedSegment(initialAnimationPosition, finalAnimationPosition);
        TinySound.loadSound(GameSoundFiles.helicopter_attack).play(0.2);
    }

    @Override
    public void exec(DataPacket input) {
        double valueOfParameterizedAutomation = animationAutomationClip.compute(bossSpinAttackPhase1.animationPlayer.getCurrentAnimationFrame());
        valueOfParameterizedAutomation = SpinToPredictedPlayerPosition.parameterizedSmoothedDisplacementFunction(valueOfParameterizedAutomation);
        bossSpinAttackPhase1.orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
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
