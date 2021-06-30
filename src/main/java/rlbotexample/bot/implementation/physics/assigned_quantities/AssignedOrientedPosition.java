package rlbotexample.bot.implementation.physics.assigned_quantities;

import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import util.math.vector.ZyxOrientedPosition;

public class AssignedOrientedPosition {

    public ExtendedCarData carData;
    public ZyxOrientedPosition orientedPosition;

    public AssignedOrientedPosition(ExtendedCarData carData, ZyxOrientedPosition orientedPosition) {
        this.carData = carData;
        this.orientedPosition = orientedPosition;
    }
}