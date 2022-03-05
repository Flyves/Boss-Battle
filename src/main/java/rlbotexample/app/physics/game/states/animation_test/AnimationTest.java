package rlbotexample.app.physics.game.states.animation_test;

import rlbotexample.assets.animations.CarGroupAnimator;
import rlbotexample.assets.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class AnimationTest implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.close();
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_idk);
        CurrentGame.bossAi.animator.carsRigidity = 0.998;
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
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
