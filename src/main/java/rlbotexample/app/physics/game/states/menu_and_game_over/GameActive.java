package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_phase.InitBossPhase;
import rlbotexample.app.physics.game.states.stats_handling.HandlePlayerStats;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;
import util.state_machine.StateMachine;

public class GameActive implements State {
    private static StateMachine playerStatsMachine;
    private static StateMachine bossPhaseMachine;

    @Override
    public void start(DataPacket input) {
        playerStatsMachine = new StateMachine(new HandlePlayerStats());
        bossPhaseMachine = new StateMachine(new InitBossPhase());
    }

    @Override
    public void exec(DataPacket input) {
        playerStatsMachine.exec(input);
        bossPhaseMachine.exec(input);
        BallStateSetter.setTarget(CurrentGame.bossAi.centerOfMass);
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.isGameOver) {
            return new GameOver();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {
        playerStatsMachine.debug(input, renderer);
        bossPhaseMachine.debug(input, renderer);
    }
}
