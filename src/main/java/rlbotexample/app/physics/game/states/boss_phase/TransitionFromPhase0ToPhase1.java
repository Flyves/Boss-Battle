package rlbotexample.app.physics.game.states.boss_phase;

import rlbotexample.asset.animation.discrete_interpolator.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.app.graphics.health_bars.BossHealthBar;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.sound.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;
import util.tinysound.TinySound;

public class TransitionFromPhase0ToPhase1 implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new DiscreteCarGroupAnimator(GameAnimations.boss_transformation_0_To_1);
        CurrentGame.bossAi.animator.looping(false);
        TinySound.init();
    }

    @Override
    public void exec(DataPacket input) {
        // sounds
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 0) {
            TinySound.loadSound(GameSoundFiles.bootup_head).play(0.1);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 464) {
            TinySound.loadSound(GameSoundFiles.bootup_tail).play(0.1);
        }

        if(CurrentGame.bossAi.animator.currentFrameIndex() == 484) {
            TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.05);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 510) {
            TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.08);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 525) {
            TinySound.loadSound(GameSoundFiles.leg_step_4).play(0.12);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 543) {
            TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.08);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 555) {
            TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.1);
        }
        if(CurrentGame.bossAi.animator.currentFrameIndex() == 573) {
            TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.12);
        }

        // life bar loading (sadly prevents the player from dealing damage in that phase)
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
        CurrentGame.bossAi.health++;
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
