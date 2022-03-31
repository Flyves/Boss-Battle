package rlbotexample.asset.animation.player;

import rlbotexample.dynamic_objects.DataPacket;

import java.util.ArrayList;
import java.util.List;

public class AnimationTasks {
    private static final List<AnimationPlayer> animationHandlers = new ArrayList<>();

    public static void append(final AnimationPlayer animationPlayer) {
        animationHandlers.add(animationPlayer);
    }

    public static void stateSetAnimations(final DataPacket input) {
        animationHandlers.forEach(animationPlayer -> animationPlayer.stateSet(input));
    }

    public static void clearFinishedTasks() {
        final List<AnimationPlayer> finishedAnimations = new ArrayList<>();
        animationHandlers.stream()
                .filter(AnimationPlayer::isFinished)
                .forEach(finishedAnimations::add);
        animationHandlers.removeAll(finishedAnimations);
    }
}
