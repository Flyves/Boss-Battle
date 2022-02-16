package rlbotexample.app.physics.game.states.stats_handling;

import rlbot.render.Renderer;
import rlbotexample.app.graphics.health_bars.BossHealthBar;
import rlbotexample.app.graphics.health_bars.PlayerHealthBar;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.app.physics.game.states.stats_handling.demolition_states.WaitForDemolitionRequest;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;
import util.state_machine.StateMachine;

import java.util.List;
import java.util.stream.Collectors;

public class HandlePlayerStats implements State {

    private static final StateMachine PLAYER_DEMOLITION_MACHINE = new StateMachine(new WaitForDemolitionRequest());
    
    private static final int HP_DEALT_FOR_EVERY_DEMOLITION = 20;

    @Override
    public void start(DataPacket input) {
        CurrentGame.humanPlayer = new HumanPlayer();
        CurrentGame.bossAi = new BossAi();
        CurrentGame.humanPlayer.health = CurrentGame.PLAYER_INITIAL_HP;
        CurrentGame.bossAi.health = CurrentGame.BOSS_INITIAL_HP;
    }

    @Override
    public void exec(DataPacket input) {
        updatePlayerHealth(input);
        updateBossAiHealth(CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation));
    }

    private void updatePlayerHealth(DataPacket input) {
        PLAYER_DEMOLITION_MACHINE.exec(input);
        if(CurrentGame.humanPlayer.health <= 0) {
            CurrentGame.humanPlayer.health = 0;
            CurrentGame.triggerGameOver();
        }
    }

    private void updateBossAiHealth(List<ExtendedCarData> bossCars) {
        List<ExtendedCarData> allNewDemos = bossCars.stream()
                .filter(carData -> {
                    if(carData.previousCarData.isPresent()) {
                        boolean previousIsDemolished = carData.previousCarData.get().isDemolished;
                        return carData.isDemolished && !previousIsDemolished;
                    }
                    return false;
                })
                .filter(newDemolishedCar -> newDemolishedCar.position.z > 0)
                .collect(Collectors.toList());

        CurrentGame.bossAi.health -= HP_DEALT_FOR_EVERY_DEMOLITION * allNewDemos.size();

        if(CurrentGame.bossAi.health <= 666) {
            CurrentGame.bossAi.health = 0;
            CurrentGame.triggerGameOver();
        }
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {
        PlayerHealthBar.renderOnScreen();
    }
}
