package util.data_structure;

import util.math.linear_transform.LinearTransform3;
import util.math.vector.Vector3;
import util.math.vector.Vector3Int;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Indexer3<T> {

    private final LinearTransform3 linearTransform;
    private final Map<Vector3Int, T> elements;

    public Indexer3(final LinearTransform3 linearTransform) {
        this.linearTransform = linearTransform;
        this.elements = new HashMap<>();
    }

    public void set(final Vector3 position, final T element) {
        final Vector3 indexDouble = linearTransform.compute(position);
        final Vector3Int index = indexDouble.toVector3Int();
        elements.put(index, element);
    }

    public T get(final Vector3 position) {
        final Vector3 indexDouble = linearTransform.compute(position);
        final Vector3Int index = indexDouble.toVector3Int();
        return elements.get(index);
    }

    public List<Vector3> inverse(T element) {
        return elements.entrySet().stream()
                .filter(e -> e.getValue().equals(element))
                .map(Map.Entry::getKey)
                .map(Vector3Int::toVector3)
                .map(linearTransform::inverse)
                .collect(Collectors.toList());
    }
}
