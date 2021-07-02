package rlbotexample.app.physics.game.states.stats_handling;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class InitPlayerStats implements State {

    @Override
    public void exec(DataPacket input) {
        CurrentGame.humanPlayer = new HumanPlayer(input.humanCar);
        try(BossAi bossAi = new BossAi(input.car)) {
            CurrentGame.bossAi = bossAi;
        }
        catch(Exception ignored) {
            System.out.println("Error: there are not enough cars in the game to display all animations!");
        }
    }

    @Override
    public State next(DataPacket input) {
        return new HandlePlayerStats();
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
