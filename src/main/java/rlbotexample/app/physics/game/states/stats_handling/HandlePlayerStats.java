package rlbotexample.app.physics.game.states.stats_handling;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.app.physics.game.states.stats_handling.demolition_states.WaitForDemolitionRequest;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;
import util.state_machine.StateMachine;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class HandlePlayerStats implements State {

    private static final StateMachine PLAYER_DEMOLITION_MACHINE = new StateMachine(new WaitForDemolitionRequest());

    @Override
    public void start(DataPacket input) {
        CurrentGame.humanPlayer = new HumanPlayer(input.humanCar);
        CurrentGame.bossAi = new BossAi(input.car);
    }

    @Override
    public void exec(DataPacket input) {
        updatePlayerHealth(input);
        updateBossAiHealth(input, CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation));
    }

    private void updatePlayerHealth(DataPacket input) {
        PLAYER_DEMOLITION_MACHINE.exec(input);
        if(CurrentGame.humanPlayer.health <= 0) {
            CurrentGame.humanPlayer.health = 0;
            CurrentGame.triggerGameOver();
        }
    }

    private void updateBossAiHealth(DataPacket input, List<ExtendedCarData> bossCars) {
        List<ExtendedCarData> allNewDemos = bossCars.stream()
                .filter(carData -> {
                    if(carData.previousCarData.isPresent()) {
                        boolean previousIsDemolished = carData.previousCarData.get().isDemolished;
                        return carData.isDemolished && !previousIsDemolished;
                    }
                    return false;
                })
                .filter(newDemolishedCar -> input.humanCar.position.minus(newDemolishedCar.position).magnitudeSquared() < 300*300)
                .collect(Collectors.toList());

        CurrentGame.bossAi.health -= allNewDemos.size() * 30;
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {
        renderer.drawString3d(Integer.toString(CurrentGame.bossAi.health), Color.CYAN, CurrentGame.bossAi.centerOfMass.toFlatVector(), 2, 2);
    }
}
