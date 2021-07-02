package rlbotexample.app.physics.game.states.boss_phase;

import rlbot.render.Renderer;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.app.physics.game.states.menu_and_game_over.GameOver;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class BossPhase3 implements State {

    private static final double BOSS_HEALTH_THRESHOLD = 0;

    @Override
    public void start(DataPacket input) {}

    @Override
    public void exec(DataPacket input) {}

    @Override
    public void stop(DataPacket input) {}

    @Override
    public State next(DataPacket input) {
        if(CurrentGame.bossAi.health <= BOSS_HEALTH_THRESHOLD) {
            return new GameOver();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input, Renderer renderer) {}
}
