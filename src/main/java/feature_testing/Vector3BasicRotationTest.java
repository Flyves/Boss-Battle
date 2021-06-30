package feature_testing;

import util.math.vector.Vector3;

public class Vector3BasicRotationTest {

    public static void main(String[] args) {
        Vector3 v = new Vector3(235, 455, 349);
        Vector3 rotator = new Vector3(-34, 42, -55);
        Vector3 result = v.rotate(rotator);

        System.out.println(result);
    }
}
