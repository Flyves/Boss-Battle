package rlbotexample.app.physics.game.states.boss_phase;

import rlbotexample.assets.animations.CarGroupAnimator;
import rlbotexample.assets.animations.GameAnimations;
import rlbotexample.app.graphics.health_bars.BossHealthBar;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

public class TransitionFromPhase0ToPhase1 implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_transformation_0_To_1);
        CurrentGame.bossAi.animator.looping(false);
    }

    @Override
    public void exec(DataPacket input) {
        double bossLife = CurrentGame.bossAi.animator.currentFrameIndex()/(double)CurrentGame.bossAi.animator.meshAnimation.frames.size();
        bossLife = bossLife*333.3333333 + 666.666666;
        if(CurrentGame.bossAi.animator.currentFrameIndex() < 50) {
            bossLife = 670;
        }
        CurrentGame.bossAi.health = (int)bossLife;
        CurrentGame.bossAi.orientedPosition.position = new Vector3();
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {
        BossHealthBar.renderOnScreen(CurrentGame.bossAi.health/(double)CurrentGame.BOSS_INITIAL_HP, input);
    }

}
