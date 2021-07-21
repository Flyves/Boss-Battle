package util.renderers;

import rlbot.render.Renderer;

import java.util.function.Function;

public class RenderTask {

    public static void render(Function<Renderer, Void> renderingTask) {
        IndexedRenderer indexedRenderer = new IndexedRenderer();
        renderingTask.apply(indexedRenderer.getRenderer());
        indexedRenderer.close();
    }

}
