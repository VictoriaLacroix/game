package net.victory.engine.world;

import net.victory.engine.Core;
import net.victory.engine.GUIEngine;
import net.victory.engine.GUI;
import net.victory.engine.Menu;
import net.victory.engine.Window;
import net.victory.engine.graphics.Screen;
import net.victory.engine.graphics.Sprite;
import net.victory.engine.graphics.SpriteSheet;
import net.victory.engine.input.KeyStateManager;
import net.victory.engine.input.KeyStateManager.Button;

/**
 * An abstract class to describe an entity.
 *
 * @author Victoria Lacroix
 *
 */
public abstract class Entity{

    /**
     * Axis Coordinate
     */
    protected double            xpos, ypos;
    protected double            xposlast, yposlast;

    /**
     * Axis Velocity
     */
    protected double            xvel, yvel;

    /**
     * Axis Velocity Cap
     */
    protected double            xvelmax, yvelmax;

    /**
     * Axis acceleration
     */
    protected double            xacc, yacc;

    /**
     * Dimensions (Size)
     */
    protected int               width, height;

    /**
     * Gravity for this entity.
     */
    protected double            gravity         = 9.8 / 60;

    /**
     * Current graphic setting
     */
    protected Sprite            sprite;

    /**
     * Character direction.
     *      0-Down
     *      1-Up
     *      2-Left
     *      3-Right
     */
    protected int               direction       = 1;
    private double              animCounter     = 0;
    private int                 step            = 0;
    private static final int    COUNTER_RESET   = 25;

    /**
     * New abstract entity with SpriteSheet 'sheet'
     *
     * @param sheet
     */
    public Entity(int w, int h, SpriteSheet sheet){
        xvel = 0;
        yvel = 0;
        xvelmax = 12;
        yvelmax = 12;
        width = w;
        height = h;
        sprite = new Sprite(w, h, sheet);
    }

    public abstract void update(double delta);

    public int control(KeyStateManager input){
        if(input.isButtonDown(Button.DOWN) && !input.isButtonDown(Button.UP)){
            yvel = 1;
            direction = 0;
        }else if(input.isButtonDown(Button.UP)
                && !input.isButtonDown(Button.DOWN)){
            yvel = -1;
            direction = 1;
        }else{
            yvel = 0;
        }
        if(input.isButtonDown(Button.LEFT) && !input.isButtonDown(Button.RIGHT)){
            xvel = -1;
            direction = 2;
        }else if(input.isButtonDown(Button.RIGHT)
                && !input.isButtonDown(Button.LEFT)){
            xvel = 1;
            direction = 3;
        }else{
            xvel = 0;
        }

        if(input.wasButtonPressed(Button.CANCEL)){
            yvel = xvel = yacc = xacc = 0;
            GUIEngine.addGUI(new Menu(0, 0, "Exit Menu", "Quit to Desktop"){});
        }

        return 0;
    }

