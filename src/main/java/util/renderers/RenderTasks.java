package util.renderers;

import rlbot.render.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RenderTasks {
    private static final List<Consumer<Renderer>> tasks = new ArrayList<>();
    private static Renderer renderer;

    public static void setRenderer(final Renderer renderer) {
        RenderTasks.renderer = renderer;
    }

    public static void append(final Consumer<Renderer> renderingTask) {
        tasks.add(renderingTask);
    }

    public static void render() {
        for(int i = 0; i < tasks.size(); i++) {
            tasks.get(i).accept(renderer);
        }
    }

    public static void clearTaskBuffer() {
        tasks.clear();
    }
}
