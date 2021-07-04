package rlbotexample.app.physics.game.states.boss_moves.boss_jump_attack_phase_1_states;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class StallPartForJumpOfBossAttackPhase1 implements State {

    static final int FRAME_AT_WHICH_THE_BOSS_STALLS_ITSELF = 185;
    static final int AMOUNT_OF_FRAMES_TO_STALL_ITSELF = 10*60;

    private int amountOfFramesStalled = 0;

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_jump);
        CurrentGame.bossAi.animator.setCurrentFrameIndex(FRAME_AT_WHICH_THE_BOSS_STALLS_ITSELF);
        CurrentGame.bossAi.animator.isLooping = false;
    }

    @Override
    public void exec(DataPacket input) {
        amountOfFramesStalled++;
        if(amountOfFramesStalled >= AMOUNT_OF_FRAMES_TO_STALL_ITSELF) {
            CurrentGame.bossAi.step(input);
        }
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
