package rlbotexample.assets.animations;

import java.io.Serializable;
import java.util.List;

public class CarGroupAnimation implements Serializable {

    public final List<IndexedCarGroup> frames;

    public CarGroupAnimation(List<IndexedCarGroup> frames) {
        this.frames = frames;
    }

    public CarGroup queryFrame(int frameIndex) {
        if(frameIndex < frames.size()
                && frameIndex >= 0) {
            return frames.get(frameIndex).carGroup;
        }

        else if(frameIndex < 0) {
            return frames.get(0).carGroup;
        }
        return frames.get(frames.size()-1).carGroup;
    }
}
