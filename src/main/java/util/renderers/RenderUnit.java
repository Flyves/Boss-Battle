package util.renderers;

import rlbot.render.Renderer;

import java.util.function.Consumer;

public class RenderUnit {

    public final Consumer<Renderer> renderTask;
    public final IndexedRenderer renderer;

    public RenderUnit(final Consumer<Renderer> renderTask, final IndexedRenderer renderer) {
        this.renderTask = renderTask;
        this.renderer = renderer;
    }
}
