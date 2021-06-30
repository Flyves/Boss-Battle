package rlbotexample.bot;

import rlbot.render.Renderer;
import rlbotexample.input.dynamic_data.DataPacket;
import util.game_constants.RlConstants;

import java.awt.*;

// Pan is an abbreviation for PATCHES ARE NEEDED! (Because patches are needed...)
public abstract class FlyveBot extends BotBehaviour {

    @Override
    public void updateGui(Renderer renderer, DataPacket input, double currentFps, double averageFps, long botExecutionTime) {
        int offset = input.botIndex *130;
        displayFpsCounter(renderer, currentFps, offset);
        displayAvgFps(renderer, averageFps, offset);
        displayMsPerFrame(renderer, botExecutionTime, offset);
    }

    private void displayFpsCounter(Renderer renderer, double fps, int offset) {
        if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/10) {
            renderer.drawString2d(String.format("%.4f", fps), Color.green, new Point(10, 10 + offset), 2, 2);
        }
        else if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/6) {
            renderer.drawString2d(String.format("%.4f", fps), Color.yellow, new Point(10, 10 + offset), 2, 2);
        }
        else {
            renderer.drawString2d(String.format("%.4f", fps), Color.red, new Point(10, 10 + offset), 2, 2);
        }
    }

    private void displayAvgFps(Renderer renderer, double fps, int offset) {
        if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/10) {
            renderer.drawString2d(String.format("%.4f", fps) + "", Color.green, new Point(10, 50 + offset), 2, 2);
        }
        else if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/6) {
            renderer.drawString2d(String.format("%.4f", fps) + "", Color.yellow, new Point(10, 50 + offset), 2, 2);
        }
        else {
            renderer.drawString2d(String.format("%.4f", fps) + "", Color.red, new Point(10, 50 + offset), 2, 2);
        }
    }

    private void displayMsPerFrame(Renderer renderer, long msPerFrame, int offset) {
        if(msPerFrame < 500/RlConstants.BOT_REFRESH_RATE) {
            renderer.drawString2d(msPerFrame + "", Color.green, new Point(10, 90 + offset), 2, 2);
        }
        else if(msPerFrame < 1000/RlConstants.BOT_REFRESH_RATE) {
            renderer.drawString2d(msPerFrame + "", Color.yellow, new Point(10, 90 + offset), 2, 2);
        }
        else {
            renderer.drawString2d(msPerFrame + "", Color.red, new Point(10, 90), 2, 2);
        }
    }
}
