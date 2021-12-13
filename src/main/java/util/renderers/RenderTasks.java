package util.renderers;

import rlbot.render.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RenderTasks {

    private static final List<Consumer<IndexedRenderer>> tasks = new ArrayList<>();
    private static final List<IndexedRenderer> renderers = new ArrayList<>();

    public RenderTasks() {}

    public static void append(final Consumer<IndexedRenderer> renderingTask) {
        tasks.add(renderingTask);
        while (renderers.size() < tasks.size()) {
            renderers.add(new IndexedRenderer());
        }
    }

    public static void render() {
        for(int i = 0; i < tasks.size(); i++) {
            renderers.get(i).open();
            tasks.get(i).accept(renderers.get(i));
            renderers.get(i).close();
        }
        System.out.println(tasks.size());
    }

    public static void clearTaskBuffer() {
        tasks.clear();
    }
}
