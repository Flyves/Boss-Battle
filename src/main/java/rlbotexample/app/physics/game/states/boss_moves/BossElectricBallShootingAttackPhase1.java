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

    private static final Integer[] FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT = new Integer[]{40, 60, 80, 100, 120,
                                                                                         35, 55, 75, 95, 115,
                                                                                         45, 65, 85, 105, 125};

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_electric_ball_firing);
        CurrentGame.bossAi.animator.isLooping = false;
    }

    @Override
    public void exec(DataPacket input) {
        for(Integer frameIndex: FRAME_INDEXES_AT_WHICH_BALLS_ARE_SHOT) {
            if(frameIndex == CurrentGame.bossAi.animator.currentFrameIndex()) {
                ElectricBallsResourceHandler.allocAt(CurrentGame.bossAi.centerOfMass.plus(new Vector3(0, 0, 200)), input.humanCar);
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
