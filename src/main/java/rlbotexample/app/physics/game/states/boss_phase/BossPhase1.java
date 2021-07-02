package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

public class BossPhase1 implements State {

    private static final double BOSS_HEALTH_THRESHOLD_FACTOR = 0.666666666;

    public BossPhase1() {
        try {
            CurrentGame.bossAi.animator.close();
        }
        catch(RuntimeException ignored) {}
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.quadrupedal_boss_rigged_walk);
    }

    @Override
    public void exec(DataPacket input) {
        Vector3 vectorFromBossToPlayer = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass);
        CurrentGame.bossAi.orientedPosition.orientation.noseVector = vectorFromBossToPlayer.scaled(1, 1, 0).normalized().scaled(-1);
        CurrentGame.bossAi.step(input);
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.health < CurrentGame.BOSS_INITIAL_HP * BOSS_HEALTH_THRESHOLD_FACTOR) {
            return new TransitionFromPhase2ToPhase3();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {

    }
}
