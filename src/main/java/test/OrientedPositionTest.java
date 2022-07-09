package test;

import org.junit.jupiter.api.Test;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;

public class OrientedPositionTest {

    @Test
    public void translatingFromZyxToOrientation_shouldYieldTheSameOrientation() {
        final OrientedPosition orientedPosition = new OrientedPosition(Vector3.generateRandomVector(), new Orientation().rotate(Vector3.generateRandomVector()));
        final OrientedPosition flipAndCanceled = orientedPosition.toZyxOrientedPosition().toCarOrientedPosition();
        assert flipAndCanceled.position.minus(orientedPosition.position).magnitude() < 0.0001;
        assert flipAndCanceled.orientation.asAngularDisplacement().minus(orientedPosition.orientation.asAngularDisplacement()).magnitude() < 0.0001;
    }

}
