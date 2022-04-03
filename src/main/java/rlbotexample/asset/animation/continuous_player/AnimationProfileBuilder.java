package rlbotexample.asset.animation.continuous_player;

import rlbotexample.asset.animation.discrete_player.Animation;
import util.data_structure.builder.Builder;
import util.math.vector.OrientedPosition;

import java.util.function.Function;
import java.util.function.Supplier;

public class AnimationProfileBuilder implements Builder<AnimationProfile> {
    private static final Double DEFAULT_PLAYBACK_SPEED = 60d;   // fps
    private static final Function<Double, Double> DEFAULT_INTERPOLATION_FUNCTION = (t) -> t;

    private Animation animation;
    private Supplier<Double> playbackSpeed;
    private Function<Double, Double> inBetweenFramesInterpolationFunction;
    private Supplier<OrientedPosition> animationOffset;

    public AnimationProfileBuilder() {
        this.playbackSpeed = () -> DEFAULT_PLAYBACK_SPEED;
        this.inBetweenFramesInterpolationFunction = DEFAULT_INTERPOLATION_FUNCTION;
        this.animationOffset = OrientedPosition::new;
    }

    public AnimationProfileBuilder withAnimation(final Animation animation) {
        this.animation = animation;
        return this;
    }

    public AnimationProfileBuilder withPlaybackSpeed(final Supplier<Double> playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
        return this;
    }

    public AnimationProfileBuilder withInterpolation(final Function<Double, Double> inBetweenFramesInterpolationFunction) {
        this.inBetweenFramesInterpolationFunction = inBetweenFramesInterpolationFunction;
        return this;
    }


    public AnimationProfileBuilder withAnimationOffset(final Supplier<OrientedPosition> orientedPosition) {
        this.animationOffset = orientedPosition;
        return this;
    }

    @Override
    public AnimationProfile build() {
        return new AnimationProfile(animation, playbackSpeed, inBetweenFramesInterpolationFunction, animationOffset);
    }
}
