package rlbotexample.asset.animation.player;

import rlbotexample.asset.animation.discrete_interpolator.CarGroup;
import rlbotexample.dynamic_objects.DataPacket;

public class AnimationPlayer {
    private final AnimationProfile animationProfile;
    private final CarGroupStateSetter carGroupStateSetter;
    private double previousTime;
    private double elapsedFrames;

    public AnimationPlayer(final AnimationProfile animationProfile) {
        this.animationProfile = animationProfile;
        this.carGroupStateSetter = new CarGroupStateSetter(animationProfile.animation);
        this.previousTime = getTimeInSeconds();
        this.elapsedFrames = 0;
    }

    public void stateSet(final DataPacket input) {
        // updating the frame at the user's desired playback rate
        final double playbackSpeed = animationProfile.playbackSpeed.get();
        final double currentTime = getTimeInSeconds();
        elapsedFrames += (currentTime - previousTime) * playbackSpeed;
        previousTime = currentTime;

        // bounding the resulting value
        if(elapsedFrames < 0) {
            elapsedFrames = 0;
        }
        if(elapsedFrames > animationProfile.animation.frames.size()-1) {
            elapsedFrames = animationProfile.animation.frames.size()-1;
        }

        // computing the interpolation value
        double t = elapsedFrames - (int) elapsedFrames;
        t = animationProfile.inBetweenFramesInterpolationFunction.apply(t);

        // finding the frames for the interpolation
        final int index1 = (int) elapsedFrames;
        int index2 = index1+1;
        // making sure we don't get an invalid index at the end
        if(index2 >= animationProfile.animation.frames.size()-1) index2--;
        final CarGroup frame1 = animationProfile.animation.queryFrame(index1);
        final CarGroup frame2 = animationProfile.animation.queryFrame(index2);

        // state setting
        final CarGroup carGroupWithoutOffset = CarGroupUtils.interpolate(frame1, frame2, t);
        final CarGroup carGroupToStateSet = CarGroupUtils.addOffset(carGroupWithoutOffset, animationProfile.animationOffset.get());
        carGroupStateSetter.stateSet(carGroupToStateSet, input);
    }

    public boolean isFinished() {
        // -1.0001 for double precision error
        return elapsedFrames >= animationProfile.animation.frames.size()-1.0001;
    }

    public void close() {
        carGroupStateSetter.close();
    }

    private double getTimeInSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }
}
