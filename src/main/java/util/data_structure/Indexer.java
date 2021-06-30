package util.data_structure;

import util.math.linear_transform.LinearTransform;
import util.math.vector.Vector;
import util.math.vector.VectorInt;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Indexer<T> implements Serializable {

    private final LinearTransform linearTransform;
    private final Map<VectorInt, T> elements;

    public Indexer(final LinearTransform linearTransform) {
        this.linearTransform = linearTransform;
        this.elements = new HashMap<>();
    }

    public void set(final Double[] position, final T element) {
        final Double[] indexDouble = linearTransform.compute(position);
        final VectorInt index = new Vector(indexDouble).toVectorInt();
        elements.put(index, element);
    }

    public T get(final Double[] position) {
        final Double[] indexDouble = linearTransform.compute(position);
        final VectorInt index = new Vector(indexDouble).toVectorInt();
        return elements.get(index);
    }

    public List<Double[]> inverse(T element) {
        return elements.entrySet().stream()
                .filter(e -> e.getValue().equals(element))
                .map(Map.Entry::getKey)
                .map(VectorInt::toVector)
                .map(v -> linearTransform.inverse(v.e))
                .collect(Collectors.toList());
    }
}
