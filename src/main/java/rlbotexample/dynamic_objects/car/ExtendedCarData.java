package rlbotexample.dynamic_objects.car;


import rlbotexample.dynamic_objects.car.orientation.Orientation;

/**
 * Basic information about the car.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class ExtendedCarData extends CarData {

    /** The orientation of the car */
    public final Orientation orientation;

    /** True if the car is driving on the ground, the wall, etc. In other words, true if you can steer. */
    public final boolean hasWheelContact;

    /** The jump output availabilities of the car. True if it has it, and false otherwise */
    public final boolean hasFirstJump;

    /** The jump usage of the car. True if the car has actively used its jump */
    public final boolean hasUsedFirstJump;
    public final boolean hasUsedSecondJump;

    /**
     * True if the car is showing the supersonic and can demolish enemies on contact.
     * This is a close approximation for whether the car is at max speed.
     */
    public final boolean isSupersonic;

    /**
     * 0 for blue team, 1 for orange team.
     */
    public final int team;

    public final boolean isDemolished;

    public final int playerIndex;

    public ExtendedCarData(rlbot.flat.PlayerInfo playerInfo, int playerIndex, float elapsedSeconds) {
        super(playerInfo, elapsedSeconds);
        this.playerIndex = playerIndex;
        this.orientation = Orientation.fromFlatbuffer(playerInfo);
        this.isSupersonic = playerInfo.isSupersonic();
        this.team = playerInfo.team();

        this.hasWheelContact = playerInfo.hasWheelContact();
        this.hasFirstJump = hasWheelContact;
        this.hasUsedFirstJump = playerInfo.jumped();
        this.hasUsedSecondJump = playerInfo.doubleJumped();

        this.isDemolished = playerInfo.isDemolished();
    }
}
