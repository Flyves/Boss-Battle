package util.timer;

import util.game_constants.RlConstants;

public class FrameTimer {

    public int numberOfFramesToCount;
    private int frameCount;

    public FrameTimer(int numberOfFramesToCount) {
        this.numberOfFramesToCount = numberOfFramesToCount;
        this.frameCount = 0;
    }

    public void start() {
        frameCount = 0;
    }

    public boolean isTimeElapsed() {
        return frameCount >= numberOfFramesToCount;
    }

    public void end() {
        frameCount = numberOfFramesToCount;
    }

    public void countFrame() {
        if(frameCount < numberOfFramesToCount) {
            frameCount++;
        }
    }

    public double remainingTime() {
        return (numberOfFramesToCount - frameCount)
                * RlConstants.BOT_REFRESH_TIME_PERIOD;
    }
}
