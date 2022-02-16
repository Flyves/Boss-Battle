package rlbotexample.app.physics.game.states.boss_moves.phase1;

import rlbot.render.Renderer;
import rlbotexample.animations.CarGroupAnimator;
import rlbotexample.animations.GameAnimations;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

public class BossRunPhase1 implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.bossAi.animator = new CarGroupAnimator(GameAnimations.quadrupedal_boss_rigged_walk);
    }

    @Override
    public void exec(DataPacket input) {
        Vector3 vectorFromBossToPlayer = input.humanCar.position.minus(CurrentGame.bossAi.centerOfMass);
        CurrentGame.bossAi.orientedPosition.orientation.noseVector = vectorFromBossToPlayer.scaled(1, 1, 0).normalized().scaled(-1);
        CurrentGame.bossAi.orientedPosition.position = CurrentGame.bossAi.orientedPosition.position
                .plus(vectorFromBossToPlayer.scaled(1, 1, 0).scaledToMagnitude(CurrentGame.BOSS_MAX_SPEED));
        CurrentGame.bossAi.step(input);
    }

    @Override
    public void stop(DataPacket input) {
        CurrentGame.bossAi.close();
    }

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.centerOfMass.minus(input.humanCar.position).magnitudeSquared() < 3000*3000 && input.humanCar.position.z > 500) {
            return new BossElectricBallShootingAttackPhase1();
        }
        if(CurrentGame.bossAi.centerOfMass.minus(input.humanCar.position).magnitudeSquared() < 2000*2000) {
            return new BossDashAttackPhase1();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
