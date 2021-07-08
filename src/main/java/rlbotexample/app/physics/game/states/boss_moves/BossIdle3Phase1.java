package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

import static rlbotexample.app.physics.game.states.boss_moves.BossIdle1Phase1.FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE;

public class BossIdle3Phase1 implements State {

    private State idle2Phase1;
    private int amountOfTimesBossJumped;

    public BossIdle3Phase1() {
        this.idle2Phase1 = new BossIdle1Phase1();
        this.amountOfTimesBossJumped = 0;
    }

    @Override
    public void start(DataPacket input) {
        idle2Phase1.start(input);
    }

    @Override
    public void exec(DataPacket input) {
        idle2Phase1.exec(input);
        if(CurrentGame.bossAi.animator.isFinished()) {
            amountOfTimesBossJumped++;
            idle2Phase1 = new BossIdle1Phase1();
            idle2Phase1.start(input);
        }
    }

    @Override
    public void stop(DataPacket input) {
        idle2Phase1.stop(input);
    }

    @Override
    public State next(DataPacket input) {
        if(amountOfTimesBossJumped == 1) {
            return new BossElectricBallShootingAttackPhase1();
        }
        if (CurrentGame.bossAi.animator.currentFrameIndex() > FRAME_AT_WHICH_BOSS_ENDS_TO_MOVE_TO_THE_SIDE + 10
                && input.humanCar.position.z > 500) {
            return new BossElectricBallShootingAttackPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
