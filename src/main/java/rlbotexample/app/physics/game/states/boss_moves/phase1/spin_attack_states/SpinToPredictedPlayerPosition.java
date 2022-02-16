package rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossSpinAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.game_constants.RlConstants;
import util.math.linear_transform.LinearNormalizer;
import util.math.linear_transform.ParameterizedSegment;
import util.math.vector.Vector3;
import util.state_machine.State;

public class SpinToPredictedPlayerPosition implements State {

    private static final int AMOUNT_OF_FRAMES_TO_REACH_SPIN_DESTINATION = 80;
    private static final double TIME_TO_EXECUTE_MOVEMENT = AMOUNT_OF_FRAMES_TO_REACH_SPIN_DESTINATION * RlConstants.BOT_REFRESH_TIME_PERIOD;

    static final int FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER = 125;

    private LinearNormalizer animationAutomationClip;
    private ParameterizedSegment parameterizedTrajectory;

    private Vector3 spinDestination;

    private final BossSpinAttackPhase1 bossSpinAttackPhase1;

    public SpinToPredictedPlayerPosition(BossSpinAttackPhase1 bossSpinAttackPhase1) {
        this.bossSpinAttackPhase1 = bossSpinAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator.setCurrentFrameIndex(FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER);
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
                FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER + AMOUNT_OF_FRAMES_TO_REACH_SPIN_DESTINATION);
        animationAutomationClip.isBounded = true;

        Vector3 bossAnimationPosition = CurrentGame.bossAi.animator.orientedPosition.position;
        parameterizedTrajectory = new ParameterizedSegment(bossAnimationPosition, spinDestination);
    }

    @Override
    public void exec(DataPacket input) {
        double valueOfParameterizedAutomation = animationAutomationClip.compute(CurrentGame.bossAi.animator.currentFrameIndex());
        valueOfParameterizedAutomation = parameterizedSmoothedDisplacementFunction(valueOfParameterizedAutomation);

        CurrentGame.bossAi.orientedPosition.position = parameterizedTrajectory.compute(valueOfParameterizedAutomation);
        CurrentGame.bossAi.step(input);
    }

    static double parameterizedSmoothedDisplacementFunction(double x) {
        final double a = 0.6;
        final double b = -2 * Math.tan(-0.5/a);
        return (a * Math.atan(b * (x - 0.5))) + 0.5;
    }

    @Override
    public void stop(DataPacket input) {
        bossSpinAttackPhase1.amountOfTimesAttackOccurred++;
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex()
                >= FRAME_INDEX_AT_WHICH_BOSS_STARTS_TO_MOVE_TOWARDS_PLAYER + AMOUNT_OF_FRAMES_TO_REACH_SPIN_DESTINATION) {
            return new StayOnPlaceWhileSpinning(bossSpinAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
