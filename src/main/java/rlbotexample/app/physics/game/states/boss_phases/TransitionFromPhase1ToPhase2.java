package rlbotexample.app.physics.game.states.boss_phases;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.OrientedPosition;
import util.state_machine.State;

public class TransitionFromPhase1ToPhase2 implements State {

    private AnimationPlayer animationPlayer;

    @Override
    public void start(DataPacket input) {
        this.animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.boss_transformation_1_To_2)
                .withAnimationOffset(OrientedPosition::new)
                .build());
        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(animationPlayer.getCurrentAnimationFrame() >= 300 || animationPlayer.isFinished()) {
            AnimationTasks.clearAll();
            return new BossPhase2();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
