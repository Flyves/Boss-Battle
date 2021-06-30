package util.controllers.throttle_controller;

import util.controllers.PulseDriveController;
import util.game_constants.RlConstants;

public class ReverseThrottleController {

    private final PulseDriveController pulseController;

    public ReverseThrottleController() {
        this.pulseController = new PulseDriveController(3500);
    }

    public boolean process(double desiredAngularAcceleration) {
        return pulseController.process(desiredAngularAcceleration);
    }
}
