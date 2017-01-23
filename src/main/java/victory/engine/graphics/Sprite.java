package victory.engine.graphics;

import victory.engine.gui.KeyStateManager;

/**
 * An instance-based wrapper for the spritesheet class.
 * @author Victoria Lacroix
 *
 */
public class Sprite implements ScreenController {
    SpriteSheet sheet;
    private int indX, indY; // xy index
    private final int tileX, tileY; // how many tiles horizontally, vertically.
    private final int width, height; // width of the image in question.
    private int mask = 0xFFFF00FF;

    public Sprite(int w, int h, SpriteSheet spritesheet) {
        sheet = spritesheet;
        width = w;
        height = h;
        tileX = sheet.getWidth() / width;
        tileY = sheet.getHeight() / height;
    }

    /**
     * Sets the current sprite index.
     * 
     * @param x
     * @param y
     */
    public void setIndex(int x, int y) {
        if(x < tileX && x >= 0){
            indX = x;
        }
        if(y < tileY && y >= 0){
            indY = y;
        }
    }

    /**
     * 
     * @return x-index on the spritesheet
     */
    public int getIndexX() {
        return indX;
    }

    /**
     * 
     * @return y-index on the spritesheet
     */
    public int getIndexY(){
        return indY;
    }

    @Override
    public void draw(int sx, int sy, Screen s){
        sheet.draw(sx, sy, indX*width, indY*height, width, height, mask, s);
    }
}
