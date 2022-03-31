package rlbotexample.asset.animation.rigidity;

import rlbotexample.asset.animation.discrete_interpolator.DiscreteCarGroupAnimator;

public class BasicRigidityTransitionHandler {

    public static void handle(DiscreteCarGroupAnimator animator) {
        handle(animator, 0.2);
    }

    public static void handle(DiscreteCarGroupAnimator animator, double initialRigidity) {
        animator.carsRigidity = initialRigidity + animator.currentFrameIndex()/180.0;
        if(animator.carsRigidity > 1) {
            animator.carsRigidity = 1;
        }
    }
}
