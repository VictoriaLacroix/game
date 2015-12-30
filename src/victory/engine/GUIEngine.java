package net.victory.engine;

import java.util.Stack;

import net.victory.engine.graphics.Screen;
import net.victory.engine.input.KeyStateManager;

/**
    Effectively the game's GUI Manager.
*/
public class GUIEngine{

    /**
     * Screen Dimensions.
     */
    public static int screenWidth, screenHeight;

    /**
     * Whether the game is paused.
     */
    private static boolean paused;

    /**
     * Tangibles in the BaseEngine
     */
    private static Stack<GUI> Interfaces;

    public static void init(int w, int h){
        screenWidth = w;
        screenHeight = h;

        Interfaces = new Stack<GUI>();
    }

    public static void addGUI(GUI g){
        Interfaces.push(g);
    }

    public static int size(){
        return Interfaces.size();
    }

    public static void control(KeyStateManager k){
        if(Interfaces.size() > 0){
            int res = Interfaces.peek().control(k);
            if(res != 0){
                Interfaces.pop();
            }
        }
    }

    public static void draw(int sx, int sy, Screen s){
        for(int i = 0; i < Interfaces.size(); ++i){
            Interfaces.get(i).draw(sx, sy, s);
        }
    }

    public static void update(double delta){
        for(int i = 0; i < Interfaces.size(); ++i){
            Interfaces.get(i).update(delta);
        }
    }
}
