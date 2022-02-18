package rlbotexample;

import com.google.flatbuffers.FlatBufferBuilder;
import rlbot.Bot;
import rlbot.cppinterop.RLBotDll;
import rlbot.render.RenderPacket;
import rlbot.render.Renderer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A renderer that will be managed automatically. Users of this renderer should expect the packet
 * to be started and finished on a regular basis without their intervention.
 */
public class SingleBotLoopRenderer extends Renderer {

    private static Map<Integer, SingleBotLoopRenderer> botLoopMap = new ConcurrentHashMap<>();
    private RenderPacket previousPacket = null;

    private SingleBotLoopRenderer(int index) {
        super(index);
    }

    public static SingleBotLoopRenderer forBotLoop(final Bot bot) {
        botLoopMap.computeIfAbsent(bot.getIndex(), SingleBotLoopRenderer::new);
        return botLoopMap.get(bot.getIndex());
    }

    void startPacket() {
        builder = new FlatBufferBuilder(1000);
    }

    void finishAndSendIfDifferent() {
        RenderPacket packet = doFinishPacket();
        if (!packet.equals(previousPacket)) {
            RLBotDll.sendRenderPacket(packet);
            previousPacket = packet;
        }
    }
}
