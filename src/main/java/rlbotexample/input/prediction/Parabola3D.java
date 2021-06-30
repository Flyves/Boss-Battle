package rlbotexample.input.prediction;

import util.math.vector.Vector3;

public class Parabola3D implements Trajectory3D {

    private final Vector3 initialPosition;
    private final Vector3 initialVelocity;
    private final Vector3 acceleration;
    private final double airDragCoefficient;

    public Parabola3D(Vector3 initialPosition, Vector3 initialVelocity, Vector3 acceleration, double airDragCoefficient) {
        this.initialPosition = initialPosition;
        this.initialVelocity = initialVelocity;
        this.acceleration = acceleration;
        this.airDragCoefficient = airDragCoefficient;
    }

    public Parabola3D(Vector3 initialPosition, Vector3 initialVelocity, Vector3 acceleration) {
        this.initialPosition = initialPosition;
        this.initialVelocity = initialVelocity;
        this.acceleration = acceleration;
        this.airDragCoefficient = 0;
    }

    @Override
    public Vector3 apply(Double deltaTime) {
        final Vector3 deltaVelocity = initialVelocity.scaled(deltaTime * (1-airDragCoefficient));
        final double accelerationFactor = deltaTime * deltaTime / 2;
        final Vector3 deltaDeltaAcceleration = acceleration.scaled(accelerationFactor);

        return initialPosition.plus(deltaVelocity.plus(deltaDeltaAcceleration));
    }

    public Vector3 derivative(double deltaTime) {
        final Vector3 deltaAcceleration = acceleration.scaled(deltaTime);
        final Vector3 newVelocity = initialVelocity.minus(initialVelocity.scaled(airDragCoefficient * deltaTime));
        return newVelocity.plus(deltaAcceleration);
    }
}
