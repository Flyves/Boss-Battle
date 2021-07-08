package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.spin_attack_states.BeginSpinAttack;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;
import util.state_machine.StateMachine;

import java.util.List;

public class BossSpinAttackPhase1 implements State {

    private static final int FRAME_AT_WHICH_BOSS_STARTS_TO_DEAL_DAMAGE_ON_COLLISION = 125;
    private static final int FRAME_AT_WHICH_BOSS_STOPS_TO_DEAL_DAMAGE_ON_COLLISION = 501;

    private static final int ATTACK_DAMAGE = 1;

    private StateMachine spinAttackMachine = new StateMachine(new BeginSpinAttack(this));

    public int amountOfTimesAttackOccurred;

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.quadrupedal_beyblade);
        CurrentGame.bossAi.animator.looping(false);

        amountOfTimesAttackOccurred = 0;
    }

    @Override
    public void exec(DataPacket input) {
        spinAttackMachine.exec(input);

        if(CurrentGame.bossAi.animator.currentFrameIndex() >= FRAME_AT_WHICH_BOSS_STARTS_TO_DEAL_DAMAGE_ON_COLLISION
                && CurrentGame.bossAi.animator.currentFrameIndex() <= FRAME_AT_WHICH_BOSS_STOPS_TO_DEAL_DAMAGE_ON_COLLISION) {
            List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation);
            boolean isBossCollidingWithPLayer = carsUsedForTheAnimation.stream()
                    .anyMatch(carData -> carData.hitBox.isCollidingWith(input.humanCar.hitBox));

            Vector3 hitDirection = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass).normalized();

            if (isBossCollidingWithPLayer) {
                CurrentGame.humanPlayer.takeDamage(input, ATTACK_DAMAGE, hitDirection.plus(new Vector3(0, 0, 0.5)).scaledToMagnitude(3000));
            }
        }
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if (CurrentGame.bossAi.animator.isFinished()) {
            return new BossIdle3Phase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
