package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import rlbotexample.sounds.GameSoundFiles;
import util.math.vector.Vector3;
import util.resource_handling.electric_balls.ElectricBallsResourceHandler;
import util.state_machine.State;
import util.tinysound.Sound;
import util.tinysound.TinySound;

import java.util.Set;

public class BossElectricBallShootingAttackPhase1 implements State {

    private Orientation bossInitialOrientation;

    private static final Set<Integer> FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT = Set.of(
            140, 160, 180, 200, 220, 240, 260, 280,
            135, 155, 175, 195, 215, 235, 255, 275,
            145, 165, 185, 205, 225, 245, 265, 285,
            150, 170, 190, 210, 230, 250, 270, 290
    );

    private final Sound[] pewpewSounds = new Sound[4];

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_electric_ball_firing);
        CurrentGame.bossAi.animator.looping(false);

        Vector3 initialNoseOrientation = CurrentGame.bossAi.orientedPosition.orientation.noseVector;
        bossInitialOrientation = new Orientation(initialNoseOrientation, Vector3.UP_VECTOR);

        TinySound.init();

        for(int i = 0; i < pewpewSounds.length; i++) {
            pewpewSounds[i] = TinySound.loadSound(GameSoundFiles.pewpew_electric_balls[i]);
        }
    }

    @Override
    public void exec(DataPacket input) {
        if(FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT.contains(CurrentGame.bossAi.animator.currentFrameIndex())) {
            ElectricBallsResourceHandler.allocAt(CurrentGame.bossAi.centerOfMass, input.humanCar);

            int randomSoundIndex = (int)(Math.random() * GameSoundFiles.pewpew_electric_balls.length);
            pewpewSounds[randomSoundIndex].play(0.1);
        }

        Vector3 orientationRotator = Vector3.UP_VECTOR.findRotator(input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass));
        Orientation newBossOrientation = bossInitialOrientation.rotate(orientationRotator);


        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();

        TinySound.shutdown();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossIdle1Phase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
