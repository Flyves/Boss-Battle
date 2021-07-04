package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.resource_handling.electric_balls.ElectricBallsResourceHandler;
import util.state_machine.State;

public class BossElectricBallShootingAttackPhase1 implements State {

    private static final Integer[] FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT = new Integer[]{140, 160, 180, 200, 220, 240, 260, 280,
                                                                                         135, 155, 175, 195, 215, 235, 255, 275,
                                                                                         145, 165, 185, 205, 225, 245, 265, 285,
                                                                                         150, 170, 190, 210, 230, 250, 270, 290};

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_electric_ball_firing);
        CurrentGame.bossAi.animator.isLooping = false;
    }

    @Override
    public void exec(DataPacket input) {
        for(Integer frameIndex: FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT) {
            if(frameIndex == CurrentGame.bossAi.animator.currentFrameIndex()) {
                ElectricBallsResourceHandler.allocAt(CurrentGame.bossAi.centerOfMass, input.humanCar);
            }
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
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
