package rlbotexample.animations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarGroup implements Serializable {

    public final List<AnimatedCarObject> carObjects;

    public CarGroup() {
        this.carObjects = new ArrayList<>();
    }
}
