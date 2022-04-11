package rlbotexample.asset.sounds;

import java.io.File;

public class GameSoundFiles {


    private static final String BASE_FILE_PATH = "src\\main\\resources\\sounds\\";

    public static final File game_won = new File(BASE_FILE_PATH + "game_won.wav");
    public static final File game_lost = new File(BASE_FILE_PATH + "game_lost.wav");

    public static final File electric_pewpew_buildup = new File(BASE_FILE_PATH + "electric_pewpew_buildup.wav");
    public static final File electric_pewpew_buildown = new File(BASE_FILE_PATH + "electric_pewpew_buildown.wav");
    public static final File[] pewpew_electric_balls = {
            new File(BASE_FILE_PATH + "pewpew_electric_ball_1.wav"),
            new File(BASE_FILE_PATH + "pewpew_electric_ball_2.wav"),
            new File(BASE_FILE_PATH + "pewpew_electric_ball_3.wav"),
            new File(BASE_FILE_PATH + "pewpew_electric_ball_4.wav")
    };

    public static final File dash_buildup = new File(BASE_FILE_PATH + "dash_buildup.wav");
    public static final File dash_shooting = new File(BASE_FILE_PATH + "dash_shooting.wav");

    public static final File helicopter_buildup = new File(BASE_FILE_PATH + "helicopter_buildup.wav");
    public static final File helicopter_attack = new File(BASE_FILE_PATH + "helicopter_attack.wav");

    public static final File idle2_sweep = new File(BASE_FILE_PATH + "idle2_sweep.wav");
    public static final File leg_step_0 = new File(BASE_FILE_PATH + "leg_step_0.wav");
    public static final File leg_step_1 = new File(BASE_FILE_PATH + "leg_step_1.wav");
    public static final File leg_step_2 = new File(BASE_FILE_PATH + "leg_step_2.wav");
    public static final File leg_step_3 = new File(BASE_FILE_PATH + "leg_step_3.wav");
    public static final File leg_step_4 = new File(BASE_FILE_PATH + "leg_step_4.wav");

    public static final File bootup_head = new File(BASE_FILE_PATH + "bootup_head.wav");
    public static final File bootup_tail = new File(BASE_FILE_PATH + "bootup_tail.wav");

}
