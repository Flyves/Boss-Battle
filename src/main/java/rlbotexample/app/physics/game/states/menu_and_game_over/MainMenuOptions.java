package rlbotexample.app.physics.game.states.menu_and_game_over;

import rlbotexample.app.physics.game.game_option.DifficultyType;
import rlbotexample.app.physics.game.game_option.GameOptions;
import rlbotexample.app.physics.game.states.menu_and_game_over.ui_components.SphericalButton;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.math.vector.Vector3;
import util.state_machine.State;

import java.awt.*;

public class MainMenuOptions implements State {

    private static final SphericalButton ROCKET_SLEDGE_DIFFICULTY_BUTTON_XD = new SphericalButton(new Vector3(-2662, -3681, 100), 300, "Sledge xD", Color.GRAY);
    private static final SphericalButton TRIVIAL_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(-2734, -989, 150), 300, "Trivial", Color.GRAY);
    private static final SphericalButton EASY_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(-1953, 663, 400), 300, "Easy", Color.GRAY);
    private static final SphericalButton MEDIUM_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(-642, 2178, 700),300,  "Medium", Color.green);
    private static final SphericalButton HARD_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(1944, 2028, 1500),300,  "Hard", Color.GRAY);
    private static final SphericalButton EXPERT_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(3495, 551, 1600),300,  "Expert", Color.GRAY);
    private static final SphericalButton IMPOSSIBLE_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(3244, -1255, 1400),300,  "Impossible", Color.GRAY);
    private static final SphericalButton NOT_SURE_IF_IT_S_BEATABLE_DIFFICULTY_BUTTON = new SphericalButton(new Vector3(1733, -2105, 1700),300,  "Is this even beatable???", Color.GRAY);
    private static final SphericalButton BACK_BUTTON = new SphericalButton(new Vector3(0, 0, 0), 300, "Back", Color.cyan);

    private static SphericalButton previouslySelectedButton = MEDIUM_DIFFICULTY_BUTTON;

    @Override
    public void start(DataPacket input) {
        BallStateSetter.setTarget(BallStateSetter.DEFAULT_TARGET);
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(ROCKET_SLEDGE_DIFFICULTY_BUTTON_XD.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            ROCKET_SLEDGE_DIFFICULTY_BUTTON_XD.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.ROCKET_SLEDGE;
            previouslySelectedButton = ROCKET_SLEDGE_DIFFICULTY_BUTTON_XD;
        }
        // makes no sense
        /*else if(TRIVIAL_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            TRIVIAL_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.TRIVIAL;
            previouslySelectedButton = TRIVIAL_DIFFICULTY_BUTTON;
        }*/
        else if(EASY_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            EASY_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.EASY;
            previouslySelectedButton = EASY_DIFFICULTY_BUTTON;
        }
        else if(MEDIUM_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            MEDIUM_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.MEDIUM;
            previouslySelectedButton = MEDIUM_DIFFICULTY_BUTTON;
        }
        else if(HARD_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            HARD_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.HARD;
            previouslySelectedButton = HARD_DIFFICULTY_BUTTON;
        }
        else if(EXPERT_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            EXPERT_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.EXPERT;
            previouslySelectedButton = EXPERT_DIFFICULTY_BUTTON;
        }
        else if(IMPOSSIBLE_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            IMPOSSIBLE_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.IMPOSSIBLE;
            previouslySelectedButton = IMPOSSIBLE_DIFFICULTY_BUTTON;
        }
        else if(NOT_SURE_IF_IT_S_BEATABLE_DIFFICULTY_BUTTON.isPressed(input)) {
            previouslySelectedButton.setColor(Color.GRAY);
            NOT_SURE_IF_IT_S_BEATABLE_DIFFICULTY_BUTTON.setColor(Color.green);
            GameOptions.gameDifficulty = DifficultyType.WTF;
            previouslySelectedButton = NOT_SURE_IF_IT_S_BEATABLE_DIFFICULTY_BUTTON;
        }
        else if(BACK_BUTTON.isPressed(input)) {
            return new MainMenu();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {
        ROCKET_SLEDGE_DIFFICULTY_BUTTON_XD.render();
        //TRIVIAL_DIFFICULTY_BUTTON.render();
        EASY_DIFFICULTY_BUTTON.render();
        MEDIUM_DIFFICULTY_BUTTON.render();
        HARD_DIFFICULTY_BUTTON.render();
        EXPERT_DIFFICULTY_BUTTON.render();
        IMPOSSIBLE_DIFFICULTY_BUTTON.render();
        NOT_SURE_IF_IT_S_BEATABLE_DIFFICULTY_BUTTON.render();
        BACK_BUTTON.render();
    }
}
