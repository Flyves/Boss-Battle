package rlbotexample.app.physics.game.states.boss_phase;

import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.graphics.health_bars.BossHealthBar;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.game_option.DifficultyType;
import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.app.physics.game.states.boss_moves.phase1.BossIdle3Phase1;
import rlbotexample.dynamic_objects.DataPacket;
import util.game_constants.RlConstants;
import util.renderers.RenderTasks;
import util.state_machine.State;
import util.state_machine.StateMachine;

import java.awt.*;

public class BossPhase1 implements State {

    private static final double BOSS_HEALTH_THRESHOLD_FACTOR = 0.666666666;

    private static StateMachine bossAttackPattern;
    private int amountOfFramesWithoutDealingHealth;

    @Override
    public void start(DataPacket input) {
        bossAttackPattern = new StateMachine(new BossIdle3Phase1());
        amountOfFramesWithoutDealingHealth = 0;
    }

    @Override
    public void exec(DataPacket input) {
        bossAttackPattern.exec(input);
        if(GameOptions.gameDifficulty == DifficultyType.WTF
                || GameOptions.gameDifficulty == DifficultyType.IMPOSSIBLE
                || GameOptions.gameDifficulty == DifficultyType.EXPERT
                || GameOptions.gameDifficulty == DifficultyType.HARD) {
            amountOfFramesWithoutDealingHealth = 0;
        }
        if(CurrentGame.bossAi.health == CurrentGame.BOSS_INITIAL_HP) {
            amountOfFramesWithoutDealingHealth++;
            if(amountOfFramesWithoutDealingHealth > 60 * RlConstants.BOT_REFRESH_RATE) {
                final String hintMessage = "Hint : You are on the blue team, its legs are on the orange team...";
                final String hintMessage2 = "What can you do to deal damage?";
                RenderTasks.append(r -> r.drawString2d(hintMessage, Color.white, new Point((int)(ScreenSize.WIDTH*0.5)-(int)(500*ScreenSize.FULL_HD_RATIO.x), (int)(ScreenSize.HEIGHT*0.9)), 1, 1));
                RenderTasks.append(r -> r.drawString2d(hintMessage2, Color.RED, new Point((int)(ScreenSize.WIDTH*0.5)-(int)(422*ScreenSize.FULL_HD_RATIO.x), (int)(ScreenSize.HEIGHT*0.9) + (int)(30*ScreenSize.FULL_HD_RATIO.y)), 1, 1));
            }
        }
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.health < CurrentGame.BOSS_INITIAL_HP * BOSS_HEALTH_THRESHOLD_FACTOR) {
            return new TransitionFromPhase1ToPhase2();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {
        BossHealthBar.renderOnScreen(CurrentGame.bossAi.health/(double)CurrentGame.BOSS_INITIAL_HP, input);
    }
}
