package rlbotexample.input.dynamic_data;

import rlbotexample.input.dynamic_data.boost.BoostManager;
import rlbotexample.input.dynamic_data.boost.BoostPad;
import rlbotexample.input.dynamic_data.ball.BallData;
import rlbotexample.input.dynamic_data.car.CarData;
import util.game_constants.RlConstants;
import util.math.MovingAverage;
import util.timer.Timer;
import util.math.vector.Vector3;

import java.util.*;
import java.util.List;

public class RlUtils {

    public static final double BALL_PREDICTION_TIME = 5;
    public static final double BALL_PREDICTION_REFRESH_RATE = 120;

    private static final List<Integer> amountOfFramesSinceFirstJumpOccurredForAllPlayers = new ArrayList<>();
    private static final List<Double> previousBoostAmountForAllPlayers = new ArrayList<>();
    private static final List<MovingAverage> averageBoostUsageForAllPlayers = new ArrayList<>();
    private static final List<Boolean> previousSecondJumpUsageForAllPlayers = new ArrayList<>();
    private static final int amountOfFramesForTheAverage = (int) (0.2*RlConstants.BOT_REFRESH_RATE);

    private static final List<Timer> boostPadTimers = new ArrayList<>(40);

    public static int getPreviousAmountOfFramesSinceFirstJumpOccurred(int playerIndex) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                return amountOfFramesSinceFirstJumpOccurredForAllPlayers.get(playerIndex);
            } catch (Exception e) {
                amountOfFramesSinceFirstJumpOccurredForAllPlayers.add(0);
                try {
                    return amountOfFramesSinceFirstJumpOccurredForAllPlayers.get(playerIndex);
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();
    }

    public static void setPreviousAmountOfFramesSinceFirstJumpOccurred(int playerIndex, int framesSinceFirstJumpOccurred) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                amountOfFramesSinceFirstJumpOccurredForAllPlayers.set(playerIndex, framesSinceFirstJumpOccurred);
                return;
            } catch (Exception e) {
                amountOfFramesSinceFirstJumpOccurredForAllPlayers.add(0);
                try {
                    amountOfFramesSinceFirstJumpOccurredForAllPlayers.set(playerIndex, framesSinceFirstJumpOccurred);
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();
    }

    public static double getPreviousAmountOfBoost(int playerIndex) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                return previousBoostAmountForAllPlayers.get(playerIndex);
            }
            catch (Exception e) {
                previousBoostAmountForAllPlayers.add(0d);
                try {
                    return previousBoostAmountForAllPlayers.get(playerIndex);
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();
    }

    public static void setPreviousAmountOfBoost(int playerIndex, double boostAmount) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                averageBoostUsageForAllPlayers.get(playerIndex)
                        .update(
                                previousBoostAmountForAllPlayers.get(playerIndex) - boostAmount < -1 ?
                                RlConstants.ACCELERATION_DUE_TO_BOOST_IN_AIR : 0
                        );
                previousBoostAmountForAllPlayers.set(playerIndex, boostAmount);
                return;
            } catch (Exception e) {
                averageBoostUsageForAllPlayers.add(new MovingAverage(amountOfFramesForTheAverage));
                previousBoostAmountForAllPlayers.add(0d);
                try {
                    averageBoostUsageForAllPlayers.get(playerIndex)
                            .update(
                                    previousBoostAmountForAllPlayers.get(playerIndex) - boostAmount < -1 ?
                                    RlConstants.ACCELERATION_DUE_TO_BOOST_IN_AIR : 0
                            );
                    previousBoostAmountForAllPlayers.set(playerIndex, boostAmount);
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();
    }

    public static double getAverageBoostUsage(int playerIndex) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                return averageBoostUsageForAllPlayers.get(playerIndex).value;
            }
            catch (Exception e) {
                averageBoostUsageForAllPlayers.add(new MovingAverage(amountOfFramesForTheAverage));
                try {
                    return averageBoostUsageForAllPlayers.get(playerIndex).value;
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();

    }

    public static boolean getPreviousSecondJumpUsage(int playerIndex) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                return previousSecondJumpUsageForAllPlayers.get(playerIndex);
            }
            catch (Exception e) {
                previousSecondJumpUsageForAllPlayers.add(false);
                try {
                    return previousSecondJumpUsageForAllPlayers.get(playerIndex);
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();
    }

    public static void setPreviousSecondJumpUsage(int playerIndex, boolean hasUsedSecondJump) {
        for(int i = 0; i <= playerIndex; i++){
            try {
                previousSecondJumpUsageForAllPlayers.set(playerIndex, hasUsedSecondJump);
                return;
            } catch (Exception e) {
                previousSecondJumpUsageForAllPlayers.add(false);
                try {
                    previousSecondJumpUsageForAllPlayers.set(playerIndex, hasUsedSecondJump);
                }
                catch (Exception ignored) {}
            }
        }
        throw new RuntimeException();
    }

    public static void updateBoostPadReloadingTimer(BoostPad pad) {
        if(boostPadTimers.size() == pad.boostId) {
            boostPadTimers.add(new Timer(pad.isBigBoost ?
                    BoostManager.BIG_BOOST_RELOAD_TIME :
                    BoostManager.SMALL_BOOST_RELOAD_TIME));
        }

        // wtf
        if(pad.boostId > boostPadTimers.size()) {
            return;
        }

        // get the timer
        Timer currentReloadTimer = boostPadTimers.get(pad.boostId);
        // if pad is taken
        if(!pad.isActive) {
            // if timer hasn't started yet
            if(currentReloadTimer.isTimeElapsed()) {
                // start it
                currentReloadTimer.start();
            }
        }
        // if the boost is active
        else {
            // elapse the counter
            currentReloadTimer.end();
        }
    }

    public static double getTimeBeforePadReloads(BoostPad pad) {
        // wtf #2
        if(pad.boostId >= boostPadTimers.size()) {
            return 0;
        }

        return boostPadTimers.get(pad.boostId).remainingTime();
    }
}
