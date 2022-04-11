package rlbotexample.app.physics.game.states.boss_phases;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TransitionFromPhase1ToPhase2 implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new DiscreteCarGroupAnimator(GameAnimations.boss_transformation_1_To_2);
        CurrentGame.bossAi.animator.looping(false);
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
        if(CurrentGame.bossAi.animator.currentFrameIndex() >= 300 || CurrentGame.bossAi.animator.isFinished()) {
            return new BossPhase2();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
