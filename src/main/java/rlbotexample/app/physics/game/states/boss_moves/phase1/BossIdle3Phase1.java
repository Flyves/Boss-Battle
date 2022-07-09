package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.asset.animation.rigidity.BasicRigidityTransitionHandler;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.OrientedPosition;
import util.state_machine.State;

public class BossIdle3Phase1 implements State {

    private BossIdleTemplatePhase1 idle2Phase1;
    private int amountOfTimesBossJumped;

    public BossIdle3Phase1(final OrientedPosition initialOrientedPosition) {
        this.idle2Phase1 = new BossIdleTemplatePhase1(initialOrientedPosition);
        this.amountOfTimesBossJumped = 0;
    }

    @Override
    public void start(DataPacket input) {
        if(amountOfTimesBossJumped < findHowManyTimesToJump()) {
            idle2Phase1.start(input);
        }
    }

    @Override
    public void exec(DataPacket input) {
        if(findHowManyTimesToJump() == 0) {
            return;
        }
        idle2Phase1.exec(input);
        if(idle2Phase1.animationPlayer.isFinished()) {
            amountOfTimesBossJumped++;
            if(amountOfTimesBossJumped < findHowManyTimesToJump()) {
                idle2Phase1 = new BossIdleTemplatePhase1(idle2Phase1.orientedPosition);
                idle2Phase1.start(input);
            }
        }
    }

    @Override
    public void stop(DataPacket input) {
        idle2Phase1.stop(input);
    }

    @Override
    public State next(DataPacket input) {
        if(amountOfTimesBossJumped >= findHowManyTimesToJump()) {
            return new BossElectricBallShootingAttackPhase1(idle2Phase1.orientedPosition);
        }
        return this;
    }

    private int findHowManyTimesToJump() {
        switch (GameOptions.gameDifficulty) {
            case ROCKET_SLEDGE: return 4;
            case TRIVIAL: return 4;
            case EASY: return 3;
            case MEDIUM: return 2;
            case HARD: return 1;
            case EXPERT: return 1;
            case IMPOSSIBLE: return 1;
            case WTF: return 1;
        }
        throw new RuntimeException("No game difficulty selected!");
    }

    @Override
    public void debug(DataPacket input) {}
}
