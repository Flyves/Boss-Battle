package util.data_structure;

import util.math.linear_transform.LinearTransform2;
import util.math.vector.Vector2;
import util.math.vector.Vector2Int;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Indexer2<T> implements Serializable {

    private final LinearTransform2 linearTransform;
    private final Map<Vector2Int, T> elements;

    public Indexer2(final LinearTransform2 linearTransform) {
        this.linearTransform = linearTransform;
        this.elements = new HashMap<>();
    }

    public void set(final Vector2 position, final T element) {
        final Vector2 indexDouble = linearTransform.compute(position);
        final Vector2Int index = indexDouble.toVector2Int();
        elements.put(index, element);
    }

    public T get(final Vector2 position) {
        final Vector2 indexDouble = linearTransform.compute(position);
        final Vector2Int index = indexDouble.toVector2Int();
        return elements.get(index);
    }

    public List<Vector2> inverse(T element) {
        return elements.entrySet().stream()
                .filter(e -> e.getValue().equals(element))
                .map(Map.Entry::getKey)
                .map(Vector2Int::toVector2)
                .map(linearTransform::inverse)
                .collect(Collectors.toList());
    }
}
