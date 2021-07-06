package rlbotexample.app.physics.game.states.stats_handling.demolition_states;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class WaitForDemolitionRequest implements State {

    @Override
    public void start(DataPacket input) {}

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.playerDemolitionRequest) {
            return new DemolishPlayer();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
