package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TransitionFromPhase1ToPhase2 implements State {

    private boolean transitionIsDone;

    @Override
    public void exec(DataPacket input) {
        this.transitionIsDone = false;
    }

    @Override
    public State next(DataPacket input) {
        if(transitionIsDone) {
            return new BossPhase2();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
