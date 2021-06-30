package rlbotexample.input.dynamic_data.car;


import rlbotexample.input.dynamic_data.RlUtils;
import rlbotexample.input.dynamic_data.car.orientation.CarOrientation;
import util.game_constants.RlConstants;

/**
 * Basic information about the car.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class ExtendedCarData extends CarData {

    /** The orientation of the car */
    public final CarOrientation orientation;

    /** True if the car is driving on the ground, the wall, etc. In other words, true if you can steer. */
    public final boolean hasWheelContact;

    /** The jump output availabilities of the car. True if it has it, and false otherwise */
    public final boolean hasFirstJump;
    public final boolean hasSecondJump;

    /** The jump usage of the car. True if the car has actively used its jump */
    public final boolean hasUsedFirstJump;
    public final boolean hasUsedSecondJump;
    public final boolean previousSecondJumpUsage;
    public final boolean hasJustUsedSecondJump;

    /** Counter for the amount of time that has elapsed since the last jump from the ground */
    public final int framesSinceFirstJumpOccurred;

    /** Amount of boost that the player had last frame */
    public final double previousBoost;

    /** Value that represents the average amount of boost the player is currently using.
     *  It can be useful for approximations of aerial trajectories when the player is pulsing its boost usage.
     */
    public final double averageBoostUsage;

    /**
     * True if the car is showing the supersonic and can demolish enemies on contact.
     * This is a close approximation for whether the car is at max speed.
     */
    public final boolean isSupersonic;

    /**
     * 0 for blue team, 1 for orange team.
     */
    public final int team;

    public final int playerIndex;

    public ExtendedCarData(rlbot.flat.PlayerInfo playerInfo, int playerIndex, float elapsedSeconds) {
        super(playerInfo, elapsedSeconds);
        this.playerIndex = playerIndex;
        this.orientation = CarOrientation.fromFlatbuffer(playerInfo);
        this.isSupersonic = playerInfo.isSupersonic();
        this.team = playerInfo.team();

        this.hasWheelContact = playerInfo.hasWheelContact();
        this.framesSinceFirstJumpOccurred = RlUtils.getPreviousAmountOfFramesSinceFirstJumpOccurred(playerIndex);
        this.hasFirstJump = hasWheelContact;
        this.hasUsedFirstJump = playerInfo.jumped();
        this.hasSecondJump = !playerInfo.doubleJumped() && hasInfiniteJump(hasUsedFirstJump);
        this.hasUsedSecondJump = playerInfo.doubleJumped();
        this.previousSecondJumpUsage = RlUtils.getPreviousSecondJumpUsage(playerIndex);
        RlUtils.setPreviousSecondJumpUsage(playerIndex, hasUsedSecondJump);
        this.hasJustUsedSecondJump = hasUsedSecondJump && !previousSecondJumpUsage;

        this.previousBoost = RlUtils.getPreviousAmountOfBoost(playerIndex);
        this.averageBoostUsage = averageBoostConsumption();
    }

    private boolean hasInfiniteJump(boolean hasJumped) {
        if(hasJumped) {
            RlUtils.setPreviousAmountOfFramesSinceFirstJumpOccurred(playerIndex, framesSinceFirstJumpOccurred+1);
        }
        else {
            RlUtils.setPreviousAmountOfFramesSinceFirstJumpOccurred(playerIndex, 0);
        }
        return framesSinceFirstJumpOccurred < RlConstants.AMOUNT_OF_TIME_BEFORE_LOSING_SECOND_JUMP*RlConstants.BOT_REFRESH_RATE;
    }

    private double averageBoostConsumption() {
        try {
            RlUtils.setPreviousAmountOfBoost(playerIndex, boost);

            return RlUtils.getAverageBoostUsage(playerIndex);
        }
        catch (Exception ignored) {}

        return 0;
    }
}
