package rlbotexample.input.prediction.ball;

import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.BallPrediction;
import rlbotexample.input.dynamic_data.ball.BallData;
import rlbotexample.input.dynamic_data.goal.StandardMapGoals;
import rlbotexample.input.prediction.Trajectory3D;
import util.math.vector.Vector3;

public class RawBallTrajectory {

    public static Trajectory3D trajectory;

    public static final int PREDICTION_REFRESH_RATE = 60;
    private static BallPrediction ballPrediction;

    public static void update(BallData ballData) {
        try {
            ballPrediction = RLBotDll.getBallPrediction();
        } catch (RLBotInterfaceException e) {
            // assuming the ball isn't moving and is centered in the field
            trajectory = time -> ballData.position;
        }
        trajectory = new Trajectory3D() {
            @Override
            public Vector3 apply(Double time) {
                return new Vector3(ballPrediction.slices(correspondingBallIndex(time)).physics().location());
            }
            @Override
            public Vector3 derivative(double time) {
                return new Vector3(ballPrediction.slices(correspondingBallIndex(time)).physics().velocity());
            }
        };
    }

    public static BallData ballAtTime(final double time) {
        if(ballPrediction == null) {
            return new BallData(trajectory.apply(0.0), new Vector3(), new Vector3(), time);
        }
        return new BallData(ballPrediction.slices(correspondingBallIndex(time)).physics(), time);
    }

    private static int correspondingBallIndex(final double deltaTime) {
        if((int) (PREDICTION_REFRESH_RATE * deltaTime) >= ballPrediction.slicesLength()) {
            return ballPrediction.slicesLength() - 1;
        }
        else if((int) (PREDICTION_REFRESH_RATE * deltaTime) < 0) {
            return 0;
        }
        return (int)(PREDICTION_REFRESH_RATE * deltaTime);
    }
}