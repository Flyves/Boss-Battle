package util.renderers;

import com.google.flatbuffers.FlatBufferBuilder;
import rlbot.cppinterop.RLBotDll;
import rlbot.render.RenderPacket;
import rlbot.render.Renderer;

import java.io.Closeable;

public class IndexedRenderer extends Renderer implements Closeable {

    private static int rendererCount = 0;

    public IndexedRenderer() {
        super(Integer.valueOf(rendererCount++).hashCode());
    }

    public void open() {
        this.builder = new FlatBufferBuilder(1000);
    }

    private RenderPacket finishPacket() {
        return this.doFinishPacket();
    }

    @Override
    public void close() {
        RLBotDll.sendRenderPacket(this.finishPacket());
    }
}
