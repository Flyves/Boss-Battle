package util.machine_learning_models.evaluators;

import rlbotexample.input.dynamic_data.DataPacket;

public abstract class BotEvaluator {

    private double bestEvaluation;

    // called every frame.
    // it adds evaluations frame after frame.
    // At the end, the highest evaluation, the best
    // (hopefully, I can find a good function that can
    // evaluate well the behaviours I'm trying to implement...)
    public abstract void updateEvaluation(DataPacket input);

    public double getEvaluation() {
        return bestEvaluation;
    }

    void setEvaluation(double newEvaluation) {
        this.bestEvaluation = newEvaluation;
    }

    public void resetEvaluation() {
        bestEvaluation = 0;
    }
}
