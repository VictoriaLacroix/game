package net.victory.engine;

import java.util.Stack;

import net.victory.engine.graphics.Screen;
import net.victory.engine.input.KeyStateManager;

public class BaseEngine{
    
    /**
     * Screen Dimensions.
     */
    public static int   screenWidth, screenHeight;
    
    /**
     * Whether the game is paused.
     */
    private static boolean  paused;
    
    /**
     * Tangibles in the MapEngine
     */
    private static Tangible[]   tangibles;
    
    /**
     * Amount of tangibles currently in the baseengine.
     */
    private static int      manyTangibles   = 0;
    
    /**
     * Who's controlling the action
     */
    private static Tangible director;
    
    private static Stack<Tangible>  order;
    
    public static void init(int w, int h){
        screenWidth = w;
        screenHeight = h;
        
        order = new Stack<Tangible>();
        
        tangibles = new Tangible[32];
    }
    
    /**
     * Adds entity e to the list.
     * @param e
     */
    public static void addTangible(Tangible e){
        if(manyTangibles <= tangibles.length){
            int i;
            for(i = 0; tangibles[i] != null; i++);
            tangibles[i] = e;
            manyTangibles++;
            if(director == null){
                director = e; // attaches camera if it is null.
            }
        }else{
            System.err.println("Tangibles full in MapEngine.");
        }
    }
    
    /**
     * Adds a tangible, and sets it as the director
     * @param e
     */
    public static void focusTangible(Tangible e){
        order.push(director);
        director = e;
        addTangible(e);
    }
    
    /**
     * Removes at index.
     * @param i
     */
    public static void removeTangible(int i){
        if(manyTangibles > 0){
            tangibles[i] = tangibles[--manyTangibles];
            tangibles[manyTangibles] = null;
        }
    }
    
    /**
     * Removes equating (note: bad method)
     * @param target
     */
    public static void removeTangible(Tangible target){
        int i;
        for(i = 0; !(target == tangibles[i]); i++);
        if(i == tangibles.length){
            return;
        }
        removeTangible(i);
    }

    public static int control(KeyStateManager k){
        int res = director.control(k);
        if(res < -1){
            removeTangible(director);
            director = order.pop();
        }
        return -1;
    }

    public static void draw(int sx, int sy, Screen s){
        for(int i = 0; i < manyTangibles; ++i){
            tangibles[i].draw(sx, sy, s);
        }
        
    }

    public static void update(double delta){
        for(int i = 0; i < manyTangibles; ++i){
            tangibles[i].update(delta);
        }
    }   
}
