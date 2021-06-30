package rlbotexample.input.animations;

import util.math.vector.ZyxOrientedPosition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarGroup implements Serializable {

    public final List<ZyxOrientedPosition> orientedPositions;

    public CarGroup() {
        this.orientedPositions = new ArrayList<>();
    }
}
