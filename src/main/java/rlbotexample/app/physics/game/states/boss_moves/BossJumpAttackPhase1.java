package rlbotexample.app.physics.game.states.boss_moves;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.boss_moves.boss_jump_attack_phase_1_states.InitialJumpPreparationForJumpOfBossAttackPhase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;
import util.state_machine.StateMachine;

public class BossJumpAttackPhase1 implements State {

    public int amountOfJumpsDone = 0;

    private StateMachine bossJumpStateMachine = new StateMachine(new InitialJumpPreparationForJumpOfBossAttackPhase1(this));

    @Override
    public void start(DataPacket input) {}

    @Override
    public void exec(DataPacket input) {
        bossJumpStateMachine.exec(input);
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
