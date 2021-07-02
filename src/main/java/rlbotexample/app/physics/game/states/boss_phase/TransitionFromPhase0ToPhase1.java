package rlbotexample.app.physics.game.states.boss_phase;

import jdk.internal.org.jline.utils.ClosedException;
import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

public class TransitionFromPhase0ToPhase1 implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.boss_transformation_0_To_1);
        CurrentGame.bossAi.animator.isLooping = false;
        CurrentGame.bossAi.health++;
    }

    @Override
    public void exec(DataPacket input) {
        CurrentGame.bossAi.orientedPosition.position = new Vector3();
        if(CurrentGame.bossAi.animator.currentFrameIndex() < 100) {
            CurrentGame.bossAi.orientedPosition.position = new Vector3(0, 0, -30 * CurrentGame.bossAi.animator.currentFrameIndex() -1000);
        }
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.animator.isFinished()) {
            return new BossPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}

}
