package rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossSpinAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class BeginSpinAttack implements State {

    private static final int FRAME_INDEX_AT_WHICH_ANIMATION_STARTS = 0;

    private final BossSpinAttackPhase1 bossSpinAttackPhase1;

    public BeginSpinAttack(final BossSpinAttackPhase1 bossSpinAttackPhase1) {
        this.bossSpinAttackPhase1 = bossSpinAttackPhase1;
    }

    @Override
    public void start(final DataPacket input) {
        bossSpinAttackPhase1.animationPlayer.setCurrentAnimationFrame(FRAME_INDEX_AT_WHICH_ANIMATION_STARTS);
    }

    @Override
    public void exec(final DataPacket input) {}

    @Override
    public void stop(final DataPacket input) {}

    @Override
    public State next(final DataPacket input) {
        if(bossSpinAttackPhase1.animationPlayer.getCurrentAnimationFrame() >=
                SpinToPredictedPlayerPosition.FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER) {
            return new SpinToPredictedPlayerPosition(bossSpinAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(final DataPacket input) {}
}
