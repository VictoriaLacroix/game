package victory.engine;

import java.awt.*;
import javax.swing.*;
import victory.engine.*;

/**
 * Base class that handles the relationship between keyboards and the screen itself.
 * 
 * @author Victoria Lacroix 
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements Runnable {

    // important final integers in regards to graphics.
    private static final int        SCALE               = 2;
    private static final int        GAME_SCREEN_WIDTH   = 320,
                                    GAME_SCREEN_HEIGHT  = 240;
    private static final String     GAME_TITLE          = "Victory Engine 0.0.1c";
    /**
     * Game logic.
     */
    private Core                    game                = new Core(GAME_SCREEN_WIDTH, GAME_SCREEN_HEIGHT, SCALE);

    /**
     * Creates new Game object.
     */
    public GameFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle(GAME_TITLE);
        // screen component
        add(game, BorderLayout.CENTER);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        new Thread(this).start(); //pops off a thread that simply continually runs a Core.
    }

    /**
     * Creates a new Game object and jumps straight to the game logic. Prevents anything from running as "static"
     * 
     * @param args
     *            command-line arguments.
     */
    public static void main(String args[]) {
        new GameFrame();
    }

    /**
     * Starts a new thread using this Game object as the thread.
     */
    public synchronized void start() {
        new Thread(this).start();
    }

    /**
     * Thread.run() method, constantly updates the Core.
     */
    public void run() {
        game.update();
    }

    /**
     * Stops the thread.
     */
    public synchronized void stop() {
        System.exit(0);
    }
}
