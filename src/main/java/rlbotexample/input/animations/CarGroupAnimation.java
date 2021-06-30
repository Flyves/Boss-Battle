package rlbotexample.input.animations;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarGroupAnimation implements Serializable {

    public final List<IndexedCarGroup> frames;

    public CarGroupAnimation(List<IndexedCarGroup> frames) {
        this.frames = frames;
    }

    public CarGroup get(int frameIndex) {
        List<CarGroup> carMeshes = frames.stream()
                .filter(carMeshFrame -> carMeshFrame.frameIndex == frameIndex)
                .map(carMeshFrame -> carMeshFrame.carGroup)
                .collect(Collectors.toList());

        Optional<CarGroup> frameOpt = carMeshes.stream()
                .findFirst();

        if(frameOpt.isPresent()) {
            return frameOpt.get();
        }
        else if(frameIndex < 0) {
            return frames.get(0).carGroup;
        }
        return frames.get(frames.size()-1).carGroup;
    }
}
