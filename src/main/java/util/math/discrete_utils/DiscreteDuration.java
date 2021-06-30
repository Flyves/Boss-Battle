package util.math.discrete_utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DiscreteDuration {

    // duration generally in seconds
    public final double duration;
    // resolution generally in samples/second
    public final double resolution;

    public DiscreteDuration(double duration, double resolution) {
        this.duration = duration;
        this.resolution = resolution;
    }

    public int amountOfSamples() {
        return (int) (duration*resolution);
    }

    public double timeAt(int sampleId) {
        return sampleId/resolution;
    }

    public void forEach(BiFunction<Integer, Double, Void> function) {
        for(int i = 0; i < amountOfSamples(); i++) {
            function.apply(i, timeAt(i));
        }
    }
}
