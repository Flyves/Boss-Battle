package rlbotexample.app.physics.game.states.boss_phase;

import jdk.internal.org.jline.utils.ClosedException;
import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TransitionFromPhase0ToPhase1 implements State {

    private boolean transitionIsDone;

    public TransitionFromPhase0ToPhase1() {
        try {
            CurrentGame.bossAi.animator.close();
        }
        catch(RuntimeException ignored) {}
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_transformation_0_To_1);
        CurrentGame.bossAi.animator.isLooping = false;
        transitionIsDone = false;
    }

    @Override
    public void exec(DataPacket input) {
        CurrentGame.bossAi.step(input);
        transitionIsDone = CurrentGame.bossAi.animator.isFinished();
    }

    @Override
    public State next(DataPacket input) {
        if(transitionIsDone) {
            return new BossPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }

}
