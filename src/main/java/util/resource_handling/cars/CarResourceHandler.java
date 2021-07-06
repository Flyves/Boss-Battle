package util.resource_handling.cars;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarResourceHandler {

    private static final List<Integer> freeCarIndexes = new ArrayList<>();

    public static void initialize(List<ExtendedCarData> allCars) {
        freeCarIndexes.addAll(allCars.stream()
                .map(carData -> carData.playerIndex)
                .collect(Collectors.toList()));
    }

    public static Optional<Integer> alloc(PlayerIndex playerIndexObj) {
        int playerIndex = playerIndexObj.value;

        if(freeCarIndexes.contains(playerIndex)) {
            freeCarIndexes.remove(playerIndex);
            return Optional.of(playerIndex);
        }
        return Optional.empty();
    }

    public static boolean free(PlayerIndex playerIndexObj) {
        int playerIndex = playerIndexObj.value;

        if(!freeCarIndexes.contains(playerIndex)) {
            freeCarIndexes.add(playerIndex);
            return true;
        }
        return false;
    }

    public static Optional<List<Integer>> alloc(PlayerAmount requestedAmount) {
        if(freeCarIndexes.size() < requestedAmount.value) {
            return Optional.empty();
        }

        List<Integer> allocatedCarIndexes = new ArrayList<>(requestedAmount.value);

        for(int i = freeCarIndexes.size() - requestedAmount.value; i < freeCarIndexes.size(); i++) {
            allocatedCarIndexes.add(freeCarIndexes.get(i));
        }
        freeCarIndexes.removeAll(allocatedCarIndexes);

        return Optional.of(allocatedCarIndexes);
    }

    public static void free(List<Integer> requestedCarIndexes) {
        requestedCarIndexes.stream()
                .map(PlayerIndex::new)
                .forEach(CarResourceHandler::free);
    }

    public static List<ExtendedCarData> dereferenceIndexes(DataPacket input, List<Integer> carIndexes) {
        return input.allCars.stream()
                .filter(carData -> carIndexes.contains(carData.playerIndex))
                .collect(Collectors.toList());
    }

    public static void handleFreeCars(DataPacket input) {
        List<ExtendedCarData> freeCars = dereferenceIndexes(input, freeCarIndexes);
        freeCars.forEach(carData -> PhysicsOfBossBattle.setOrientedPosition(new OrientedPosition(new Vector3(30000, carData.playerIndex*200, 10000), new Orientation()).toZyxOrientedPosition(), carData));
    }
}
