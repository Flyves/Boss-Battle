package rlbotexample.app.physics.game.states.stats_handling;

import rlbot.render.Renderer;
import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.math.vector.ZyxOrientedPosition;
import util.resource_handling.cars.CarResourceHandler;
import util.resource_handling.cars.PlayerAmount;
import util.resource_handling.cars.PlayerIndex;
import util.state_machine.State;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DemolishPlayer implements State {

    private final AtomicReference<Optional<Integer>> demolitionCarIndexOptRef = new AtomicReference<>();

    @Override
    public void start(DataPacket input) {
        demolitionCarIndexOptRef.set(Optional.empty());
        Optional<List<Integer>> demolitionCarIndexOpt = CarResourceHandler.alloc(new PlayerAmount(1));
        demolitionCarIndexOpt.ifPresent(demolitionCarIndexes -> demolitionCarIndexOptRef.set(Optional.of(demolitionCarIndexes.get(0))));
    }

    @Override
    public void exec(DataPacket input) {
        demolitionCarIndexOptRef.get().ifPresent(demolitionCarIndex -> {
            ExtendedCarData demolitionCar = input.allCars.get(demolitionCarIndex);
            ExtendedCarData humanCar = input.humanCar;
            Vector3 killingPosition = humanCar.position.minus(humanCar.orientation.noseVector.scaled(80));
            Vector3 noseOrientation = humanCar.position.minus(killingPosition).normalized().scaled(-1);
            Vector3 roofOrientation = humanCar.orientation.roofVector;
            ZyxOrientedPosition demolitionZyxOrientedPosition = new OrientedPosition(killingPosition, new Orientation(noseOrientation, roofOrientation)).toZyxOrientedPosition();
            demolitionZyxOrientedPosition.eulerZYX = demolitionZyxOrientedPosition.eulerZYX.scaled(-1, 1, 1);
            PhysicsOfBossBattle.setOrientedPosition(demolitionZyxOrientedPosition, demolitionCar);
            PhysicsOfBossBattle.setVelocity(noseOrientation.scaledToMagnitude(-3000), demolitionCar);
        });
    }

    @Override
    public void stop(DataPacket input) {
        demolitionCarIndexOptRef.get().ifPresent(demolitionCar -> CarResourceHandler.free(new PlayerIndex(input.allCars.get(demolitionCar).playerIndex)));
    }

    @Override
    public State next(DataPacket input) {
        if(input.humanCar.isDemolished) {
            CurrentGame.playerDemolitionRequest = false;
            return new WaitForDemolitionRequest();
        }
        if(!demolitionCarIndexOptRef.get().isPresent()) {
            return new WaitForDemolitionRequest();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
