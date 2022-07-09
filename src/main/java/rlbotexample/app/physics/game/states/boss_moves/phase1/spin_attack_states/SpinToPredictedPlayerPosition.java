package rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossSpinAttackPhase1;
import rlbotexample.asset.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import util.game_constants.RlConstants;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.Vector3;
import util.state_machine.State;
import util.tinysound.TinySound;

public class SpinToPredictedPlayerPosition implements State {

    private static int amountOfFramesToReachSpinDestination = findAmountOfFramesToReach();
    private static final double TIME_TO_EXECUTE_MOVEMENT = amountOfFramesToReachSpinDestination * RlConstants.BOT_REFRESH_TIME_PERIOD;

    static final int FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER = 125;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;

    private Vector3 spinDestination;

    private final BossSpinAttackPhase1 bossSpinAttackPhase1;

    public SpinToPredictedPlayerPosition(final BossSpinAttackPhase1 bossSpinAttackPhase1) {
        this.bossSpinAttackPhase1 = bossSpinAttackPhase1;
    }

    @Override
    public void start(final DataPacket input) {
        amountOfFramesToReachSpinDestination = findAmountOfFramesToReach();
        bossSpinAttackPhase1.animationPlayer.setCurrentAnimationFrame(FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER);
        Vector3 distanceFromSpeedPrediction = input.humanCar.velocity.scaled(TIME_TO_EXECUTE_MOVEMENT);
        spinDestination = input.humanCar.position.plus(distanceFromSpeedPrediction)
            .minus(new Vector3(0, 0, 50));

        final double offset = 200;
        if(spinDestination.z < -50) {
            spinDestination.z = -50;
        }
        else if(spinDestination.z > 1700) {
            spinDestination.z = 1700;
        }
        if(spinDestination.x > RlConstants.WALL_DISTANCE_X - offset) {
            spinDestination.x = RlConstants.WALL_DISTANCE_X - offset;
        }
        else if(spinDestination.x < -RlConstants.WALL_DISTANCE_X + offset) {
            spinDestination.x = -RlConstants.WALL_DISTANCE_X + offset;
        }
        if(spinDestination.y > RlConstants.WALL_DISTANCE_Y - offset) {
            spinDestination.y = RlConstants.WALL_DISTANCE_Y - offset;
        }
        else if(spinDestination.y < -RlConstants.WALL_DISTANCE_Y + offset) {
            spinDestination.y = -RlConstants.WALL_DISTANCE_Y + offset;
        }

        animationAutomationClip = new LinearNormalizer(
                FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER,
                FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER + amountOfFramesToReachSpinDestination);
        animationAutomationClip.isBounded = true;

        Vector3 bossAnimationPosition = bossSpinAttackPhase1.orientedPosition.position;
        parameterizedTrajectory = new ParameterizedSegment(bossAnimationPosition, spinDestination);
        TinySound.loadSound(GameSoundFiles.helicopter_attack).play(0.2);
    }

    @Override
    public void exec(final DataPacket input) {
        double valueOfParameterizedAutomation = animationAutomationClip.compute(bossSpinAttackPhase1.animationPlayer.getCurrentAnimationFrame());
        valueOfParameterizedAutomation = parameterizedSmoothedDisplacementFunction(valueOfParameterizedAutomation);

        bossSpinAttackPhase1.orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
    }

    static double parameterizedSmoothedDisplacementFunction(final double x) {
        final double a = 0.6;
        final double b = -2 * Math.tan(-0.5/a);
        return (a * Math.atan(b * (x - 0.5))) + 0.5;
    }

    @Override
    public void stop(final DataPacket input) {
        bossSpinAttackPhase1.amountOfTimesAttackOccurred++;
    }

    @Override
    public State next(final DataPacket input) {
        if(bossSpinAttackPhase1.animationPlayer.getCurrentAnimationFrame()
                >= FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER + amountOfFramesToReachSpinDestination) {
            return new StayOnPlaceWhileSpinning(bossSpinAttackPhase1);
        }
        return this;
    }

    private static int findAmountOfFramesToReach() {
        switch (GameOptions.gameDifficulty) {
            case ROCKET_SLEDGE: return 4;
            case TRIVIAL: return 180;
            case EASY: return 160;
            case MEDIUM: return 140;
            case HARD: return 120;
            case EXPERT: return 100;
            case IMPOSSIBLE: return 90;
            case WTF: return 80;
        }
        throw new RuntimeException("No game difficulty selected!");
    }

    @Override
    public void debug(DataPacket input) {

    }
}
