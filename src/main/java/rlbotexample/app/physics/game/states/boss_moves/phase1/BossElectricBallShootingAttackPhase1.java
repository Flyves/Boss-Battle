package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.asset.animation.animation.AnimationPlayer;
import rlbotexample.asset.animation.animation.AnimationProfileBuilder;
import rlbotexample.asset.animation.animation.AnimationTasks;
import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;
import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import rlbotexample.asset.sounds.GameSoundFiles;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.resource_handling.electric_balls.ElectricBallsResourceHandler;
import util.state_machine.State;
import util.tinysound.Sound;
import util.tinysound.TinySound;

import javax.xml.ws.Holder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BossElectricBallShootingAttackPhase1 implements State {
    private Orientation bossInitialOrientation;
    private Vector3 bossInitialPosition;
    private Holder<DataPacket> inputHolder;

    private static final List<Integer> FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT = Arrays.stream(new Integer[] {
            140, 160, 180, 200, 220, 240, 260, 280,
            135, 155, 175, 195, 215, 235, 255, 275,
            145, 165, 185, 205, 225, 245, 265, 285,
            150, 170, 190, 210, 230, 250, 270, 290
    }).collect(Collectors.toList());

    private final Sound[] pewpewSounds = new Sound[4];
    private Sound buildownSound;
    private AnimationPlayer animationPlayer;
    private OrientedPosition orientedPosition;

    public BossElectricBallShootingAttackPhase1(final OrientedPosition initialPosition) {
        this.orientedPosition = initialPosition;
    }

    @Override
    public void start(final DataPacket input) {
        // initial orientation stuff
        inputHolder = new Holder<>(input);
        Vector3 initialNoseOrientation = orientedPosition.orientation.noseVector;
        bossInitialOrientation = new Orientation(initialNoseOrientation, Vector3.Z_VECTOR);
        bossInitialPosition = orientedPosition.position;

        // initializing the sounds
        TinySound.init();

        for(int i = 0; i < pewpewSounds.length; i++) {
            pewpewSounds[i] = TinySound.loadSound(GameSoundFiles.pewpew_electric_balls[i]);
        }
        TinySound.loadSound(GameSoundFiles.electric_pewpew_buildup).play(0.1);
        buildownSound = TinySound.loadSound(GameSoundFiles.electric_pewpew_buildown);

        // building the animation player
        final AnimationProfileBuilder animationProfileBuilder = new AnimationProfileBuilder();

        // sounds and ball shooting stuff
        FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT.forEach(i -> animationProfileBuilder.withFrameEvent(i, () -> {
            ElectricBallsResourceHandler.allocAt(animationPlayer.getCenterOfMass(), inputHolder.value.humanCar);
            int randomSoundIndex = (int)(Math.random() * GameSoundFiles.pewpew_electric_balls.length);
            pewpewSounds[randomSoundIndex].play(0.15);
        }));

        // finally, we build the player
        this.animationPlayer = new AnimationPlayer(animationProfileBuilder
                .withAnimation(GameAnimations.boss_electric_ball_firing)
                .withFrameEvent(305, () -> buildownSound.play(0.14))
                .withAnimationOffset(this::orientedPositionFunction)
                .withRigidity(this::rigidityFunction)
                .withPlaybackSpeed(() -> 50d)
                .build());

        AnimationTasks.append(animationPlayer);
    }

    @Override
    public void exec(DataPacket input) {
        inputHolder.value = input;
        BallStateSetter.setTarget(animationPlayer.getCenterOfMass());
    }

    private OrientedPosition orientedPositionFunction() {
        final Vector3 orientationRotator = Vector3.Z_VECTOR.findRotator(inputHolder.value.humanCar.position.minus(animationPlayer.getCenterOfMass()));
        final Orientation newBossOrientation = bossInitialOrientation.rotate(orientationRotator);
        if(animationPlayer.getCurrentAnimationFrame() < FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT.get(FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT.size()-1) + 30) {
            orientedPosition.orientation = newBossOrientation;
            orientedPosition.position = bossInitialPosition.plus(new Vector3(0, 0, 1000));
        }
        else {
            orientedPosition.orientation = bossInitialOrientation;
            orientedPosition.position = bossInitialPosition;
        }
        return orientedPosition;
    }

    private Double rigidityFunction(final Integer frameIndex) {
        if(frameIndex >= FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT.get(FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT.size()-1) + 30) {
            return  0.2;
        }
        return 0.1 + frameIndex/180.0;
    }

    @Override
    public void stop(DataPacket input) {
        TinySound.shutdown();
    }

    @Override
    public State next(DataPacket input) {
        if(animationPlayer.isFinished()) {
            return new BossIdle1Phase1(orientedPosition);
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
