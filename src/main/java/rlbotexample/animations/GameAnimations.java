package rlbotexample.animations;

import util.parameter_configuration.ObjectSerializer;

public class GameAnimations {

    private static final String BASE_FILE_PATH = "src\\main\\resources\\car animations\\";

    public static boolean areReady = false;
    public static boolean areLoading = false;
    public static int loadingProgress = 0;
    public static int maxAnimationLoadingProgress= 8;

    public static CarGroupAnimation quadrupedal_boss_rigged_walk;
    public static CarGroupAnimation boss_transformation_0_To_1;
    public static CarGroupAnimation boss_dash_attack;
    public static CarGroupAnimation boss_electric_ball_firing;
    public static CarGroupAnimation boss_jump;
    public static CarGroupAnimation quadrupedal_idle;
    public static CarGroupAnimation quadrupedal_idle_2;
    public static CarGroupAnimation quadrupedal_beyblade;
    public static CarGroupAnimation boss_idk;

    public static void loadAnimations() {
        quadrupedal_boss_rigged_walk = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_boss_rigged_walk.sob");
        loadingProgress++;
        boss_transformation_0_To_1 = ObjectSerializer.load(BASE_FILE_PATH + "boss_transformation_0_To_1.sob");
        loadingProgress++;
        boss_dash_attack = ObjectSerializer.load(BASE_FILE_PATH + "boss_dash_attack.sob");
        loadingProgress++;
        boss_electric_ball_firing = ObjectSerializer.load(BASE_FILE_PATH + "boss_electric_ball_firing.sob");
        loadingProgress++;
        boss_jump = ObjectSerializer.load(BASE_FILE_PATH + "boss_jump.sob");
        loadingProgress++;
        quadrupedal_idle = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_idle.sob");
        loadingProgress++;
        quadrupedal_idle_2 = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_idle_2.sob");
        loadingProgress++;
        quadrupedal_beyblade = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_beyblade.sob");
        loadingProgress++;
        boss_idk = ObjectSerializer.load(BASE_FILE_PATH + "boss_idk.sob");
        areReady = true;
        areLoading = false;
    }
}
