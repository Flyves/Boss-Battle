package rlbotexample.bot.implementation.physics.game.states;

import rlbot.render.Renderer;
import rlbotexample.bot.implementation.physics.game.CurrentGame;
import rlbotexample.input.dynamic_data.DataPacket;
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
