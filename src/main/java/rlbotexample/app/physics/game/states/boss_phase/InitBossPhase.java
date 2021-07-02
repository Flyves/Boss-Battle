package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class InitBossPhase implements State {

    @Override
    public void exec(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        return new TransitionFromPhase0ToPhase1();
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
