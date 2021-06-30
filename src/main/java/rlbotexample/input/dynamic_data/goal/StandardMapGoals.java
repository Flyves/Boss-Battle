package rlbotexample.input.dynamic_data.goal;

import util.game_constants.RlConstants;
import util.math.vector.Vector3;

public class StandardMapGoals {

    public static final GoalRegion blueGoal = new GoalRegion(
            new Vector3(893, -5120, 643),
            new Vector3(-893, -5120, 0));
    public static final GoalRegion orangeGoal = new GoalRegion(
            new Vector3(-893, 5120, 643),
            new Vector3(893, 5120, 0));

    public static GoalRegion getAlly(int teamId) {
        if(teamId == 0) {
            return blueGoal;
        }
        else {
            return orangeGoal;
        }
    }

    public static GoalRegion getOpponent(int teamId) {
        if(teamId == 0) {
            return orangeGoal;
        }
        else {
            return blueGoal;
        }
    }

}
