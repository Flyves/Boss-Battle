package rlbotexample.app.graphics.health_bars;

import rlbotexample.app.graphics.ScreenSize;

public class BossHealthBarSegment {

    private static final int HP_BAR_DIMENSION_X = (int)((ScreenSize.WIDTH/1080.0) * 342);
    private static final int HP_BAR_DIMENSION_Y = (int)((ScreenSize.HEIGHT/1920.0) * 16);

    private static final int HP_BAR_BACKGROUND_DIMENSION_X = (int)((ScreenSize.WIDTH/1080.0) * 358);
    private static final int HP_BAR_BACKGROUND_DIMENSION_Y = (int)((ScreenSize.HEIGHT/1920.0) * 32);

    private static final int HP_BAR_OVERLAPPING_OFFSET_X = (int)((ScreenSize.WIDTH/1080.0) * 8);
    private static final int HP_BAR_OVERLAPPING_OFFSET_Y = (int)((ScreenSize.HEIGHT/1920.0) * 16);

    private static final int HP_BAR_OFFSET_WITH_BACKGROUND_X = (int)((ScreenSize.WIDTH/1080.0) * 8);
    private static final int HP_BAR_OFFSET_WITH_BACKGROUND_Y = (int)((ScreenSize.HEIGHT/1920.0) * 8);
}
