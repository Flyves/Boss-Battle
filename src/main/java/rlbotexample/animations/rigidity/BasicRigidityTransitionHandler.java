package rlbotexample.animations.rigidity;

import rlbotexample.animations.CarGroupAnimator;

public class BasicRigidityTransitionHandler {

    public static void handle(CarGroupAnimator animator) {
        animator.carsRigidity = 0.2 + animator.currentFrameIndex()/180.0;
        if(animator.carsRigidity > 1) {
            animator.carsRigidity = 1;
        }
    }
}
