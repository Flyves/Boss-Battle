package util.controllers;

import util.game_constants.RlConstants;

public class PulseDriveController {

    private final double threshold;
    private double currentValue;

    public PulseDriveController(final double threshold) {
        this.threshold = threshold;
        this.currentValue = 0;
    }

    public boolean process(double desiredValue) {
        currentValue += desiredValue;
        if(currentValue < 0) {
            currentValue = 0;
        }
        if(currentValue > threshold) {
            currentValue -= threshold;
            // cap the current value if we were unreasonable with the desired one
            if(currentValue > threshold) {
                currentValue = threshold;
            }
            return true;
        }

        return false;
    }
}
