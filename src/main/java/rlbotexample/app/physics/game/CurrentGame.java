package rlbotexample.app.physics.game;

import rlbot.render.Renderer;
import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.app.physics.game.states.boss_phase.InitBossPhase;
import rlbotexample.app.physics.game.states.stats_handling.HandlePlayerStats;
import rlbotexample.app.physics.game.states.stats_handling.demolition_states.WaitForDemolitionRequest;
import rlbotexample.dynamic_objects.DataPacket;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.resource_handling.electric_balls.ElectricBallsResourceHandler;
import util.state_machine.StateMachine;

public class CurrentGame {

    public static final int BOSS_INITIAL_HP = 1000;
    public static final int PLAYER_INITIAL_HP = 100;
    public static final double BOSS_MAX_SPEED = 2700/RlConstants.BOT_REFRESH_RATE;
    public static final double BOSS_DASH_SPEED = 17000/RlConstants.BOT_REFRESH_RATE;
    public static HumanPlayer humanPlayer;
    public static BossAi bossAi;

    public static boolean playerDemolitionRequest = false;
    private static final StateMachine PLAYER_STATS_MACHINE = new StateMachine(new HandlePlayerStats());
    private static final StateMachine BOSS_PHASE_MACHINE = new StateMachine(new InitBossPhase());

    public static void step(DataPacket input) {
        PLAYER_STATS_MACHINE.exec(input);
        BOSS_PHASE_MACHINE.exec(input);

        blockGoals(input);

        ElectricBallsResourceHandler.updateElectricBalls(input);
    }

    public static void displayRenderer(DataPacket input, Renderer renderer) {
        BOSS_PHASE_MACHINE.debug(input, renderer);

        displayBlockedGoals(input, renderer);

        ElectricBallsResourceHandler.renderElectricBalls(input, renderer);

        PLAYER_STATS_MACHINE.debug(input, renderer);
    }

    public static void blockGoals(DataPacket input) {
        if(input.humanCar.position.y > RlConstants.WALL_DISTANCE_Y) {
            PhysicsOfBossBattle.setVelocity(input.humanCar.velocity.minus(new Vector3(0, 8000/RlConstants.BOT_REFRESH_RATE, 0)), input.humanCar);
        }
        else if(input.humanCar.position.y < -RlConstants.WALL_DISTANCE_Y) {
            PhysicsOfBossBattle.setVelocity(input.humanCar.velocity.plus(new Vector3(0, 8000/RlConstants.BOT_REFRESH_RATE, 0)), input.humanCar);
        }
    }

    public static void displayBlockedGoals(DataPacket input, Renderer renderer) {

    }

    public static void demolishPlayer() {
        playerDemolitionRequest = true;
    }

    public static void triggerGameOver() {
        //demolishPlayer();

        /* TODO: add game over state, menu state, etc.
            Set the game over trigger to true, and stuff...
            In the "game over" state, demolish the player, play some "game over" scene/animation or idk, and go back to the menu.
         */
    }
}
