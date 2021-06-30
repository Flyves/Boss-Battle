package util.state_machine;

import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.DataPacket;

public interface Debuggable {
    void debug(DataPacket input, Renderer renderer);
}
