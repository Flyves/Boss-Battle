package util.state_machine;

import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.DataPacket;

public interface State extends Behaviour, Transition, Debuggable {
}
