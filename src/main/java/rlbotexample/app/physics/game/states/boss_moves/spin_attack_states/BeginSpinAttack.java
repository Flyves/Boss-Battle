package rlbotexample.app.physics.game.states.boss_moves.spin_attack_states;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.BossSpinAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class BeginSpinAttack implements State {

    private static final int FRAME_INDEX_AT_WHICH_ANIMATION_STARTS = 0;

    private final BossSpinAttackPhase1 bossSpinAttackPhase1;

    public BeginSpinAttack(BossSpinAttackPhase1 bossSpinAttackPhase1) {
        this.bossSpinAttackPhase1 = bossSpinAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator.setCurrentFrameIndex(FRAME_INDEX_AT_WHICH_ANIMATION_STARTS);
    }

    @Override
    public void exec(DataPacket input) {
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex() >= SpinToPredictedPlayerPosition.FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER) {
            return new SpinToPredictedPlayerPosition(bossSpinAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
