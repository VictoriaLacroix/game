package net.victory.engine.world;

import net.victory.engine.graphics.SpriteSheet;

/**
 * An extension of the Entity class that accepts user input and feeds it to the
 * player character.
 * 
 * @author Victoria Lacroix
 */
public class Player extends Entity{
    
    public Player(double x, double y){
        super(16, 16, new SpriteSheet("res/td-char.png"));
        sprite.setIndex(0, 0);
        this.xpos = x;
        this.ypos = y;
    }
    
    @Override
    public void onCollide(Entity other){
        //TODO I dunno, something.
    }
    
    @Override
    public boolean getGarbage(){
        // TODO determine if this entity is to be processed at all
        return false;
    }

    @Override
    public void update(double delta){
        // TODO Auto-generated method stub
        
    }
}
