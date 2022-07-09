package rlbotexample.app.physics.game.states.boss_phases;

import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.OrientedPosition;
import util.state_machine.State;
import util.tinysound.TinySound;

public class TransitionFromPhase0ToPhase1 implements State {

    private AnimationPlayer animationPlayer;

    @Override
    public void start(DataPacket input) {
        this.animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.boss_transformation_0_To_1)
                .withRigidity((frameIndex) -> 1d)
                .withAnimationOffset(OrientedPosition::new)
                // Head and tail.
                .withFrameEvent(0, () -> TinySound.loadSound(GameSoundFiles.bootup_head).play(0.1))
                .withFrameEvent(464, () -> TinySound.loadSound(GameSoundFiles.bootup_tail).play(0.1))

                // Steps.
                .withFrameEvent(484, () -> TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.05))
                .withFrameEvent(510, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.08))
                .withFrameEvent(525, () -> TinySound.loadSound(GameSoundFiles.leg_step_4).play(0.12))
                .withFrameEvent(543, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.08))
                .withFrameEvent(555, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.1))
                .withFrameEvent(573, () -> TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.12))
                .build());
        TinySound.init();

        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {
        // life bar loading (sadly prevents the player from dealing damage in that phase)
        double bossLife = animationPlayer.getCurrentAnimationFrame()/(double)animationPlayer.getAnimationLength();
        bossLife = bossLife*CurrentGame.BOSS_INITIAL_HP;
        if(animationPlayer.getCurrentAnimationFrame() < 50) {
            bossLife = 1;
        }
        CurrentGame.bossAi.health = (int)bossLife;
        BallStateSetter.setTarget(animationPlayer.getCenterOfMass());
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(animationPlayer.isFinished()) {
            return new BossPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}

}
