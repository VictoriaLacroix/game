package victory.engine;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.*;

import victory.engine.graphics.Screen;
import victory.engine.graphics.SpriteSheet;
import victory.engine.gui.KeyStateManager;
import victory.engine.gui.GUIEngine;
import victory.engine.world.Map;
import victory.engine.world.MapEngine;
import victory.engine.world.Player;

/**
    Core class that performs the main logic in a game. It handles timing for
    graphics drawing, and calls various update() methods for different objects.
    It also handles windowing in the game.
    @author Victoria Lacroix
*/
public class Core extends JPanel {

    private Screen              screen;

    // Changing this number will cause strange things to happen with timing.
    private static final int    FRAMERATE   = 60;

    private boolean             running     = false;
    int                         width, height;
    protected static int        tickCount   = 0;

    private KeyStateManager     buttonManager;

    public Core(int w, int h, int s){
        setLayout(new BorderLayout());
        buttonManager = new KeyStateManager();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
            addKeyEventDispatcher(buttonManager);
        width = w;
        height = h;
        screen = new Screen(w, h, s);
        add(screen);

        GUIEngine.init(w, h);

        MapEngine world =
            new MapEngine(w, h, new Map(32, 32, new SpriteSheet("world.png"), "map/csv/world.csv"));
        world.addEntity(new Player(128, 128));
        world.attachInput(0);
        GUIEngine.addGUI(world);

        running = true;
    }

    public int getWidth(){
        return screen.getSize().width;
    }

    public int getHeight(){
        return screen.getSize().height;
    }

    /**
        Update method that is called indefinitely from Window.

        @author Victoria Lacroix #0296738 (Current form)
        @author youtube.com/user/thech3rno (Original single-class form that
                included really basic software rendering (see screen class).
                About half of the code in this method is his.)
     */
    public void update(){
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / FRAMERATE;
        int rendersThisSecond = 0;
        long tickTimer = System.currentTimeMillis();
        double delta = 0;
        while(running){
            long now = System.nanoTime();
            delta = (now - lastTime) / nsPerTick;
            lastTime = now;
            rendersThisSecond++;
            do {
                tick((delta >= 1d) ? 1d : delta);
                delta--;
            } while(delta > 0);

            draw();

            try {
                Thread.sleep(2);
            } catch(InterruptedException e) {
                // Exit if we can't thread
                e.printStackTrace();
            }

            render();

            if(System.currentTimeMillis() - tickTimer > 1000) {
                tickTimer += 1000;
                System.err.println(rendersThisSecond + "fps");
                rendersThisSecond = 0;
            }
        }
    }

    /**
        Game logic method.
    */
    protected void tick(double delta) {
        GUIEngine.control(buttonManager);
        GUIEngine.update(delta);
        buttonManager.update();
    }

    /**
        Graphics drawing method.
    */
    protected void draw() {
        GUIEngine.draw(0, 0, screen);
    }

    /**
        Hardware render method.
    */
    private void render() {
        screen.render();
    }

    /**
        Get the screen of the core.

        @return this Core's Screen
     */
    protected Screen getScreen() {
        return screen;
    }
}
