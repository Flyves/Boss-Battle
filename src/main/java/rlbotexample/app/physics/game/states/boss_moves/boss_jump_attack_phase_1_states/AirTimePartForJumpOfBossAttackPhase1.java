package rlbotexample.app.physics.game.states.boss_moves.boss_jump_attack_phase_1_states;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.BossJumpAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.game_constants.RlConstants;
import util.math.vector.OrientedPosition;
import util.math.vector.Ray3;
import util.math.vector.Vector3;
import util.state_machine.State;

public class AirTimePartForJumpOfBossAttackPhase1 implements State {

    static final int ANIMATION_FRAME_AT_WHICH_THE_BOSS_STARTS_DESCENDING = 155;
    private static final double AIR_TIME_DURATION_IN_FRAMES = LandingPartForJumpOfBossAttackPhase1.ANIMATION_FRAME_AT_WHICH_THE_BOSS_LANDS
            - ANIMATION_FRAME_AT_WHICH_THE_BOSS_STARTS_DESCENDING;

    private final BossJumpAttackPhase1 bossJumpAttackPhase1;

    private Vector3 initialPosition = new Vector3();
    private Vector3 initialOrientation = new Vector3();
    private Vector3 groundDestination = new Vector3();
    private Vector3 orientationDestination = new Vector3();

    public AirTimePartForJumpOfBossAttackPhase1(BossJumpAttackPhase1 bossJumpAttackPhase1) {
        this.bossJumpAttackPhase1 = bossJumpAttackPhase1;
    }

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_jump);
        CurrentGame.bossAi.animator.setCurrentFrameIndex(ANIMATION_FRAME_AT_WHICH_THE_BOSS_STARTS_DESCENDING);
        CurrentGame.bossAi.animator.isLooping = false;

        initialPosition = CurrentGame.bossAi.orientedPosition.position;
        initialOrientation = CurrentGame.bossAi.orientedPosition.orientation.roofVector;

        Vector3 awkwardlyPredictedPlayerPosition = input.humanCar.position
                .plus(input.humanCar.velocity
                .scaled(AIR_TIME_DURATION_IN_FRAMES/RlConstants.BOT_REFRESH_RATE));
        Ray3 closestRayOfCarOnMap = new Ray3(awkwardlyPredictedPlayerPosition, input.humanCar.orientation.roofVector);//MapMeshGeometry.findClosestRay(awkwardlyPredictedPlayerPosition);
        groundDestination = closestRayOfCarOnMap.offset;
        orientationDestination = closestRayOfCarOnMap.direction;
    }

    @Override
    public void exec(DataPacket input) {
        double t = (CurrentGame.bossAi.animator.currentFrameIndex() - ANIMATION_FRAME_AT_WHICH_THE_BOSS_STARTS_DESCENDING) / AIR_TIME_DURATION_IN_FRAMES;
        CurrentGame.bossAi.orientedPosition.position = initialPosition.plus(groundDestination.minus(initialPosition)
                .scaled(t));
        CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation
                .rotate(CurrentGame.bossAi.orientedPosition.orientation.roofVector.findRotator(orientationDestination).scaled(1/AIR_TIME_DURATION_IN_FRAMES));
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(CurrentGame.bossAi.orientedPosition.orientation.roofVector.findRotator(orientationDestination));
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex()
                >= LandingPartForJumpOfBossAttackPhase1.ANIMATION_FRAME_AT_WHICH_THE_BOSS_LANDS) {
            return new LandingPartForJumpOfBossAttackPhase1(bossJumpAttackPhase1);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
