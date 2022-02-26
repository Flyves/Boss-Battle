package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class BossPhase2 implements State {

    private static final double BOSS_HEALTH_THRESHOLD_FACTOR = 0.3333333333;

    @Override
    public void start(DataPacket input) {}

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.health < CurrentGame.BOSS_INITIAL_HP * BOSS_HEALTH_THRESHOLD_FACTOR) {
            return new TransitionFromPhase1ToPhase2();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {}
}
