package util.math.vector;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Vector {

    public Double[] e;

    public Vector(Double... e) {
        this.e = e;
    }

    public VectorInt toVectorInt() {
        Integer[] convertedNumbers = new Integer[e.length];
        Arrays.stream(e)
                .map(Double::intValue)
                .collect(Collectors.toList())
                .toArray(convertedNumbers);
        return new VectorInt(convertedNumbers);
    }
}
