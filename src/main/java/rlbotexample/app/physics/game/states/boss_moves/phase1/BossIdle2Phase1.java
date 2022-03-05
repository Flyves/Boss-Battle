package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.assets.animations.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.assets.sounds.GameSoundFiles;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;
import util.tinysound.TinySound;

public class BossIdle2Phase1 implements State {

    private BossIdle1Phase1 idle2Phase1;
    private int amountOfTimesBossJumped;

    public BossIdle2Phase1() {
        this.idle2Phase1 = new BossIdle1Phase1();
        this.amountOfTimesBossJumped = 0;
    }

    @Override
    public void start(DataPacket input) {
        idle2Phase1.start(input);
    }

    @Override
    public void exec(DataPacket input) {
        BasicRigidityTransitionHandler.handle(CurrentGame.bossAi.animator);
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
        if(amountOfTimesBossJumped == 2) {
            return new BossSpinAttackPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
