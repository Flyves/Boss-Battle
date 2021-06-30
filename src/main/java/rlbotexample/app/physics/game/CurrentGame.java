package rlbotexample.app.physics.game;

import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.app.physics.game.states.InitGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.StateMachine;

public class CurrentGame {

    public static HumanPlayer humanPlayer;
    public static BossAi bossAi;

    private static final StateMachine GAME_MACHINE = new StateMachine(new InitGame());

    public static void step(DataPacket input) {
        GAME_MACHINE.exec(input);
    }
}
