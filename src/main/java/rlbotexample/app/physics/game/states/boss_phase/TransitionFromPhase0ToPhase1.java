package rlbotexample.app.physics.game.states.boss_phase;

import rlbotexample.assets.animations.CarGroupAnimator;
import rlbotexample.assets.animations.GameAnimations;
import rlbotexample.app.graphics.health_bars.BossHealthBar;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

public class TransitionFromPhase0ToPhase1 implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_transformation_0_To_1);
        CurrentGame.bossAi.animator.looping(false);
    }

    @Override
    public void exec(DataPacket input) {
        CurrentGame.bossAi.orientedPosition.position = new Vector3();
        /*if(CurrentGame.bossAi.animator.currentFrameIndex() < CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation.size()) {
            CurrentGame.bossAi.orientedPosition.position = new Vector3(0, 0, -1000);
            final int carIndexToKill = CurrentGame.bossAi.animator.carIndexesUsedForTheAnimation.get(CurrentGame.bossAi.animator.currentFrameIndex());
            PhysicsOfBossBattle.setOrientedPosition(new ZyxOrientedPosition(new Vector3((Math.random()-0.5)*1000, (Math.random()-0.5)*1000, -10000), new Vector3()), input.allCars.get(carIndexToKill));
        }*/
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {
        BossHealthBar.renderOnScreen(CurrentGame.bossAi.health/(double)CurrentGame.BOSS_INITIAL_HP, input);
    }

}
