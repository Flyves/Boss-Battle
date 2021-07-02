package rlbotexample.app.physics.game;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.app.physics.game.states.animation_test.AnimationTest;
import rlbotexample.app.physics.game.states.boss_phase.InitBossPhase;
import rlbotexample.app.physics.game.states.stats_handling.InitPlayerStats;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.StateMachine;

public class CurrentGame {

    public static final int BOSS_INITIAL_HP = 1000;
    public static HumanPlayer humanPlayer;
    public static BossAi bossAi;

    private static final StateMachine PLAYER_STATS_MACHINE = new StateMachine(new InitPlayerStats());
    private static final StateMachine BOSS_PHASE_MACHINE = new StateMachine(new InitBossPhase());

    public static void step(DataPacket input) {
        PLAYER_STATS_MACHINE.exec(input);
        BOSS_PHASE_MACHINE.exec(input);
    }

    public static void displayRenderer(DataPacket input, Renderer renderer) {
        PLAYER_STATS_MACHINE.debug(input, renderer);
        BOSS_PHASE_MACHINE.debug(input, renderer);
    }
}
