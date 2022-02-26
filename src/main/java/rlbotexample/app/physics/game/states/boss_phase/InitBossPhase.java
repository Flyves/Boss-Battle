package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class InitBossPhase implements State {

    @Override
    public void start(DataPacket input) {}

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        return new TransitionFromPhase0ToPhase1();
    }

    @Override
    public void debug(DataPacket input) {}
}
