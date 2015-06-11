package net.victory.engine;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.*;

import net.victory.engine.graphics.Screen;
import net.victory.engine.graphics.SpriteSheet;
import net.victory.engine.input.KeyStateManager;
import net.victory.engine.world.Map;
import net.victory.engine.world.MapEngine;
import net.victory.engine.world.Player;

@SuppressWarnings("serial")
/**
 * Core class that performs the main logic in a game. It handles timings for graphics drawings, and calls various update() methods for different objects. It also handles windowing in a game.
 * @author Victoria Lacroix
 *
 */
public class Core extends JPanel{
    
    // our output
    private Screen              screen;
    // visuals
    private static final int    FRAMERATE   = 60;   // Target FPS. NOTE: Entity
                                                    // movement is largely dependent
                                                    // on this number. You've been
                                                    // warned.
    
    // under-the-hood
    private boolean             running     = false;
    int                         width, height;
    protected static int        tickCount   = 0;
    // game-ey stuff
    private KeyStateManager     buttonManager;
    private BaseEngine          engine;
    
    public Core(int w, int h, int s){
        setLayout(new BorderLayout());
        buttonManager = new KeyStateManager();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(buttonManager);
        width = w;
        height = h;
        screen = new Screen(w, h, s);
        add(screen);
        
        BaseEngine.init(w, h);
        
        MapEngine me = new MapEngine(w, h, new Map(32, 32, new SpriteSheet("res/world.png"), "res/map/csv/world.csv"));
        me.addEntity(new Player(128, 128));
        me.attachInput(0);
        BaseEngine.addTangible(me);
        
        running = true;
    }
    
    public int getWidth(){
        return screen.getSize().width;
    }
    
    public int getHeight(){
        return screen.getSize().height;
    }
    
    /**
     * Update method that is called indefinitely from Window.
     * 
     * @author Victoria Lacroix #0296738 (Current form)
     * @author youtube.com/user/thech3rno (Original single-class form that
     *         included really basic software rendering (see screen class).
     *         About half of the code in this method is his.)
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
            /*
             * What's being done here is that the delta value will be capped at
             * 1 to prevent collision issues. If the game is rendering below
             * 60fps, it will process multiple ticks before rendering again.
             * This also prevents collision failures when the game needs to reload quickly
             * from sleep. If the game renders above 60fps, then it will just do
             * a part of a tick, which means that movement should be smoother.
             * In other terms, the game's framerate is essentially uncapped, and
             * technically unfloored, but the tickrate is floored at 60tps.
             */
            do{
                tick((delta >= 1d) ? 1d : delta);
                delta--;
            }while(delta > 0);
            // S/W draw
            draw();
            try{
                Thread.sleep(2);
            }catch(InterruptedException e){
                // crash if for some reason we can't thread
                e.printStackTrace();
            }
            // H/W render
            render();
            // Debug print out the amount of frames in the last second.
            if(System.currentTimeMillis() - tickTimer > 1000){
                tickTimer += 1000;
                System.err.println(rendersThisSecond + "fps");
                rendersThisSecond = 0;
            }
        }
    }
    
    /**
     * Game logic method.
     */
    protected void tick(double delta){
        BaseEngine.control(buttonManager);
        BaseEngine.update(delta);
        buttonManager.update();
    }
    
    /**
     * Graphics drawing method.
     */
    protected void draw(){
        BaseEngine.draw(0, 0, screen);
    }
    
    /**
     * Hardware render method.
     */
    private void render(){
        screen.render();
    }
    
    /**
     * Shallow return for the Core's Screen object.
     * 
     * @return this Core's Screen
     */
    protected Screen getScreen(){
        return screen;
    }
}
