package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import rlbot.manager.BotLoopRenderer;
import rlbot.render.Renderer;
import rlbotexample.bot.BotBehaviour;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.output.BotOutput;
import rlbotexample.output.ControlsOutput;

public class SampleBot implements Bot {

    private final int playerIndex;
    private BotOutput botOutput;
    private final BotBehaviour botBehaviour;
    private final Renderer renderer;
    public static double averageFps;
    private long currentFpsTime;
    private long previousFpsTime;
    private long time1;
    private long time2;
    private long deltaTime;
    public static double currentFps;

    public SampleBot(int playerIndex, BotBehaviour botBehaviour) {
        this.playerIndex = playerIndex;
        this.botOutput = new BotOutput();
        this.botBehaviour = botBehaviour;
        this.renderer = getRenderer();
        averageFps = 0;
        this.currentFpsTime = 0;
        this.previousFpsTime = 0;
        this.time1 = 0;
        this.time2 = 0;
        this.deltaTime = 0;
        currentFps = 0;

    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the getNativeBallPrediction.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput(DataPacket input, GameTickPacket packet) {

        processDefaultInputs(input);

        // Bot behaviour
        botOutput = botBehaviour.processInput(input, packet);

        // just some debug calculations all the way down to the return...
        previousFpsTime = currentFpsTime;
        currentFpsTime = System.currentTimeMillis();

        if(currentFpsTime - previousFpsTime == 0) {
            currentFpsTime++;
        }
        currentFps = 1.0 / ((currentFpsTime - previousFpsTime) / 1000.0);
        averageFps = (averageFps*29 + (currentFps)) / 30.0;

        botBehaviour.updateGui(renderer, input, currentFps, averageFps, deltaTime);

        // Output the calculated states
        return botOutput.getForwardedOutput();
    }

    private void processDefaultInputs(DataPacket input) {
    }

    private Renderer getRenderer() {
        return BotLoopRenderer.forBotLoop(this);
    }

    @Override
    public int getIndex() {
        return this.playerIndex;
    }

    /**
     * This is the most important function. It will automatically get called by the framework with fresh data
     * every frame. Respond with appropriate controls!
     */
    @Override
    public ControllerState processInput(GameTickPacket packet) {
        // timestamp before executing the bot
        time1 = System.currentTimeMillis();

        if (packet.playersLength() <= playerIndex || packet.ball() == null || !packet.gameInfo().isRoundActive()) {
            // Just return immediately if something looks wrong with the data. This helps us avoid stack traces.
            return new ControlsOutput();
        }

        // Translate the raw packet data (which is in an unpleasant format) into our custom DataPacket class.
        // The DataPacket might not include everything from GameTickPacket, so improve it if you need to!
        DataPacket dataPacket = new DataPacket(packet, playerIndex);

        // if the bot running the thread does not correspond to THE bot
        // that we want to use, stop its execution as soon as possible.
        if(DataPacket.indexOfBotThatLoadsData.get() != dataPacket.car.playerIndex) {
            return new ControlsOutput();
        }

        // Do the actual logic using our dataPacket.
        ControlsOutput controlsOutput = processInput(dataPacket, packet);

        // timestamp after executing the bot
        time2 = System.currentTimeMillis();

        deltaTime = time2 - time1;
        return controlsOutput;
    }

    public void retire() {
        System.out.println("Retiring bot " + playerIndex);
        renderer.eraseFromScreen();
    }
}
