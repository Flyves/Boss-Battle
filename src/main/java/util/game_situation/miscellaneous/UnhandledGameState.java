package util.game_situation.miscellaneous;

import util.game_situation.GameSituation;
import util.timer.FrameTimer;
import util.timer.Timer;

public class UnhandledGameState extends GameSituation {

    public UnhandledGameState() {
        super(new FrameTimer(0));
    }

    @Override
    public void loadGameState() {}
}
