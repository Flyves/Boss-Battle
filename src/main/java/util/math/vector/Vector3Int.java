package util.math.vector;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple 3d vector class with the most essential operations.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Vector3Int implements Serializable {

    public final int x;
    public final int y;
    public final int z;

    public Vector3Int(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 toVector3() {
        return new Vector3(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3Int that = (Vector3Int) o;
        return x == that.x &&
                y == that.y &&
                z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "[ x:" + this.x + ", y:" + this.y + ", z:" + this.z + " ]";
    }
}
