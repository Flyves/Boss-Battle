package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TransitionFromPhase2ToPhase3 implements State {

    private boolean transitionIsDone;

    @Override
    public void exec(DataPacket input) {
        this.transitionIsDone = false;
    }

    @Override
    public State next(DataPacket input) {
        if(transitionIsDone) {
            return new BossPhase3();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
