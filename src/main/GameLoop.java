package main;

import renderEngine.display.Display;

/**
 * The main game loop.  Handles all the main game logic.
 *
 * @author  Seph Pace
 * @version 1.0
 * @since   2019-04-18
 */
public class GameLoop {

    // The width of the display
    private static final int DISPLAY_WIDTH = 700;

    // The height of the display
    private static final int DISPLAY_HEIGHT = 500;

    // The main game window
    private Display display;

    /**
     * Constructor.
     */
    protected GameLoop() {
        this.display = new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT);
    }

    /**
     * Runs the game loop.
     */
    protected void run() {
        // The main loop
        while(!display.shouldClose()) {
            display.update();
        }

        // Clean everything up
        display.cleanup();
    }
}
