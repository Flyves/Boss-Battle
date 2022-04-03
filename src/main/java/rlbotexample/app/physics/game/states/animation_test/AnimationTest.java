package rlbotexample.app.physics.game.states.animation_test;

import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.continuous_player.AnimationPlayer;
import rlbotexample.asset.animation.continuous_player.AnimationProfileBuilder;
import rlbotexample.asset.animation.continuous_player.AnimationTasks;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.state_machine.State;

public class AnimationTest implements State {
    private AnimationPlayer animationPlayer;

    @Override
    public void start(DataPacket input) {
        animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.boss_transformation_0_To_1)
                .withAnimationOffset(() -> new OrientedPosition(new Vector3(2000, 1000, 1000), new Orientation().rotate(new Vector3(Math.PI/2, 0, 0))))
                .build());
        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(animationPlayer.isFinished()) {
            return new AnimationTest();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
