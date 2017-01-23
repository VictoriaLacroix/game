package victory.engine.gui;

import victory.engine.gui.KeyStateManager;
import victory.engine.gui.KeyStateManager.Button;
import victory.engine.graphics.Screen;

/**
 * A menu for a videogame.
 * @author victoria
 *
 */
public class DialogWindow extends Window {

    /**
     * Coords of the queue's cursor.
     */
    private int     qx, qy;
    private String  textQueue   = "";

    /**
     * How many ticks have happened since the last write.
     */
    private double  tickCount   = 0;

    /**
     * How long to wait before typing the next char
     */
    private double  tickLength  = 6.0;

    /**
     * Time multiplier for ticks-per-character when printing from queue. Smaller numbers are faster.
     */
    private static final double QUEUE_FACTOR = 0.3;

    /**
     * True if the the user is holding the "Accept" button.
     */
    private boolean skipping = false;

    /**
     * Window Constructor
     * @param w width in tiles
     * @param h height in tiles
     */
    public DialogWindow(int sx, int sy, int w, int h, String s) {
        super(sx, sy, w, h);
        textQueue = s;
        qx = 1; qy = 1;
    }

    public void queue(String s) {
        setupBorders();
        qx = 1; qy = 1;
        textQueue = s;
    }

    /**
     * Scrolls the window's contents upwards, for windows with text in queue and no room.
     * TODO: Add features such as backwards scrolling through history. Make it more generalized.
     */
    private void scrollUp() {
        //Don't drag up the border.
        for(int sy = 1; sy < getHeight()-2; ++sy) {
            for(int sx = 1; sx < getWidth()-1; ++sx){
                put(sx, sy, getAt(sx, sy+1));
            }
        }
        //clear last row
        for(int x = 1; x < getWidth()-1; ++x) {
            put(x, getHeight()-1, (char)0x20);
        }
        --qy;
    }


    @Override
    public void update(double delta) {
        tickCount += delta;
        while(tickCount >= tickLength && textQueue.length() > 0) {
            int wordlength = 0;
            while(wordlength < textQueue.length() && textQueue.charAt(wordlength) != ' ') {
                wordlength++;
            }
            if(qx + wordlength >= getWidth() - 2) {
                qx = 1; ++qy;
            }
            if(qx >= getWidth() - 2 || textQueue.charAt(0) == '\n') {
                qx = 1; ++qy;
            }
            if(qy == getHeight() - 1) {
                scrollUp();
            }
            put(qx, qy, textQueue.charAt(0));
            textQueue = textQueue.substring(1);
            if(skipping){
                tickCount -= tickLength * QUEUE_FACTOR;
            }else{
                tickCount -= tickLength;
            }
            ++qx;
        }
        skipping = false;
    }

    @Override
    public int control(KeyStateManager k) {
        if(textQueue.equals("")) {
            put(getWidth()-2, getHeight()-2, (char)0x02);
        }
        if((k.wasButtonPressed(Button.ACCEPT) || k.wasButtonPressed(Button.CANCEL)) && textQueue.equals("")) {
            return 1;
        } else if(k.isButtonDown(Button.ACCEPT)) {
            skipping = true;
        }
        return 0;
    }


}
