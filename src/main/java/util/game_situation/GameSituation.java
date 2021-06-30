package util.game_situation;

import rlbot.cppinterop.RLBotDll;
import rlbot.gamestate.GameState;
import rlbotexample.dynamic_objects.DataPacket;
import util.timer.FrameTimer;

public abstract class GameSituation {

    private FrameTimer gameStateDuration;
    private int numberOfFramesToCount;

    public GameSituation(FrameTimer gameStateDuration) {
        this.gameStateDuration = gameStateDuration;
        this.numberOfFramesToCount = gameStateDuration.numberOfFramesToCount;
        this.gameStateDuration.numberOfFramesToCount = 2;
        this.gameStateDuration.start();
    }

    public void reloadGameState() {
        this.gameStateDuration.numberOfFramesToCount = this.numberOfFramesToCount;
        this.gameStateDuration.start();
        this.loadGameState();
    }

    public boolean updatingWontBreakBot(DataPacket input) {
        return input.car.elapsedSeconds > 1;
    }

    public abstract void loadGameState();

    public boolean isGameStateElapsed() {
        return gameStateDuration.isTimeElapsed();
    }

    public void frameHappened() {
        gameStateDuration.countFrame();
    }

    public static GameState getCurrentGameState() {
        return new rlbot.gamestate.GameState();
    }

    public static void applyGameState(final GameState gameState) {
        RLBotDll.setGameState(gameState.buildPacket());
    }
}
