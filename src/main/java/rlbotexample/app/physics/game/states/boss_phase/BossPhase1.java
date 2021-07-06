package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.*;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;
import util.state_machine.StateMachine;

public class BossPhase1 implements State {

    private static final double BOSS_HEALTH_THRESHOLD_FACTOR = 0.666666666;

    private static final StateMachine bossAttackPattern = new StateMachine(new BossIdle3Phase1());

    @Override
    public void start(DataPacket input) {

    }

    @Override
    public void exec(DataPacket input) {
        bossAttackPattern.exec(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.health < CurrentGame.BOSS_INITIAL_HP * BOSS_HEALTH_THRESHOLD_FACTOR) {
            return new TransitionFromPhase2ToPhase3();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
