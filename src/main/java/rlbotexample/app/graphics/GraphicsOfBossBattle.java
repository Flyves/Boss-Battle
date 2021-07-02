package rlbotexample.app.graphics;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.renderers.ShapeRenderer;

public class GraphicsOfBossBattle {

    public static void print(DataPacket input, Renderer renderer) {
        CurrentGame.displayRenderer(input, renderer);
    }
}