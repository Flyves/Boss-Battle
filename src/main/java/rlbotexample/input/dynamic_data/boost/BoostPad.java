package rlbotexample.input.dynamic_data.boost;


import rlbotexample.input.dynamic_data.DataPacket;
import rlbotexample.input.dynamic_data.car.CarData;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of one of the boost pads on the field.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class BoostPad {

    public static final double MAX_ACCEPTED_DISTANCE_BETWEEN_NODES = 4000;

    public final Vector3 location;
    public final boolean isBigBoost;
    public boolean isActive;
    public final int boostId;
    public final double boostAmount;
    public double timeBeforeReloaded;
    public final HitCylinder hitBox;

    public final List<BoostPad> neighbours;

    public BoostPad(Vector3 location, boolean isBigBoost, HitCylinder hitBox, int boostId) {
        this.location = location;
        this.isBigBoost = isBigBoost;
        this.boostId = boostId;
        this.boostAmount = isBigBoost ? BoostManager.BIG_BOOST_AMOUNT : BoostManager.SMALL_BOOST_AMOUNT;

        this.isActive = false;
        this.timeBeforeReloaded = 0;
        this.hitBox = hitBox;

        this.neighbours = new ArrayList<>();
    }

    public void setActive(final boolean isActive) {
        this.isActive = isActive;
    }

    public void setTimeBeforeReloaded(final double timeBeforeReloaded) {
        this.timeBeforeReloaded = timeBeforeReloaded;
    }

    public void addNeighbourNode(final BoostPad that) {
        if(this.location.minus(that.location).magnitudeSquared()
                < MAX_ACCEPTED_DISTANCE_BETWEEN_NODES * MAX_ACCEPTED_DISTANCE_BETWEEN_NODES) {
            if(!neighbours.contains(that)) {
                neighbours.add(that);
            }
        }
    }

    public double nodeWeight(final ExtendedCarData carData) {
        final Vector3 positionFromCar = location.minus(carData.position);
        final double distance = positionFromCar.magnitude();
        final double speed = carData.velocity.magnitude();
        final double carBoost = carData.boost;

        return 1.0/
                ((-carData.velocity.dotProduct(positionFromCar)
                + distance
                - effectiveBoostAmount(carBoost))
            * (reachableWithConstantSpeed(distance, speed) ? 1 : 0.000001));
    }

    private boolean reachableWithConstantSpeed(final double distance, final double speed) {
        return distance/speed >= timeBeforeReloaded;
    }

    private double effectiveBoostAmount(final double boost) {
        return Math.min(boost + boostAmount, RlConstants.CAR_MAX_BOOST_AMOUNT) - boost;
    }
}
