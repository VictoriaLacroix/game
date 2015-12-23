package net.victory.engine;

import net.victory.engine.graphics.Screen;
import net.victory.engine.graphics.Sprite;
import net.victory.engine.graphics.SpriteSheet;
import net.victory.engine.input.KeyStateManager;
import net.victory.engine.input.KeyStateManager.Button;

public class Window implements GUI{
    private int x, y;
    private int width, height;
    /**
     * 0x10 = Top Left
     * 0x11 = Top
     * 0x12 = Top Right
     * 0x13 = Left
     * 0x14 = Right
     * 0x15 = Bottom Left
     * 0x16 = Bottom
     * 0x17 = Bottom Right
     * 0x20 = Blank/Space
     * 0x21+ = ASCII text
     */
    private char[] win;
    static SpriteSheet gfx = new SpriteSheet("res/text.png");

    private String  textQueue   = "";

    /**
     * Coords of the queue's cursor.
     */
    private int     qx, qy;

    /**
     * How many ticks have happened since the last write.
     */
    private double  tickCount   = 0;

    /**
     * How long to wait before typing the next char
     */
    private double  tickLength  = 6.0;

    /**
     * True if the the user is holding the "Accept" button.
     */
    private boolean skipping = false;

    /**
     * Time multiplier for ticks-per-character when printing from queue. Smaller numbers are faster.
     */
    private static final double QUEUE_FACTOR = 0.3;

    public Window(int sx, int sy, int w, int h){
        new Window(sx, sy, w, h, "");
    }

    /**
     * Window Constructor
     * @param w width in tiles
     * @param h height in tiles
     */
    public Window(int sx, int sy, int w, int h, String s){
        x = sx;
        y = sy;
        textQueue = s;
        qx = 1; qy = 1;
        setSize(w, h);
    }

    /**
     * Sets the window's size
     * @param w new width of the window
     * @param h new height of the window
     */
    public void setSize(int w, int h){
        width = (w > 1) ? w: 2;
        height = (h > 1) ? h: 2;
        win = new char[width*height];
        setupBorders();
    }

    /**
     * Set up a blank, bordered window.
     */
    private void setupBorders(){
        for(int i = 0; i<width*height; ++i){
            if(i < width){
                if(i == 0){
                    win[i] = 0x10;  //Top Left
                }else if(i < width -1){
                    win[i] = 0x11;  //Top
                }else{
                    win[i] = 0x12;  //Top Right
                }
            }else if(i < width * (height-1)){
                if(i % width == 0){
                    win[i] = 0x13;  //Left
                }else if(i % width < width-1){
                    win[i] = 0x20;  //Space
                }else{
                    win[i] = 0x14;  //Right
                }
            }else{
                if(i % width == 0){
                    win[i] = 0x15;  //Bottom Left
                }else if(i % width < width-1){
                    win[i] = 0x16;  //Bottom
                }else{
                    win[i] = 0x17;  //Bottom Right
                }
            }
        }
    }

    /**
     * Writes a string to this window.
     * @param s string to write.
     */
    public void write(String s){
        //Assumes the wanted string will be written in the top-left corner of the window.
        write(1, 1, s);
    }

    /**
     * Writes a string to the window.
     * @param x coord to start writing at.
     * @param y coord to start writing at.
     * @param s string to write.
     */
    public void write(int x, int y, String s){
        char str[] = s.toCharArray();
        for(int i = 0; i < str.length; ++i){
            if(str[i] == '\n' || x >= width-1){ x = 1; ++y; }
            else{
                win[x+(y*width)] = str[i];
                ++x;
            }
            if(y == height-1) return;
        }
    }

    /**
     * Writes a char to the window.
     * @param cx coord.
     * @param cy coord.
     * @param c char to write.
     */
    public void put(int cx, int cy, char c){
        if(cx >= 0 && cy >= 0 && cx < width && cy < height){
            win[cx+(cy*width)] = c;
        }
    }

    public void queue(String s){
        setupBorders();
        qx = 1; qy = 1;
        textQueue = s;
    }

    /**
     * Scrolls the window's contents upwards, for windows with text in queue and no room.
     * TODO: Add features such as backwards scrolling through history. Make it more generalized.
     */
    private void scrollUp(){
        //Don't drag up the bottom border.
        for(int y = 1; y < height-2; ++y){
            for(int x = 1; x < width-1; ++x){
                win[(y*width)+x] = win[((y+1)*width)+x];
            }
        }
        //clear last row
        for(int x = 1; x < width-1; ++x){
            win[((height-2)*width)+x] = 0x20;
        }
        --qy;
    }

    @Override
    public void update(double delta){
        tickCount += delta;
        while(tickCount >= tickLength && textQueue.length() > 0){
            int wordlength = 0;
            while(wordlength < textQueue.length() && textQueue.charAt(wordlength) != ' '){
                wordlength++;
            }
            if(qx + wordlength >= width-2){
                qx = 1; ++qy;
            }
            if(qx >= width-2 || textQueue.charAt(0) == '\n'){
                qx = 1; ++qy;
            }
            if(qy == height-1){
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
    public int control(KeyStateManager k){
        if(textQueue.equals("")){
            put(width-2, height-2, (char)0x02);
        }
        if((k.wasButtonPressed(Button.ACCEPT) || k.wasButtonPressed(Button.CANCEL)) && textQueue.equals("")){
            return 1;
        }else if(k.isButtonDown(Button.ACCEPT)){
            skipping = true;
        }
        return 0;
    }

    @Override
    public void draw(int sx, int sy, Screen s){
        Sprite brush = new Sprite(8, 8, gfx);
        for(int dy = 0; dy < height; dy++){
            for(int dx = 0; dx < width; dx++){
                brush.setIndex(win[dx+(dy*width)]%0x10, win[dx+(dy*width)]/0x10);
                brush.draw(dx*8 + x*8, dy*8 + y*8, s);
            }
        }
    }
}
