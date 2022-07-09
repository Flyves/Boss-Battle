package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.phase1.spin_attack_states.BeginSpinAttack;
import rlbotexample.asset.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;
import util.state_machine.StateMachine;
import util.tinysound.TinySound;

import java.util.List;

public class BossSpinAttackPhase1 implements State {
    private static final int FRAME_AT_WHICH_BOSS_STARTS_TO_DEAL_DAMAGE_ON_COLLISION = 125;
    private static final int FRAME_AT_WHICH_BOSS_STOPS_TO_DEAL_DAMAGE_ON_COLLISION = 501;

    private static final int ATTACK_DAMAGE = 1;

    private StateMachine spinAttackMachine = new StateMachine(new BeginSpinAttack(this));

    public int amountOfTimesAttackOccurred;

    public AnimationPlayer animationPlayer;

    public OrientedPosition orientedPosition;

    public BossSpinAttackPhase1(final OrientedPosition initialOrientedPosition) {
        this.orientedPosition = initialOrientedPosition;
        this.animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.quadrupedal_beyblade)
                .withAnimationOffset(this::orientedPositionFunction)
                .withRigidity(this::rigidityFunction)
                .build());
    }

    @Override
    public void start(DataPacket input) {
        amountOfTimesAttackOccurred = 0;
        TinySound.init();
        TinySound.loadSound(GameSoundFiles.helicopter_buildup).play(0.2);

        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {
        spinAttackMachine.exec(input);

        if(animationPlayer.getCurrentAnimationFrame() >= FRAME_AT_WHICH_BOSS_STARTS_TO_DEAL_DAMAGE_ON_COLLISION
                && animationPlayer.getCurrentAnimationFrame() <= FRAME_AT_WHICH_BOSS_STOPS_TO_DEAL_DAMAGE_ON_COLLISION) {
            List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(input, animationPlayer.getCarIndexesUsedForTheAnimation());
            final boolean isBossCollidingWithPLayer = carsUsedForTheAnimation.stream()
                    .anyMatch(carData -> carData.hitBox.isCollidingWith(input.humanCar.hitBox));
            final Vector3 hitDirection = input.humanCar.position.minus(animationPlayer.getCenterOfMass()).normalized();
            if (isBossCollidingWithPLayer) {
                CurrentGame.humanPlayer.takeDamage(input, ATTACK_DAMAGE, hitDirection.plus(new Vector3(0, 0, 0.5)).scaledToMagnitude(3000));
            }
        }
        BallStateSetter.setTarget(animationPlayer.getCenterOfMass());
    }

    private OrientedPosition orientedPositionFunction() {
        return orientedPosition;
    }

    private Double rigidityFunction(final Integer frameIndex) {
        return 0.2 + frameIndex/180.0;
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if (animationPlayer.isFinished()) {
            return new BossIdle3Phase1(orientedPosition);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
