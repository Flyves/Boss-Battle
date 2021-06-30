package util.state_machine;

import rlbotexample.input.dynamic_data.DataPacket;

public interface Behaviour {
    void exec(DataPacket input);
}
