package rlbotexample.app.physics.game.states.animation_test;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class AnimationTest implements State {

    @Override
    public void exec(DataPacket input) {
        CurrentGame.bossAi.step(input);
    }

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
