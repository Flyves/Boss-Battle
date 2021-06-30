package util.controllers;

import util.game_constants.RlConstants;

public class DriftController {

    private final PulseDriveController pulseController;

    public DriftController() {
        this.pulseController = new PulseDriveController(RlConstants.CAR_MAX_ANGULAR_ACCELERATION_YAW);
    }

    public boolean process(double desiredAngularAcceleration) {
        return pulseController.process(Math.abs(desiredAngularAcceleration));
    }
}
