package util.controllers.throttle_controller;

import util.controllers.PulseDriveController;
import util.game_constants.RlConstants;

public class IdleThrottleController {

    private final PulseDriveController pulseController;

    public IdleThrottleController() {
        this.pulseController = new PulseDriveController(525);
    }

    public boolean process(double desiredAcceleration) {
        return pulseController.process(Math.abs(desiredAcceleration));
    }
}
