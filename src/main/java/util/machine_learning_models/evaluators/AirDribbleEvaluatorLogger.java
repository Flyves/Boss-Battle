package util.machine_learning_models.evaluators;

import rlbotexample.input.dynamic_data.DataPacket;
import util.machine_learning_models.generic_data_structure.generic_data.FileParameter;

public class AirDribbleEvaluatorLogger extends BotEvaluator {

    private FileParameter fileParameter;

    public AirDribbleEvaluatorLogger(FileParameter fileParameter) {
        this.fileParameter = fileParameter;
    }

    @Override
    public void updateEvaluation(DataPacket input) {
        // gotta find a way to evaluate air dribbles
        double currentEvaluation = 0;

        // this is where we judge intensely the bot
        currentEvaluation -= input.ball.position.minus(input.car.position).magnitude();

        // apply the new evaluation
        setEvaluation(currentEvaluation/1000 + getEvaluation());
    }

    @Override
    public void resetEvaluation() {
        // serialize data so we can visualize it later
        fileParameter.set(getEvaluation());

        // do the things we normally do with superclass implementation
        super.resetEvaluation();
    }
}
