package rlbotexample.app.physics.game.states.boss_moves.boss_jump_attack_phase_1_states;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.BossJumpAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TakeOffPartForJumpOfBossAttackPhase1 implements State {

    private static final int PHYSICAL_FRAME_AT_WHICH_THE_BOSS_STARTS_ASCENDING = 90;
    private static final int ANIMATION_FRAME_AT_WHICH_THE_BOSS_STARTS_ASCENDING = 98;

    private final BossJumpAttackPhase1 bossJumpAttackPhase1;

    public TakeOffPartForJumpOfBossAttackPhase1(BossJumpAttackPhase1 bossJumpAttackPhase1) {
        this.bossJumpAttackPhase1 = bossJumpAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_jump);
        CurrentGame.bossAi.animator.setCurrentFrameIndex(PHYSICAL_FRAME_AT_WHICH_THE_BOSS_STARTS_ASCENDING);
        CurrentGame.bossAi.animator.isLooping = false;
    }

    @Override
    public void exec(DataPacket input) {
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex() >= ANIMATION_FRAME_AT_WHICH_THE_BOSS_STARTS_ASCENDING) {
            return new AirTimePartForJumpOfBossAttackPhase1(bossJumpAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
