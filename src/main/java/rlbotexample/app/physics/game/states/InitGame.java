package rlbotexample.app.physics.game.states;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class InitGame implements State {

    @Override
    public void exec(DataPacket input) {
        CurrentGame.humanPlayer = new HumanPlayer(input.humanCar);
        CurrentGame.bossAi = new BossAi(input.car);
    }

    @Override
    public State next(DataPacket input) {
        return new AnimationTest();
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
