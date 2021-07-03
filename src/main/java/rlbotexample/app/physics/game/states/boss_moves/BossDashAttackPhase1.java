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

public class BossDashAttackPhase1 implements State {

    private Vector3 dashDirection = new Vector3();

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_dash_attack);
        CurrentGame.bossAi.animator.isLooping = false;
        CurrentGame.bossAi.orientedPosition.orientation = CurrentGame.bossAi.orientedPosition.orientation.rotate(new Vector3(0, 0, -Math.PI/2));
        dashDirection = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass)
                .scaled(1, 1, 0).normalized();
    }

    @Override
    public void exec(DataPacket input) {
        if(CurrentGame.bossAi.animator.currentFrameIndex() < 114-27) {
            CurrentGame.bossAi.orientedPosition.position = CurrentGame.bossAi.orientedPosition.position
                    .plus(dashDirection.scaledToMagnitude(
                            CurrentGame.BOSS_DASH_SPEED
                            - (CurrentGame.bossAi.animator.currentFrameIndex()*(CurrentGame.BOSS_DASH_SPEED/RlConstants.BOT_REFRESH_RATE)/(114.0-27))));

            List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation);
            boolean isBossCollidingWithPLayer = carsUsedForTheAnimation.stream()
                    .anyMatch(carData -> carData.hitBox.isCollidingWith(input.humanCar.hitBox));

            if(isBossCollidingWithPLayer) {
                CurrentGame.humanPlayer.takeDamage(input, 1, dashDirection.plus(new Vector3(0, 0, 0.5)).scaledToMagnitude(2200));
            }
        }
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {}

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
