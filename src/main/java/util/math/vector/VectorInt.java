package util.math.vector;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VectorInt implements Serializable {

    private final Integer e[];

    public VectorInt(Integer... e) {
        this.e = e;
    }

    public Vector toVector() {
        Double[] convertedNumbers = new Double[e.length];
        Arrays.stream(e)
                .map(Integer::doubleValue)
                .collect(Collectors.toList())
                .toArray(convertedNumbers);

        return new Vector(convertedNumbers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorInt that = (VectorInt) o;
        if(this.e.length != that.e.length) return false;
        for(int i = 0; i < this.e.length; i++) {
            if(!this.e[i].equals(that.e[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) e);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ ");
        Arrays.stream(e)
                .forEach(i -> stringBuilder.append(i)
                        .append(", "));
        int indexOfPortionToReplace = stringBuilder.lastIndexOf(", ");
        stringBuilder.replace(indexOfPortionToReplace, indexOfPortionToReplace+2, " ]");
        return stringBuilder.toString();
    }
}
