package rlbotexample.bot;

import rlbot.flat.GameTickPacket;
import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.output.BotOutput;

public abstract class BotBehaviour {

    private BotOutput botOutput;

    public BotBehaviour() {
        botOutput = new BotOutput();
    }

    public BotOutput output() {
        return botOutput;
    }

    public void setOutput(BotOutput output) {
        botOutput.throttle(output.throttle());
        botOutput.steer(output.steer());
        botOutput.pitch(output.pitch());
        botOutput.yaw(output.yaw());
        botOutput.roll(output.roll());
        botOutput.jump(output.jump());
        botOutput.boost(output.boost());
        botOutput.drift(output.drift());
    }

    public abstract BotOutput processInput(DataPacket input, GameTickPacket packet);

    public abstract void updateGui(Renderer renderer, DataPacket input, double currentFps, double averageFps, long botExecutionTime);
}
