package rlbotexample.animations;

import util.math.vector.ZyxOrientedPosition;

import java.io.Serializable;

public class AnimatedCarObject implements Serializable {

    public int carId;
    public int teamId;
    public ZyxOrientedPosition zyxOrientedPosition;

    public AnimatedCarObject(int carId, int teamId, ZyxOrientedPosition zyxOrientedPosition) {
        this.carId = carId;
        this.teamId = teamId;
        this.zyxOrientedPosition = zyxOrientedPosition;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AnimatedCarObject)) {
            return false;
        }
        return ((AnimatedCarObject)obj).carId == this.carId;
    }
}
