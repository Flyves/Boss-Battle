package rlbotexample.animations;

import util.parameter_configuration.ObjectSerializer;

public class GameAnimations {

    private static final String BASE_FILE_PATH = "src\\main\\resources\\car animations\\";

    public static final CarGroupAnimation quadrupedal_boss_rigged_walk = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_boss_rigged_walk.sob");
    public static final CarGroupAnimation boss_transformation_0_To_1 = ObjectSerializer.load(BASE_FILE_PATH + "boss_transformation_0_To_1.sob");
    public static final CarGroupAnimation boss_dash_attack = ObjectSerializer.load(BASE_FILE_PATH + "boss_dash_attack.sob");
}
