package rlbotexample.asset.animation.discrete_interpolator;

import util.math.vector.ZyxOrientedPosition;

import java.io.Serializable;

public class CarData implements Serializable {
    public int carId;
    public int teamId;
    public ZyxOrientedPosition zyxOrientedPosition;

    public CarData(int carId, int teamId, ZyxOrientedPosition zyxOrientedPosition) {
        this.carId = carId;
        this.teamId = teamId;
        this.zyxOrientedPosition = zyxOrientedPosition;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CarData)) {
            return false;
        }
        return ((CarData)obj).carId == this.carId;
    }
}
