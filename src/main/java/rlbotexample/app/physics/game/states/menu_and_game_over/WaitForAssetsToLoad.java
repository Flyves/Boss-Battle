package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbotexample.asset.animation.GameAnimations;
import rlbotexample.app.graphics.ScreenSize;
import rlbotexample.app.physics.game.CurrentGame;
import rlbotexample.dynamic_objects.DataPacket;
import util.renderers.RenderTasks;
import util.state_machine.State;

import java.awt.*;

public class WaitForAssetsToLoad implements State {

    @Override
    public void start(DataPacket input) {
        CurrentGame.isGameOver = false;
    }

    @Override
    public void exec(DataPacket input) {
        String loadingMessage = "Loading assets";
        if(System.currentTimeMillis()%1000 < 250) {
            loadingMessage += "    ";
        }
        else if(System.currentTimeMillis()%1000 < 500) {
            loadingMessage += ".   ";
        }
        else if(System.currentTimeMillis()%1000 < 750) {
            loadingMessage += "..  ";
        }
        else /*(System.currentTimeMillis()%1000 < 1000)*/ {
            loadingMessage += "... ";
        }
        loadingMessage += (int)(GameAnimations.loadingProgress/(double)GameAnimations.maxAnimationLoadingProgress*100);
        loadingMessage += "%";
        final String finalLoadingMessage = loadingMessage;
        RenderTasks.append(renderer -> renderer.drawString2d(finalLoadingMessage,
                Color.WHITE, new Point(20, (int)(ScreenSize.HEIGHT*0.85)),
                2, 2));
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(GameAnimations.areReady) {
            return new MainMenu();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
