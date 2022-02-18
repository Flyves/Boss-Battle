package rlbotexample;

import rlbot.Bot;
import rlbot.manager.BotManager;

import java.util.function.Supplier;

/**
 * This class uses almost the same implementation as the default BotManager for bots.
 * It runs the main logic loop, but only keeps 1 bot active at any given time.
 */
public class SingleBotManager extends BotManager {

    public void ensureBotRegistered(final int index, final int team, final Supplier<Bot> botSupplier) {
        super.ensureBotRegistered(index, team, botSupplier);
        if(getRunningBotIndices().size() > 1) {
            retireBot(index);
        }
    }
}
