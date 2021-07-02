package rlbotexample.app.physics.game.states.stats_handling;

import rlbot.render.Renderer;
import rlbot.vector.Vector3;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.resource_handling.CarResourceHandler;
import util.state_machine.State;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HandlePlayerStats implements State {

    private boolean wasPlayerDemolished = false;
    private boolean wasBossAiDemolished = false;

    @Override
    public void exec(DataPacket input) {
        updatePlayerHealth();
        updateBossAiHealth(CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation));
    }

    private void updatePlayerHealth() {
        if(CurrentGame.humanPlayer.humanCar.isDemolished && !wasPlayerDemolished) {
            CurrentGame.humanPlayer.health--;
        }
        wasPlayerDemolished = CurrentGame.humanPlayer.humanCar.isDemolished;
    }

    private void updateBossAiHealth(List<ExtendedCarData> bossCars) {
        boolean isDemolished = bossCars.stream()
                .anyMatch(car -> car.isDemolished);

        if(isDemolished && !wasBossAiDemolished) {
            CurrentGame.bossAi.health--;
        }
        wasBossAiDemolished = isDemolished;
    }

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {
        renderer.drawString3d(Integer.toString(CurrentGame.bossAi.health), Color.CYAN, CurrentGame.bossAi.centerOfMass.toFlatVector(), 2, 2);
    }
}
