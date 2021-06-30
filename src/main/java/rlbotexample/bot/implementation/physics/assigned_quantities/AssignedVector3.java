package rlbotexample.bot.implementation.physics.assigned_quantities;

import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import util.math.vector.Vector3;

public class AssignedVector3 {

    public ExtendedCarData carData;
    public Vector3 vector;

    public AssignedVector3(ExtendedCarData carData, Vector3 vector) {
        this.carData = carData;
        this.vector = vector;
    }
}