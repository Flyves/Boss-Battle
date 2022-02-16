package rlbotexample.animations.rigidity;

import rlbotexample.animations.CarGroupAnimator;

public class BasicRigidityTransitionHandler {

    public static void handle(CarGroupAnimator animator) {
        handle(animator, 0.2);
    }

    public static void handle(CarGroupAnimator animator, double initialRigidity) {
        animator.carsRigidity = initialRigidity + animator.currentFrameIndex()/180.0;
        if(animator.carsRigidity > 1) {
            animator.carsRigidity = 1;
        }
    }
}
