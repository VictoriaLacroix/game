package victory.engine.gui;

import victory.engine.gui.KeyStateManager;
import victory.engine.gui.KeyStateManager.Button;
import victory.engine.graphics.Screen;

/**
 * A menu for a videogame.
 * @author victoria
 *
 */
public class Menu extends Window {

    private String[]    choices;
    private int         current;
    private boolean     valid;

    /**
     * Constructor
     * @param nx coord
     * @param ny coord
     * @param s options
     */
    public Menu(int nx, int ny, String... s) {
        super(nx, ny, maxLength(s)+3, s.length+2);

        for(int i = 0; i < s.length; ++i) {
            write(2, i+1, s[i]);
        }

        choices = s;
    }

    @Override
    public int control(KeyStateManager k) {
        //selection logic
        if(k.wasButtonPressed(Button.ACCEPT)) {
            valid = true;
        } else if(k.wasButtonPressed(Button.CANCEL)) {
            valid = true;
            current = 0;
        } else if(k.wasButtonPressed(Button.UP)) {
            --current;
        } else if(k.wasButtonPressed(Button.DOWN)) {
            ++current;
        }

        //adjust if rolled over
        if(current < 0) current = choices.length - 1;
        if(current >= choices.length) current = 0;

        //Return logic
        if(!valid) {
            return 0;
        } else {
            //fire(current);
            return current+1;
        }
    }

    @Override
    public void update(double delta) {
        // update cursor position
        for(int i = 1; i <= choices.length; ++i) {
            put(1, i, ' ');
        }
        put(1, current+1, (char)0x01);
    }

    /**
     * Helper method.
     * @param s array to sort through
     * @return the length of the longest string in the array.
     */
    private static int maxLength(String[] s) {
        int res = 0;
        for(int i = 0; i < s.length; ++i) res = (s[i].length() > res) ? s[i].length(): res;
        return res;
    }

    /**
     * Called when the menu resolves. By default, does nothing but can be overridden in anonymous or sub-classes.
     */
    private void fire(int choice) {
        return;
    }
}
