package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import rlbot.manager.BotLoopRenderer;
import rlbot.manager.BotManager;
import rlbot.render.Renderer;
import rlbotexample.animations.GameAnimations;
import rlbotexample.generic_bot.BotBehaviour;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.generic_bot.output.BotOutput;
import rlbotexample.generic_bot.output.ControlsOutput;
import util.renderers.RenderTasks;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
    public BotManager botManager;

    private final AtomicReference<Optional<DataPacket>> previousDataPacketOptRef;

    public SampleBot(int playerIndex, BotBehaviour botBehaviour, BotManager botManager) {
        this.botManager = botManager;
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

        this.previousDataPacketOptRef = new AtomicReference<>(Optional.empty());
    }

    public Renderer getRenderer() {
        return SingleBotLoopRenderer.forBotLoop(this);
    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the getNativeBallPrediction.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput(DataPacket input, GameTickPacket packet) {
        if(!GameAnimations.areLoading) {
            GameAnimations.areLoading = true;
            new Thread(GameAnimations::loadAnimations).start();
        }
        botOutput = botBehaviour.processInput(input, packet);
        botBehaviour.updateGui(renderer, input, currentFps, averageFps, deltaTime);
        RenderTasks.setRenderer(renderer);
        RenderTasks.render();
        RenderTasks.clearTaskBuffer();

        fpsDataCalc();

        return botOutput.getForwardedOutput();
    }

    private void fpsDataCalc() {
        previousFpsTime = currentFpsTime;
        currentFpsTime = System.currentTimeMillis();

        if(currentFpsTime - previousFpsTime == 0) {
            currentFpsTime++;
        }
        currentFps = 1.0 / ((currentFpsTime - previousFpsTime) / 1000.0);
        averageFps = (averageFps*29 + (currentFps)) / 30.0;
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

        DataPacket dataPacket;
        try {
            dataPacket = new DataPacket(packet, previousDataPacketOptRef, playerIndex, this);
        }
        catch (RuntimeException runtimeException) {
            return new ControlsOutput();
        }
        ControlsOutput controlsOutput = processInput(dataPacket, packet);

        previousDataPacketOptRef.set(Optional.of(dataPacket));

        // timestamp after executing the bot
        time2 = System.currentTimeMillis();

        deltaTime = time2 - time1;
        return controlsOutput;
    }

    public void retire() {
        //System.out.println("Retiring bot " + playerIndex);
        renderer.eraseFromScreen();
    }
}
