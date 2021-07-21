package util.renderers;

import rlbot.render.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RenderTasks {

    private final List<Consumer<Renderer>> tasks;

    public RenderTasks() {
        tasks = new ArrayList<>();
    }

    public RenderTasks append(Consumer<Renderer> renderingTask) {
        tasks.add(renderingTask);
        return this;
    }

    public void render(Renderer renderer) {
        tasks.forEach(renderingTask -> {
            IndexedRenderer indexedRenderer = new IndexedRenderer();
            renderingTask.accept(renderer);
            indexedRenderer.close();
        });
    }

}
