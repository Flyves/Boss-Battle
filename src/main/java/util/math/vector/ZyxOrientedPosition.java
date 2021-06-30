package util.math.vector;

import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import rlbotexample.input.dynamic_data.car.orientation.Orientation;

import java.io.Serializable;

public class ZyxOrientedPosition implements Serializable {
    public Vector3 position;
    public Vector3 eulerZYX;

    public ZyxOrientedPosition(Vector3 position, Vector3 eulerZYX) {
        this.position = position;
        this.eulerZYX = eulerZYX;
    }

    public CarOrientedPosition toCarOrientedPosition() {
        Vector3 rotatorZ = Vector3.UP_VECTOR.scaled(eulerZYX.z);
        Vector3 rotatorY = Vector3.Y_VECTOR.rotate(rotatorZ).scaled(eulerZYX.y);
        Vector3 rotatorX = Vector3.X_VECTOR.rotate(rotatorZ).rotate(rotatorY).scaled(eulerZYX.x);

        Vector3 nose = Vector3.X_VECTOR
                .rotate(rotatorZ)
                .rotate(rotatorY)
                .rotate(rotatorX);
        Vector3 roof = Vector3.UP_VECTOR
                .rotate(rotatorZ)
                .rotate(rotatorY)
                .rotate(rotatorX);

        return new CarOrientedPosition(position, new CarOrientation(nose, roof));
    }
}
