package rlbotexample.app.physics.game.states.stats_handling;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.entity.BossAi;
import rlbotexample.app.physics.game.entity.HumanPlayer;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;

import java.awt.*;
import java.util.List;

public class HandlePlayerStats implements State {

    private boolean wasBossAiDemolished = false;

    @Override
    public void start(DataPacket input) {
        CurrentGame.humanPlayer = new HumanPlayer(input.humanCar);
        CurrentGame.bossAi = new BossAi(input.car);
    }

    @Override
    public void exec(DataPacket input) {
        updatePlayerHealth();
        updateBossAiHealth(CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation));
    }

    private void updatePlayerHealth() {
        if(CurrentGame.humanPlayer.health <= 0) {
            CurrentGame.humanPlayer.health = 0;
            CurrentGame.triggerGameOver();
        }
    }

    private void updateBossAiHealth(List<ExtendedCarData> bossCars) {
        boolean isDemolished = bossCars.stream()
                .anyMatch(car -> car.isDemolished);

        if(isDemolished && !wasBossAiDemolished) {
            CurrentGame.bossAi.health -= 30;
        }
        wasBossAiDemolished = isDemolished;
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
