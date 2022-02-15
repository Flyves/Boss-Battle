package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_phase.InitBossPhase;
import rlbotexample.app.physics.game.states.stats_handling.HandlePlayerStats;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;
import util.state_machine.StateMachine;

public class GameActive implements State {
    private static final StateMachine PLAYER_STATS_MACHINE = new StateMachine(new HandlePlayerStats());
    private static final StateMachine BOSS_PHASE_MACHINE = new StateMachine(new InitBossPhase());

    @Override
    public void start(DataPacket input) {

    }

    @Override
    public void exec(DataPacket input) {
        PLAYER_STATS_MACHINE.exec(input);
        BOSS_PHASE_MACHINE.exec(input);
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
        PLAYER_STATS_MACHINE.debug(input, renderer);
        BOSS_PHASE_MACHINE.debug(input, renderer);
    }
}
