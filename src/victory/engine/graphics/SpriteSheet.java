package net.victory.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A SpriteSheet class that holds its own pixels and allows drawing to a Screen
 * object
 * 
 * @author Victoria Lacroix
 */
public class SpriteSheet {
    private String path;
    private final int SHEET_WIDTH;
    private final int SHEET_HEIGHT;
    private int pixels[];

    // public int indexSizeX;
    // public int indexSizeY;
    /**
     * Creates SpriteSheet using the specified path.
     * 
     * @param path
     *            path where spritesheet can be found. ("res/file.png")
     */
    public SpriteSheet(String URL) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(URL));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image == null) {
            SHEET_WIDTH = 0;
            SHEET_HEIGHT = 0;
            return;
        }
        SHEET_WIDTH = image.getWidth();
        SHEET_HEIGHT = image.getHeight();
        path = URL;
        /*
         * Grab an array of pixels from the loaded sheet. This mostly uses some
         * java built-ins, but with it we'll be able to directly pull out pixels
         * when we need them.
         */
        pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null,
                0, image.getWidth());
    }


    /**
     * Returns the SpriteSheet object's width.
     * 
     * @return SpriteSheet's width.
     */
    public int getWidth() {
        return SHEET_WIDTH;
    }

    /**
     * Returns the SpriteSheet object's height.
     * 
     * @return SpriteSheet's height.
     */
    public int getHeight() {
        return SHEET_HEIGHT;
    }

    /**
     * Returns the sheet's path.
     * 
     * @return sheet's path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return a single pixel at x,y coordinate. Good if a color needs to be
     *         replaced.
     */
    public int getPixel(int x, int y) {
        return pixels[x + (y * SHEET_WIDTH)];
    }
    
    /**
     * Draws a section of the SpriteSheet on the Screen.
     * This is not the same as most draw()functions and requires the use of a Sprite class.
     * @param x coord to draw on the screen
     * @param y coord to draw on the screen
     * @param ix sheet x to start drawing from
     * @param iy sheet y to start drawing from
     * @param iw sheet width to draw
     * @param ih sheet height to draw
     * @note i* ints are essentially a cross-section of the sheet to draw.
     * @param m image mask/transparency
     * @param s screen to draw to
     */
    public void draw(int x, int y, int ix, int iy, int iw, int ih, int m, Screen s){
        if(ix < 0 || iy < 0){ return; }
        for(int dy = 0; (int)Math.abs(dy) < (int)Math.abs(ih); ++dy){
            for(int dx = 0; (int)Math.abs(dx) < (int)Math.abs(iw); ++dx){
                if(ix + dx < 0 || iy + dy < 0){ break; }
                if(ix + dx >= SHEET_WIDTH || iy + dy >= SHEET_HEIGHT){ break; }
                if(pixels[(ix+dx) + ((iy + dy)*SHEET_WIDTH)] != m){
                    s.writePixel(x + dx, y + dy, pixels[(ix+dx) + ((iy + dy)*SHEET_WIDTH)]);
                }
            }
        }
    }
}
