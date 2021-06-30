package rlbotexample.bot.implementation;

import rlbot.flat.GameTickPacket;
import rlbot.render.Renderer;
import rlbotexample.bot.FlyveBot;
import rlbotexample.bot.implementation.graphics.GraphicsOfBossBattle;
import rlbotexample.bot.implementation.physics.PhysicsOfBossBattle;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.output.BotOutput;

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