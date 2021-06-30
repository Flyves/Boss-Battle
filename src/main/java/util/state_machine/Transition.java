package util.state_machine;

import rlbotexample.input.dynamic_data.DataPacket;

public interface Transition {
    State next(DataPacket input);
}
