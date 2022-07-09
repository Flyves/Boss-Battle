package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.asset.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.state_machine.State;
import util.tinysound.Sound;
import util.tinysound.TinySound;

import javax.xml.ws.Holder;
import java.util.List;

public class BossDashAttackPhase1 implements State {

    private static final int AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH = 171;
    private static final int FRAME_INDEX_WHEN_DASH_ENDS = 229;
    private static final int DASH_DURATION = FRAME_INDEX_WHEN_DASH_ENDS - AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH;

    private static final int FRAME_FOR_BEGINNING_OF_REORIENTATION = 10;
    private static final int FRAME_FOR_ENDING_OF_REORIENTATION = 135;

    private static final int ATTACK_DAMAGE = 3;

    private Vector3 dashDirection = new Vector3();
    private Sound shootingSound;
    private AnimationPlayer animationPlayer;
    private OrientedPosition orientedPosition;
    private Holder<DataPacket> inputHolder;

    public BossDashAttackPhase1(final OrientedPosition initialPosition) {
        this.orientedPosition = initialPosition;
    }

    @Override
    public void start(DataPacket input) {
        this.inputHolder = new Holder<>(input);
        this.animationPlayer = new AnimationPlayer(new AnimationProfileBuilder()
                .withAnimation(GameAnimations.boss_dash_attack)
                .withRigidity(this::rigidityFunction)
                .withAnimationOffset(this::orientedPositionFunction)
                .withFrameEvent(19, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.06))
                .withFrameEvent(27, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.06))
                .withFrameEvent(32, () -> TinySound.loadSound(GameSoundFiles.leg_step_2).play(0.06))
                .withFrameEvent(37, () -> TinySound.loadSound(GameSoundFiles.leg_step_4).play(0.06))
                .withFrameEvent(54, () -> TinySound.loadSound(GameSoundFiles.leg_step_2).play(0.06))
                .withFrameEvent(62, () -> TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.06))
                .withFrameEvent(67, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.06))
                .withFrameEvent(72, () -> TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.06))
                .withFrameEvent(88, () -> TinySound.loadSound(GameSoundFiles.leg_step_2).play(0.06))
                .withFrameEvent(96, () -> TinySound.loadSound(GameSoundFiles.leg_step_4).play(0.06))
                .withFrameEvent(100, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.06))
                .withFrameEvent(106, () -> TinySound.loadSound(GameSoundFiles.leg_step_3).play(0.06))
                .withFrameEvent(123, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.06))
                .withFrameEvent(131, () -> TinySound.loadSound(GameSoundFiles.leg_step_4).play(0.06))
                .withFrameEvent(136, () -> TinySound.loadSound(GameSoundFiles.leg_step_0).play(0.06))
                .withFrameEvent(141, () -> TinySound.loadSound(GameSoundFiles.leg_step_1).play(0.06))
                .withFrameEvent(177, () -> {
                    TinySound.shutdown();
                    TinySound.init();
                    shootingSound = TinySound.loadSound(GameSoundFiles.dash_shooting);
                    shootingSound.play(0.1);
                })
                .withFinishingFunction(() -> isBossDashOutOfBound(animationPlayer.getCenterOfMass()))
                .build());
        AnimationTasks.append(animationPlayer);

        TinySound.init();
        Sound buildupSound = TinySound.loadSound(GameSoundFiles.dash_buildup);
        buildupSound.play(0.1);
    }

    @Override
    public void exec(DataPacket input) {
        inputHolder.value = input;
        BallStateSetter.setTarget(animationPlayer.getCenterOfMass());
    }

    private double rigidityFunction(final int frameIndex) {
        return 0.2 + frameIndex/180.0;
    }

    private OrientedPosition orientedPositionFunction() {
        if(animationPlayer.getCurrentAnimationFrame() - AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH < DASH_DURATION
                && animationPlayer.getCurrentAnimationFrame() > AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH) {
            orientedPosition.position = orientedPosition.position
                    .plus(dashDirection.scaledToMagnitude(
                            findDashSpeed()
                                    - ((animationPlayer.getCurrentAnimationFrame() - AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH) * (findDashSpeed()/RlConstants.BOT_REFRESH_RATE)/(DASH_DURATION))));

            final List<ExtendedCarData> carsUsedForTheAnimation = CarResourceHandler.dereferenceIndexes(inputHolder.value, animationPlayer.getCarIndexesUsedForTheAnimation());
            boolean isBossCollidingWithPLayer = carsUsedForTheAnimation.stream()
                    .anyMatch(carData -> carData.hitBox.isCollidingWith(inputHolder.value.humanCar.hitBox));

            if(isBossCollidingWithPLayer) {
                CurrentGame.humanPlayer.takeDamage(inputHolder.value, ATTACK_DAMAGE, dashDirection.plus(new Vector3(0, 0, 0.5)).scaledToMagnitude(2200));
            }
        }
        else if(animationPlayer.getCurrentAnimationFrame() >= FRAME_FOR_BEGINNING_OF_REORIENTATION
                && animationPlayer.getCurrentAnimationFrame() < FRAME_FOR_ENDING_OF_REORIENTATION) {
            Vector3 vectorFromBossToPlayer = inputHolder.value.humanCar.position.minus(animationPlayer.getCenterOfMass());
            dashDirection = vectorFromBossToPlayer.plus(inputHolder.value.humanCar.velocity.scaled(
                                    ((AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH - FRAME_FOR_ENDING_OF_REORIENTATION)/RlConstants.BOT_REFRESH_RATE)
                                            + vectorFromBossToPlayer.magnitude()/(findDashSpeed()*RlConstants.BOT_REFRESH_RATE))
                            .scaled(1.4))
                    .scaled(1, 1, 0).normalized();
            final Vector3 noseDestination = dashDirection.scaled(1, 1, 0).normalized().scaled(-1);
            orientedPosition.orientation.noseVector = noseDestination;
            orientedPosition.orientation = orientedPosition.orientation.rotate(new Vector3(0, 0, -Math.PI/2));
        }
        else if(animationPlayer.getCurrentAnimationFrame() == FRAME_FOR_ENDING_OF_REORIENTATION) {
            Vector3 vectorFromBossToPlayer = inputHolder.value.humanCar.position.minus(animationPlayer.getCenterOfMass());
            dashDirection = vectorFromBossToPlayer.plus(inputHolder.value.humanCar.velocity.scaled(
                                    ((AMOUNT_OF_FRAMES_TO_PREPARE_BEFORE_THE_DASH - FRAME_FOR_ENDING_OF_REORIENTATION)/RlConstants.BOT_REFRESH_RATE)
                                            + vectorFromBossToPlayer.magnitude()/(findDashSpeed()*RlConstants.BOT_REFRESH_RATE))
                            .scaled(1.4))
                    .scaled(1, 1, 0).normalized();
            final Vector3 noseDestination = dashDirection.scaled(1, 1, 0).normalized().scaled(-1);
            orientedPosition.orientation.noseVector = noseDestination;
            orientedPosition.orientation = orientedPosition.orientation.rotate(new Vector3(0, 0, -Math.PI/2));
        }
        return orientedPosition;
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(animationPlayer.isFinished()) {
            return new BossIdle2Phase1(orientedPosition);
        }
        return this;
    }

    private double findDashSpeed() {
        switch (GameOptions.gameDifficulty) {
            case ROCKET_SLEDGE: return 4;
            case TRIVIAL: return 3000 / RlConstants.BOT_REFRESH_RATE;
            case EASY: return 5000 / RlConstants.BOT_REFRESH_RATE;
            case MEDIUM: return 9000 / RlConstants.BOT_REFRESH_RATE;
            case HARD: return 13000 / RlConstants.BOT_REFRESH_RATE;
            case EXPERT: return 17000 / RlConstants.BOT_REFRESH_RATE;
            case IMPOSSIBLE: return 22000 / RlConstants.BOT_REFRESH_RATE;
            case WTF: return 25000 / RlConstants.BOT_REFRESH_RATE;
        }
        throw new RuntimeException("No game difficulty selected!");
    }

    private boolean isBossDashOutOfBound(Vector3 centerOfMass) {
        final double offset = 0;

        if(centerOfMass.x > RlConstants.WALL_DISTANCE_X - offset) {
            return true;
        }
        else if(centerOfMass.x < -RlConstants.WALL_DISTANCE_X + offset) {
            return true;
        }

        if(centerOfMass.y > RlConstants.WALL_DISTANCE_Y - offset) {
            return true;
        }
        else if(centerOfMass.y < -RlConstants.WALL_DISTANCE_Y + offset) {
            return true;
        }

        return false;
    }

    @Override
    public void debug(DataPacket input) {}
}
