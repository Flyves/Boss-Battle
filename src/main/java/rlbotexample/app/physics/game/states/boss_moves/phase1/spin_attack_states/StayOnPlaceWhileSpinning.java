package rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossSpinAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class StayOnPlaceWhileSpinning implements State {

    private static final int FRAME_INDEX_AT_WHICH_WAIT_ON_PLACE_BEGINS = 177;
    private static final int FRAME_INDEX_AT_WHICH_WAIT_ON_PLACE_ENDS = 357 - 40*5;

    private final BossSpinAttackPhase1 bossSpinAttackPhase1;

    public StayOnPlaceWhileSpinning(BossSpinAttackPhase1 bossSpinAttackPhase1) {
        this.bossSpinAttackPhase1 = bossSpinAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        bossSpinAttackPhase1.animationPlayer.setCurrentAnimationFrame(FRAME_INDEX_AT_WHICH_WAIT_ON_PLACE_BEGINS);
    }

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(bossSpinAttackPhase1.animationPlayer.getCurrentAnimationFrame() >= FRAME_INDEX_AT_WHICH_WAIT_ON_PLACE_ENDS) {
            if(bossSpinAttackPhase1.amountOfTimesAttackOccurred >= 3) {
                return new EndSpinAttack(bossSpinAttackPhase1);
            }
            return new SpinToPredictedPlayerPosition(bossSpinAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
