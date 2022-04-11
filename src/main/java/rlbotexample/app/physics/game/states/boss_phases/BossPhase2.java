package rlbotexample.app.physics.game.states.boss_phases;

import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossIdle3Phase1;
import rlbotexample.app.physics.game.states.boss_moves.phase2.InitPhase2;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;
import util.state_machine.StateMachine;

public class BossPhase2 implements State {

    private static final double BOSS_HEALTH_THRESHOLD_FACTOR = 0.3333333333;
    private static StateMachine bossAttackPattern;

    @Override
    public void start(DataPacket input) {
        bossAttackPattern = new StateMachine(new InitPhase2());
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
    public void debug(DataPacket input) {}
}
