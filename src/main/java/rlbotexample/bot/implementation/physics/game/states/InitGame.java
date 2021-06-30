package rlbotexample.bot.implementation.physics.game.states;

import rlbot.render.Renderer;
import rlbotexample.bot.implementation.physics.game.CurrentGame;
import rlbotexample.bot.implementation.physics.game.entity.BossAi;
import rlbotexample.bot.implementation.physics.game.entity.HumanPlayer;
import rlbotexample.input.animations.CarGroupAnimator;
import rlbotexample.input.animations.GameAnimations;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import util.math.vector.CarOrientedPosition;
import util.math.vector.Vector3;
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
