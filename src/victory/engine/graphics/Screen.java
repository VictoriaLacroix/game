package net.victory.engine.graphics;

import java.awt.*;
import java.awt.image.*;

/**
 * This class adds a JComponent (A Canvas, to be specific) that features
 * directly addressable 2D pixels.
 * 
 * @author Victoria Lacroix
 */
@SuppressWarnings("serial")
public class Screen extends Canvas{
    
    private final int       SCREEN_WIDTH, SCREEN_HEIGHT;
    private int             scaleSize;
    private BufferedImage   bufImage;
    private int[]           pixels;
    
    // public SpriteSheet sheet;
    /**
     * A Screen Component.
     * 
     * @param w
     *            Screen width. (in pixels, before scaling)
     * @param h
     *            Screen height. (in pixels, before scaling)
     * @param s
     *            Screen pixel scaling.
     */
    public Screen(int w, int h, int s){
        scaleSize = s;
        SCREEN_WIDTH = w;
        SCREEN_HEIGHT = h;
        bufImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)bufImage.getRaster().getDataBuffer()).getData();
        setMinimumSize(new Dimension(w * s, h * s));
        setMaximumSize(new Dimension(w * s, h * s));
        setPreferredSize(new Dimension(w * s, h * s));
    }
    
    /**
     * Get the Screen's width. Different from getWidth() which is inherited from
     * Component.
     * 
     * @return screen's width
     */
    public int getScreenWidth(){
        return SCREEN_WIDTH;
    }
    
    /**
     * Get the Screen's height. Different from getHeight() which is inherited
     * from Component.
     * 
     * @return screen's height
     */
    public int getScreenHeight(){
        return SCREEN_HEIGHT;
    }
    
    public int getScreenScale(){
        return scaleSize;
    }
    
    /**
     * Clear all pixels on the screen to a color c.
     * 
     * @param c
     *            pixel color to clear to.
     */
    public void clear(int c){
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = c;
        }
    }
    
    /**
     * Writes a pixel to the screen. While this is extremely inefficient, this
     * is mostly designed for ease-of-use. This would definitely be an inline if
     * this was C++.
     * 
     * @param x
     *            coord
     * @param y
     *            coord
     * @param pixel
     *            to write
     */
    public void writePixel(int x, int y, int inPixel){
        if(x >= 0 && x < SCREEN_WIDTH && y >= 0 && y < SCREEN_HEIGHT && pixels.length == SCREEN_WIDTH * SCREEN_HEIGHT){
            pixels[x + (y * SCREEN_WIDTH)] = inPixel;
        }
    }
    
    /**
     * Creates a "scatter" effect to test if the screen is working. Formula is
     * (pixel index * offset)
     * 
     * @param offset
     *            offset for the scatter.
     */
    public void scatter(int offset){
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = i * offset;
        }
    }
    
    /**
     * Randomizes all the pixels on the screen,
     */
    public void randomize(){
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = (int)(Math.random() * 0xFFFFFF);
        }
    }
    
    /**
     * Hardware render for the Screen object. Call at least once per 1/framerate
     * second.
     */
    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(bufImage, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }
}
