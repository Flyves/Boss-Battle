package rlbotexample.input.dynamic_data;

import rlbot.flat.GameTickPacket;
import rlbotexample.input.dynamic_data.boost.BoostManager;
import rlbotexample.input.dynamic_data.ball.BallData;
import rlbotexample.input.dynamic_data.car.ExtendedCarData;
import rlbotexample.input.prediction.ball.RawBallTrajectory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it. The benefits of using this instead of rlbot.flat.GameTickPacket are:
 * 1. You end up with nice custom Vector3 objects that you can call methods on.
 * 2. If the framework changes its data format, you can just update the code here
 * and leave your bot logic alone.
 */
public class DataPacket {

    /** Your own car, based on the playerIndex */
    public final ExtendedCarData car;
    public ExtendedCarData humanCar = null;

    public final List<ExtendedCarData> allCars;

    public final BallData ball;
    public final int team;

    /** The index of your player */
    public final int botIndex;
    public int humanIndex = 0;

    /** The index of the bot that is going to reload the ball prediction (if there is many bots) */
    public static final AtomicInteger indexOfBotThatLoadsData = new AtomicInteger(-1);

    private static volatile boolean dataLoaded = false;

    public DataPacket(GameTickPacket request, int playerIndex) {
        this.botIndex = playerIndex;
        this.allCars = new ArrayList<>();
        for (int i = 0; i < request.playersLength(); i++) {
            final rlbot.flat.PlayerInfo playerInfo = request.players(i);
            final float elapsedSeconds = request.gameInfo().secondsElapsed();
            allCars.add(new ExtendedCarData(playerInfo, i, elapsedSeconds));
            if(!request.players(i).isBot()) {
                humanIndex = i;
                humanCar = allCars.get(i);
            }
        }
        if(humanCar.playerIndex == request.playersLength()-1) {
            indexOfBotThatLoadsData.set(humanIndex-1);
        }
        else {
            indexOfBotThatLoadsData.set(humanIndex+1);
        }

        this.car = allCars.get(playerIndex);
        this.team = this.car.team;
        this.ball = new BallData(request.ball());

        // omg
        if(indexOfBotThatLoadsData.get() == playerIndex) {
            loadData(request);
            dataLoaded = true;
        }
    }

    private void loadData(GameTickPacket request) {
        // update the "raw" ball trajectory
        RawBallTrajectory.update(ball);

        // refresh boostPads information so we can utilize it
        BoostManager.loadGameTickPacket(request);
    }
}
