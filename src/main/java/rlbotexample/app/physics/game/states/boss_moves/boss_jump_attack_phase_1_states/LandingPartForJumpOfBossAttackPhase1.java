package rlbotexample.app.physics.game.states.boss_moves.boss_jump_attack_phase_1_states;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.BossJumpAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class LandingPartForJumpOfBossAttackPhase1 implements State {

    static final int ANIMATION_FRAME_AT_WHICH_THE_BOSS_LANDS = 175;
    private static final int FRAME_AT_WHICH_THE_BOSS_ENDS_ITS_CORRECT_LANDING = 200;

    private final BossJumpAttackPhase1 bossJumpAttackPhase1;

    public LandingPartForJumpOfBossAttackPhase1(BossJumpAttackPhase1 bossJumpAttackPhase1) {
        this.bossJumpAttackPhase1 = bossJumpAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_jump);
        CurrentGame.bossAi.animator.setCurrentFrameIndex(ANIMATION_FRAME_AT_WHICH_THE_BOSS_LANDS);
        CurrentGame.bossAi.animator.isLooping = false;
        bossJumpAttackPhase1.amountOfJumpsDone++;
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
        if(bossJumpAttackPhase1.amountOfJumpsDone >= 3) {
            if(CurrentGame.bossAi.animator.currentFrameIndex()
                    >= StallPartForJumpOfBossAttackPhase1.FRAME_AT_WHICH_THE_BOSS_STALLS_ITSELF) {
                return new StallPartForJumpOfBossAttackPhase1();
            }
        }
        else {
            if(CurrentGame.bossAi.animator.currentFrameIndex() >= FRAME_AT_WHICH_THE_BOSS_ENDS_ITS_CORRECT_LANDING) {
                return new TakeOffPartForJumpOfBossAttackPhase1(bossJumpAttackPhase1);
            }
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
