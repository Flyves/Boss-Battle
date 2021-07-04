package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;

import java.util.List;

import static rlbotexample.app.physics.game.CurrentGame.BOSS_DASH_SPEED;

public class BossDashAttackPhase1 implements State {

    private static final int AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH = 171;
    private static final int FRAME_INDEX_WHEN_DASH_ENDS = 229;
    private static final int DASH_DURATION = FRAME_INDEX_WHEN_DASH_ENDS - AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH;

    private static final int FRAME_FOR_BEGINNING_OF_REORIENTATION = 115;
    private static final int FRAME_FOR_ENDING_OF_REORIENTATION = 135;
    private static final double MAX_SPIN_SPEED = 4;

    private static final int ATTACK_DAMAGE = 3;

    private Vector3 dashDirection = new Vector3();

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_dash_attack);
        CurrentGame.bossAi.animator.isLooping = false;
        CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(new Vector3(0, 0, -Math.PI/2));
    }

    @Override
    public void exec(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex() - AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH < DASH_DURATION
                && CurrentGame.bossAi.animator.currentFrameIndex() > AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH) {
            CurrentGame.bossAi.orientedPosition.position = CurrentGame.bossAi.orientedPosition.position
                    .plus(dashDirection.scaledToMagnitude(
                            BOSS_DASH_SPEED
                            - ((CurrentGame.bossAi.animator.currentFrameIndex() - AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH) * (BOSS_DASH_SPEED/RlConstants.BOT_REFRESH_RATE)/(DASH_DURATION))));

            List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation);
            boolean isBossCollidingWithPLayer = carsUsedForTheAnimation.stream()
                    .anyMatch(carData -> carData.hitBox.isCollidingWith(input.humanCar.hitBox));

            if(isBossCollidingWithPLayer) {
                CurrentGame.humanPlayer.takeDamage(input, ATTACK_DAMAGE, dashDirection.plus(new Vector3(0, 0, 0.5)).scaledToMagnitude(2200));
            }
        }
        else if(CurrentGame.bossAi.animator.currentFrameIndex() >= FRAME_FOR_BEGINNING_OF_REORIENTATION
                && CurrentGame.bossAi.animator.currentFrameIndex() < FRAME_FOR_ENDING_OF_REORIENTATION) {
            CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(new Vector3(0, 0, Math.PI/2));
            Vector3 vectorFromBossToPlayer = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass);
            Vector3 noseDestination = vectorFromBossToPlayer.scaled(1, 1, 0).normalized().scaled(-1);
            Vector3 spin = CurrentGame.bossAi.orientedPosition.orientation.noseVector
                    .findRotator(noseDestination)
                    .scaledToMagnitude(MAX_SPIN_SPEED/RlConstants.BOT_REFRESH_RATE);
            if(CurrentGame.bossAi.orientedPosition.orientation.noseVector
                    .findRotator(noseDestination)
                    .magnitude()
                    < Math.PI/10) {
                spin = new Vector3();
            }
            CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(spin);
            CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(new Vector3(0, 0, -Math.PI/2));
            dashDirection = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass)
                    .scaled(1, 1, 0).normalized();
        }
        else if(CurrentGame.bossAi.animator.currentFrameIndex() == FRAME_FOR_ENDING_OF_REORIENTATION) {
            Vector3 vectorFromBossToPlayer = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass);
            dashDirection = vectorFromBossToPlayer.plus(input.humanCar.velocity.scaled(
                    ((AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH - FRAME_FOR_ENDING_OF_REORIENTATION)/RlConstants.BOT_REFRESH_RATE)
                            + vectorFromBossToPlayer.magnitude()/(BOSS_DASH_SPEED*RlConstants.BOT_REFRESH_RATE))
                    .scaled(1.4))
                    .scaled(1, 1, 0).normalized();
            Vector3 noseDestination = dashDirection.scaled(1, 1, 0).normalized().scaled(-1);
            CurrentGame.bossAi.orientedPosition.orientation.noseVector = noseDestination;
            CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(new Vector3(0, 0, -Math.PI/2));
        }
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossRunPhase1();
        }
        /*
      TODO
        if(bossAiHitsAWall()) {
            return new DashIntoWallOuchPhase1();
        }*/
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
