package util.timer;

public class Timer
{
    private double duration;
    private long timeOfStartMillis;

    public Timer(double duration) {
        this.duration = duration;
    }

    public Timer start()
    {
        timeOfStartMillis = System.currentTimeMillis();
        return this;
    }

    public void end() {
        timeOfStartMillis = System.currentTimeMillis() - ((int)(duration*1000));
    }

    public boolean isTimeElapsed()
    {
        return (System.currentTimeMillis() - timeOfStartMillis)/(double)1000 >= duration;
    }

    public float remainingTime()
    {
        return (float)(duration - (System.currentTimeMillis() - timeOfStartMillis)/1000.0);
    }

    public boolean timeElapsed()
    {
        return  remainingTime() >= duration;
    }

    public double duration()
    {
        return duration;
    }
}
