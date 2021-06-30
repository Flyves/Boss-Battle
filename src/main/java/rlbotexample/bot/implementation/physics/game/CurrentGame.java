package rlbotexample.bot.implementation.physics.game;

import rlbotexample.bot.implementation.physics.game.entity.BossAi;
import rlbotexample.bot.implementation.physics.game.entity.HumanPlayer;
import rlbotexample.bot.implementation.physics.game.states.InitGame;
import rlbotexample.input.animations.CarGroupAnimator;
import rlbotexample.input.animations.GameAnimations;
import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import util.math.vector.CarOrientedPosition;
import util.math.vector.Vector3;
import util.state_machine.StateMachine;

public class CurrentGame {

    public static HumanPlayer humanPlayer;
    public static BossAi bossAi;

    private static final StateMachine GAME_MACHINE = new StateMachine(new InitGame());

    public static void step(DataPacket input) {
        GAME_MACHINE.exec(input);
    }
}
