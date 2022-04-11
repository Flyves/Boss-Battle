package rlbotexample.asset.animation.discrete_player;

import java.io.Serializable;

public class IndexedCarGroup implements Serializable {

    public final CarGroup carGroup;
    public final int frameIndex;

    public IndexedCarGroup(int index) {
        this.carGroup = new CarGroup();
        this.frameIndex = index;
    }
}
