package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class BossIdle2Phase1 implements State {

    private BossIdleTemplatePhase1 idle2Phase1;
    private int amountOfTimesBossJumped;

    public BossIdle2Phase1() {
        this.idle2Phase1 = new BossIdleTemplatePhase1();
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
            idle2Phase1 = new BossIdleTemplatePhase1();
            idle2Phase1.start(input);
        }
    }

    @Override
    public void stop(DataPacket input) {
        idle2Phase1.stop(input);
    }

    @Override
    public State next(DataPacket input) {
        if(amountOfTimesBossJumped == findHowManyTimesToJump()) {
            return new BossSpinAttackPhase1();
        }
        return this;
    }

    private int findHowManyTimesToJump() {
        switch (GameOptions.gameDifficulty) {
            case ROCKET_SLEDGE: return 4;
            case TRIVIAL: return 4;
            case EASY: return 3;
            case MEDIUM: return 2;
            case HARD: return 2;
            case EXPERT: return 2;
            case IMPOSSIBLE: return 1;
            case WTF: return 1;
        }
        throw new RuntimeException("No game difficulty selected!");
    }

    @Override
    public void debug(DataPacket input) {}
}
