package rlbotexample.asset.animation.animation;

import rlbotexample.asset.animation.car_group.CarData;
import rlbotexample.asset.animation.car_group.CarGroup;
import rlbotexample.asset.animation.car_group.CarGroupStateSetter;
import rlbotexample.asset.animation.car_group.CarGroupUtils;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.math.vector.ZyxOrientedPosition;

import java.util.*;
import java.util.stream.Collectors;

public class AnimationPlayer {
    private double timeOffsetDueToPausing;
    private final AnimationProfile animationProfile;
    private final CarGroupStateSetter carGroupStateSetter;
    private double previousTime;
    private double elapsedFrames;
    private double previousElapsedFrames;
    private Vector3 centerOfMass;

    public AnimationPlayer(final AnimationProfile animationProfile) {
        this.animationProfile = animationProfile;
        this.carGroupStateSetter = new CarGroupStateSetter(animationProfile.animation);
        this.previousTime = getTimeInSeconds();
        this.previousElapsedFrames = 0;
        this.elapsedFrames = 0;
        this.timeOffsetDueToPausing = 0;
        this.centerOfMass = new Vector3();
    }

    public void stateSet(final DataPacket input) {
        // updating the frame at the user's desired playback rate
        final double playbackSpeed = animationProfile.playbackSpeed.get();
        final double currentTime = getTimeInSeconds();
        previousElapsedFrames = elapsedFrames;
        elapsedFrames += (currentTime - previousTime) * playbackSpeed;
        previousTime = currentTime;

        // Running triggers.
        for(int i = (int)previousElapsedFrames; i < (int)elapsedFrames; i++) {
                final Runnable triggeredFunction = animationProfile.frameEvents.get(i);
                if(triggeredFunction != null) {
                    triggeredFunction.run();
                }
        }

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
        handleLooping();

        // state setting
        final CarGroup carGroupWithoutOffset = CarGroupUtils.interpolate(frame1, frame2, t);
        final CarGroup offsetedCarGroup = CarGroupUtils.addOffset(carGroupWithoutOffset, animationProfile.animationOffset.get());
        final CarGroup inGameCarGroupRepresentation = generateInGameCarGroupRepresentation(offsetedCarGroup, input);
        final double rigidity = animationProfile.rigidityFunction.apply(index1);
        // TODO: Fix cars flipping back and forth when the rigidity is below 1.0.
        final CarGroup carGroupToStateSet = CarGroupUtils.interpolate(inGameCarGroupRepresentation, offsetedCarGroup, rigidity);
        if(carGroupToStateSet.carObjects.size() > 0) {
            this.centerOfMass = carGroupToStateSet.carObjects.stream()
                    .map(carData -> carData.zyxOrientedPosition.position)
                    .reduce(Vector3::plus)
                    .map(v -> v.scaled(1.0 / carGroupToStateSet.carObjects.size()))
                    .orElse(new Vector3());
        }
        carGroupStateSetter.stateSet(carGroupToStateSet, input);
    }

    private CarGroup generateInGameCarGroupRepresentation(final CarGroup carGroupWithoutOffset, final DataPacket input) {
        final CarGroup carGroup = new CarGroup();

        carGroup.carObjects.addAll(carGroupWithoutOffset.carObjects.stream().map(carData -> {
            final int inGameCarIndex = carGroupStateSetter.mapAnimationIndexToRLBotIndex(carGroupWithoutOffset.carObjects.indexOf(carData));
            final ExtendedCarData inGameCar = input.allCars.get(inGameCarIndex);
            // TODO: Cars are flipping, I'm not sure why.
            //       The tests for transforming euler orientation into our own orientation representation and vice versa are passing, I'm very confused.
            //       This code is working and all, but man, this is scuffed af.
            final OrientedPosition inGameOrientedPosition = new OrientedPosition(inGameCar.position, inGameCar.orientation.rotate(inGameCar.orientation.roofVector.scaled(Math.PI)));
            final ZyxOrientedPosition inGameZyxOrientedPosition = inGameOrientedPosition.toZyxOrientedPosition();
            return new CarData(
                    carData.carId,
                    carData.teamId,
                    inGameZyxOrientedPosition,
                    carData.isBoosting);
        }).collect(Collectors.toList()));

        return carGroup;
    }

    public double getCurrentAnimationFrame() {
        return elapsedFrames;
    }

    public void setCurrentAnimationFrame(final double frameCount) {
        elapsedFrames = frameCount;
    }

    public int getAnimationLength() {
        return animationProfile.animation.frames.size();
    }

    public boolean isFinished() {
        return animationProfile.finishingSupplier.get()
                || isAnimationEndReached();
    }

    private void handleLooping() {
        if(animationProfile.isLooping) {
            if(elapsedFrames >= animationProfile.animation.frames.size() - 1.0001) {
                elapsedFrames -= animationProfile.animation.frames.size() - 1.0001;
            }
        }
    }

    private boolean isAnimationEndReached() {
        // -1.0001 for double precision error
        return elapsedFrames >= animationProfile.animation.frames.size() - 1.0001;
    }

    public Vector3 getCenterOfMass() {
        return centerOfMass;
    }

    public void close() {
        carGroupStateSetter.close();
    }

    private double getTimeInSeconds() {
        final double currentTime = System.currentTimeMillis() / 1000.0;
        // The player likely paused the game.
        if(currentTime - previousTime > 0.5) {
            timeOffsetDueToPausing += currentTime - previousTime;
        }
        return currentTime - timeOffsetDueToPausing;
    }

    public List<Integer> getCarIndexesUsedForTheAnimation() {
        return carGroupStateSetter.getCarIndexesUsedForTheAnimation();
    }
}