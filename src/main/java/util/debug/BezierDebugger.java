package util.debug;

import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.DataPacket;
import util.bezier_curve.PathIterator;
import util.bezier_curve.CurveSegment;
import util.math.vector.Vector3;

import java.awt.*;

public class BezierDebugger {

    private static Vector3 currentCursorPosition = new Vector3(-11480, -20580, 2700);

    private static final double CURVE_LENGTH_INCREMENT = 1;

    public static void renderPath(CurveSegment myPath, Color color, Renderer renderer) {
        PathIterator bezierIterator = new PathIterator(myPath, myPath.curveLength(50)/300, 10);
        Vector3 testLastPosition;
        Vector3 testCurrentPosition = myPath.interpolate(0);

        //Create a new rendering group

        int i = 0;
        int j = 0;
        while(bezierIterator.hasNext()) {
            testLastPosition = testCurrentPosition;
            testCurrentPosition = bezierIterator.next();
            renderer.drawLine3d(color, testLastPosition.toFlatVector(), testCurrentPosition.toFlatVector());
        }
    }

    public static void renderPositionControlledByCar(DataPacket input, Color color, Renderer renderer) {
        Vector3 distanceIncrement = new Vector3();

        if(input.allCars.get(input.allCars.size()-1-input.botIndex).position.minus(new Vector3(0, 0, 450.2)).magnitude() > 500) {
            distanceIncrement = input.allCars.get(input.allCars.size()-1-input.botIndex).position.scaled(0.02);
        }

        if(input.allCars.get(input.allCars.size()-1-input.botIndex).orientation.roofVector.z < -0.5) {
            distanceIncrement = new Vector3(0, 0, -10);
        }
        currentCursorPosition = currentCursorPosition.plus(distanceIncrement);
        renderer.drawLine3d(color, input.allCars.get(input.allCars.size()-1-input.botIndex).position.toFlatVector(), currentCursorPosition.toFlatVector());
        System.out.println("cursor: " + currentCursorPosition);
    }
}
