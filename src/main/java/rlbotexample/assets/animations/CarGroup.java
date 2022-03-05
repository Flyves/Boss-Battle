package rlbotexample.assets.animations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarGroup implements Serializable {

    public final List<AnimatedCarObject> carObjects;

    public CarGroup() {
        this.carObjects = new ArrayList<>();
    }

    public Integer amountOfBlueCars() {
        return (int) carObjects.stream().filter(carObject -> carObject.teamId == 0).count();
    }

    public Integer amountOfOrangeCars() {
        return (int) carObjects.stream().filter(carObject -> carObject.teamId == 1).count();
    }
}
