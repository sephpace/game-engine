package main;

/**
 * Main class.  Starts all other parts of the program.
 *
 * @author  Seph Pace
 * @version 1.0
 * @since  2019-04-18
 */
public class Main {

    /**
     * Main method.
     *
     * @param args  Arguments from the command line: none expected
     */
    public static void main(String[] args) {
        GameLoop gameLoop = new GameLoop();
        gameLoop.run();
    }
}
