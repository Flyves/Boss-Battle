package rlbotexample.animations;

import util.parameter_configuration.ObjectSerializer;

public class GameAnimations {

    private static final String BASE_FILE_PATH = "src\\main\\resources\\car animations\\";

    public static final CarGroupAnimation boss_idle = ObjectSerializer.load(BASE_FILE_PATH + "boss_idle.sob");
    public static final CarGroupAnimation boss_attack = ObjectSerializer.load(BASE_FILE_PATH + "boss_attack.sob");
    public static final CarGroupAnimation boss_basic_rotation = ObjectSerializer.load(BASE_FILE_PATH + "boss_basic_rotation.sob");
    public static final CarGroupAnimation quadrupedal_boss_walk = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_boss_walk.sob");
    public static final CarGroupAnimation quadrupedal_boss_rigged_walk = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_boss_rigged_walk.sob");
    public static final CarGroupAnimation boss_transformation_0_To_1 = ObjectSerializer.load(BASE_FILE_PATH + "boss_transformation_0_To_1.sob");
}
