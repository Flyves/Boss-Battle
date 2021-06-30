package rlbotexample.app;

import rlbot.flat.GameTickPacket;
import rlbot.render.Renderer;
import rlbotexample.generic_bot.FlyveBot;
import rlbotexample.app.graphics.GraphicsOfBossBattle;
import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.generic_bot.output.BotOutput;

public class BossBattleBot extends FlyveBot {

    public BossBattleBot() {}

    @Override
    public BotOutput processInput(DataPacket input, GameTickPacket packet) {
        PhysicsOfBossBattle.execute(input);

        return new BotOutput();
    }

    @Override
    public void updateGui(Renderer renderer, DataPacket input, double currentFps, double averageFps, long botExecutionTime) {
        //super.updateGui(renderer, input, currentFps, averageFps, botExecutionTime);
        GraphicsOfBossBattle.print(input, renderer);
    }
}