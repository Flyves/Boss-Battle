package feature_testing;

import util.math.vector.Vector3;

public class CrossProductIntuition {

    public static void main(String[] args) {

        Vector3 a = new Vector3(1, 0, 0);
        Vector3 b = new Vector3(1, 1, 0);

        System.out.println(
                b.crossProduct(b.crossProduct(a))
        );
    }
}