    /**
     * Collision detection/reaction method for tilemaps/collisionmaps.
     *
     * @param cmap
     *            CollisionMap to correspond to.
     */
    public void checkCollision(CollisionMap cmap){
        /*
         * Okay kid, this is where things get complicated.
         *
         * First of all, we have to use the x,y values for the next frame to
         * determine what the hell is going on, otherwise we'll get weird
         * "snapping" when an entity is consistently trying to move in a direction
         * (eg gravity).
         *
         * Next up, we manipulate WHERE the collision points on each side are
         * (an if statement represents one side). The result is an octagon shape
         * for the collision hitbox. The reasoning behind this is that a 4-point
         * bounding box doesn't give us enough info on *where* collisions are.
         * Case in point, if the bottom left corner is activated, is the
         * character against a wall? Or leaning off a cliff? It leaves too much
         * ambiguity.
         *
         * Finally I wanted to address the math going on... because it's fucking
         * wild, and I try not to cuss much. Basically, when trying to find a
         * coordinate, one must first ask whether or not the axis corresponds to
         * our collision. If we're looking left-right then our x axis
         * corresponds to it, otherwise the y axis. You need to add/remove 4
         * from the other axis to simulate the octagon shape.If you're using width/height (right
         * side/ bottom), then substract it by one. This shrinks the bounding
         * box to prevent java's imperfect double-int conversion from detecting
         * the wrong block. Basically, it's an automated rounding. The final
         * resulting octagon is a little more inside of the entity than outside.
         *
         * Collision detection, in some cases, is still a little hoiky. This is
         * super-early beta stuff so you shouldn't, like, expect miracles.
         */

        // First off, we look at grouped points representing the full collided side. This should take care of high-velocity collisions.

        // Vertical collision (top/bottom)
        if(cmap.getAt(xpos + xvel + 4, ypos + yvel + height - 1)
                && cmap.getAt(xpos + xvel + width - 1 - 4, ypos + yvel + height
                        - 1)){
            // bottom
            ypos += yvel;
            ypos -= ypos % height;
            yvel = (yvel > 0) ? 0 : yvel;
        }else if((cmap.getAt((xpos) + xvel + 4, (ypos) + yvel) && cmap.getAt(
                (xpos) + xvel + (width - 1) - 4, (ypos) + yvel))){
            // top
            if(yvel < 0){
                ypos += yvel; // change position
                ypos += height - (ypos % height);
                yvel = (yvel < 0) ? 0 : yvel; // fix velocity if needed
            }

        }

        // horizontal collision (left/right)
        if((cmap.getAt((xpos) + xvel, (ypos) + yvel + 4) && cmap.getAt((xpos)
                + xvel, (ypos) + yvel + (height - 1) - 4))){
            // left
            xpos += xvel;
            xpos += width - (xpos % width);
            xvel = (xvel < 0) ? 0 : xvel;
        }else if((cmap.getAt((xpos) + xvel + (width - 1), (ypos) + yvel + 4) && cmap
                .getAt((xpos) + xvel + (width - 1), (ypos) + yvel
                        + (height - 1) - 4))){
            // right
            xpos += xvel;
            xpos -= xpos % width;
            xvel = (xvel > 0) ? 0 : xvel;
        }

        // Here, we check weak collision (single points instead of grouped points). This take care of low-velocity collisions.

        // Vertical collision (top/bottom)
        if(cmap.getAt(xpos + xvel + 4, ypos + yvel + height - 1)
                || cmap.getAt(xpos + xvel + width - 1 - 4, ypos + yvel + height
                        - 1)){
            // bottom
            ypos += yvel;
            ypos -= ypos % height;
            yvel = (yvel > 0) ? 0 : yvel;
        }else if((cmap.getAt((xpos) + xvel + 4, (ypos) + yvel) || cmap.getAt(
                (xpos) + xvel + (width - 1) - 4, (ypos) + yvel))){
            // top
            ypos += yvel; // change position
            ypos += height - (ypos % height);
            yvel = (yvel < 0) ? 0 : yvel; // fix velocity if needed
        }

        // horizontal collision (left/right)
        if((cmap.getAt((xpos) + xvel, (ypos) + yvel + 4) || cmap.getAt((xpos)
                + xvel, (ypos) + yvel + (height - 1) - 4))){
            // left
            xpos += xvel;
            xpos += width - (xpos % width);
            xvel = (xvel < 0) ? 0 : xvel;
        }else if((cmap.getAt((xpos) + xvel + (width - 1), (ypos) + yvel + 4) || cmap
                .getAt((xpos) + xvel + (width - 1), (ypos) + yvel
                        + (height - 1) - 4))){
            // right
            xpos += xvel;
            xpos -= xpos % width;
            xvel = (xvel > 0) ? 0 : xvel;
        }
    }

    /**
     * Calculate/Run anything that needs to be finalized.
     *
     * A note on the delta, all velocity values are roughly equal to what the entity will traverse in 1/60th of a second, in pixels.
     */
    public final void nextFrame(double delta){
        animCounter += delta;
        if(animCounter >= COUNTER_RESET){
            step = (step == 0) ? 1 : 0;
            animCounter -= COUNTER_RESET;
        }
        sprite.setIndex(step, direction);
        xposlast = xpos;
        yposlast = ypos;
        xvel += xacc * delta;
        yvel += yacc * delta;
        xvel = xvel > xvelmax ? xvelmax : xvel; // correct larger-than-intended velocities
        yvel = yvel > yvelmax ? yvelmax : yvel;
        xvel = -xvel > xvelmax ? -xvelmax : xvel; // negative case
        yvel = -yvel > yvelmax ? -yvelmax : yvel;
        xpos += xvel * delta;
        ypos += yvel * delta;
    }

    /**
     * Entity collision, to be implemented by subclass.
     *
     * @param other
     */
    public abstract void onCollide(Entity other);

    public boolean isCollidedWith(Entity other){
        // step1
        if(xpos >= other.xpos && xpos < other.xpos + other.width
                && ypos >= other.ypos && ypos < other.ypos + other.height){
            return true;
        }
        // step2
        if(other.xpos >= xpos && other.xpos < xpos + width
                && other.ypos >= ypos && other.ypos < ypos + height){
            return true;
        }
        return false;
    }

    public final double getX(){
        return xpos;
    }

    public double getVelocityX(){
        return xvel;
    }

    public final double getY(){
        return ypos;
    }

    public final double getVelocityY(){
        return yvel;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    /**
     * Determines whether or not this entity should be removed from an active
     * list. I.E if its health is 0, etc.
     *
     * @return
     */
    public abstract boolean getGarbage();

    /**
     * The sprite this entity is using
     *
     * @return
     */
    public Sprite getSprite(){
        return sprite;
    }

    public final void draw(int sx, int sy, Screen s){
        sprite.draw(sx, sy, s);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Entity){
            return ((Entity)other).xpos == xpos && ((Entity)other).ypos == ypos;
        }else{
            return false;
        }
    }
}
