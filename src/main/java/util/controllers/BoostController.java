package util.controllers;

import util.game_constants.RlConstants;

public class BoostController {

    private final PulseDriveController pulseController;

    public BoostController() {
        this.pulseController = new PulseDriveController(RlConstants.ACCELERATION_DUE_TO_BOOST_IN_AIR);
    }

    public boolean process(double desiredAverageAcceleration) {
        return pulseController.process(desiredAverageAcceleration);
    }
}
