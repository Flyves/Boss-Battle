package rlbotexample.app.physics.game.states.boss_phases;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TransitionFromPhase2ToPhase3 implements State {

    @Override
    public void start(DataPacket input) {}

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossPhase3();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
