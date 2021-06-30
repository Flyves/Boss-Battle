package util.game_situation.trainning_pack;

import rlbotexample.input.dynamic_data.DataPacket;
import util.game_situation.GameSituation;
import util.game_situation.miscellaneous.UnhandledGameState;

import java.util.ArrayList;
import java.util.List;

public abstract class TrainingPack {

    private List<GameSituation> gameSituationList;
    private GameSituation currentGameSituation;
    private int currentGameSituationIndex;

    public TrainingPack() {
        gameSituationList = new ArrayList<>();
        currentGameSituation = new UnhandledGameState();
        currentGameSituationIndex = 0;
    }

    public void add(GameSituation gameSituation) {
        gameSituationList.add(gameSituation);
    }

    public void update() {
        currentGameSituation.frameHappened();
        if(currentGameSituation.isGameStateElapsed()) {
            if(hasNext()) {
                next();
                currentGameSituation.reloadGameState();
            }
        }
    }

    public boolean updatingWontBreakBot(DataPacket input) {
        return currentGameSituation.updatingWontBreakBot(input);
    }

    public void reset() {
        currentGameSituationIndex = 0;
    }

    public GameSituation next() {
        int index = currentGameSituationIndex;
        currentGameSituation = gameSituationList.get(index);
        currentGameSituationIndex++;
        return currentGameSituation;
    }

    public abstract boolean hasNext();

    public abstract boolean hasBeenCompleted();

    public int getNextGameSituationIndex() {
        return currentGameSituationIndex;
    }

    public List<GameSituation> getGameSituationList() {
        return gameSituationList;
    }
}
