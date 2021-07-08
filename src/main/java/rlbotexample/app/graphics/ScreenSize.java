package rlbotexample.app.graphics;

import java.awt.*;

// The following code was found on this website:
// https://www.geeksforgeeks.org/java-program-to-print-screen-resolution/

// I tweaked it just a little bit so it's embedded directly in a static class instead

public class ScreenSize {

    // getScreenSize() returns the size
    // of the screen in pixels
    private static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

    // width will store the width of the screen
    public static final int WIDTH = (int)size.getWidth();

    // height will store the height of the screen
    public static final int HEIGHT = (int)size.getHeight();
}
