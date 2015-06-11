package net.victory.engine;

import net.victory.engine.input.KeyStateManager;
import net.victory.engine.input.KeyStateManager.Button;

/**
 * A menu for a videogame.
 * @author victoria
 *
 */
public class Menu extends Window{
    
    private String[]    choices;
    private int         current;
    private boolean     valid;
    
    /**
     * Constructor
     * @param nx coord
     * @param ny coord
     * @param s options
     */
    public Menu(int nx, int ny, String... s){
        super(nx, ny, maxLength(s)+3, s.length+2);
        
        for(int i = 0; i < s.length; ++i){
            write(2, i+1, s[i]);
        }
        
        choices = s;
    }
    
    public int control(KeyStateManager k){
        //selection logic
        if(k.wasButtonPressed(Button.UP)){
            --current;
        }else if(k.wasButtonPressed(Button.DOWN)){
            ++current;
        }else if(k.wasButtonPressed(Button.A)){
            return current;
        }else if(k.wasButtonPressed(Button.START) || k.wasButtonPressed(Button.B)){
            return -2;
        }
        if(current < 0) current = choices.length - 1;
        if(current >= choices.length) current = 0;
        
        //Alter window
        for(int i = 1; i <= choices.length; ++i){
            put(1, i, ' ');
        }
        put(1, current+1, (char)0x01);
        
        //Return logic
        if(!valid){
            return -1;
        }else{
            return current;
        }
    }
    
    /**
     * Helper method.
     * @param s array to sort through
     * @return the length of the longest string in the array.
     */
    private static int maxLength(String[] s){
        int res = 0;
        for(int i = 0; i < s.length; ++i) res = (s[i].length() > res) ? s[i].length(): res;
        return res;
    }
}
