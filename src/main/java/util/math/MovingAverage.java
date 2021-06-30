package util.math;

public class MovingAverage {

    public double value;
    private int length;

    public MovingAverage(int averageIterationLength) {
        this.value = 0;
        this.length = averageIterationLength;
    }

    public void update(double value) {
        this.value = (value + (this.value*(this.length-1)))
                / (this.length);
    }
}
