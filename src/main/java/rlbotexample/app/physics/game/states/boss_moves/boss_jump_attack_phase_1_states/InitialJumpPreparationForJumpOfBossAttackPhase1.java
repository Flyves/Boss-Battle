package rlbotexample.app.physics.game.states.boss_moves.boss_jump_attack_phase_1_states;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.BossJumpAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class InitialJumpPreparationForJumpOfBossAttackPhase1 implements State {

    private final BossJumpAttackPhase1 bossJumpAttackPhase1;

    public InitialJumpPreparationForJumpOfBossAttackPhase1(BossJumpAttackPhase1 bossJumpAttackPhase1) {
        this.bossJumpAttackPhase1 = bossJumpAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_jump);
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
        if(CurrentGame.bossAi.animator.currentFrameIndex() >= 90) {
            return new TakeOffPartForJumpOfBossAttackPhase1(bossJumpAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
