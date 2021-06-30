package util.state_machine;

import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.DataPacket;

public class StateMachine implements Behaviour, Debuggable {
    private State state;
    private State nextState;

    public StateMachine(State initState) {
        nextState = initState;
    }

    public void exec(DataPacket input) {
        state = nextState;
        state.exec(input);
        nextState = state.next(input);
    }

    public void debug(DataPacket input, Renderer renderer) {
        state.debug(input, renderer);
    }
}
