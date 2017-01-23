package victory.engine.world;

import victory.engine.graphics.SpriteSheet;
import victory.engine.gui.*;
import victory.engine.gui.KeyStateManager.Button;

/**
 * An extension of the Entity class that accepts user input and feeds it to the
 * player character.
 * 
 * @author Victoria Lacroix
 */
public class Player extends Entity {

    public Player(double x, double y) {
        super(16, 16, new SpriteSheet("td-char.png"));
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
        // TODO determine if this entity is to be delet
        return false;
    }

    @Override
    public void update(double delta){
        // TODO Auto-generated method stub
    }

    public int control(KeyStateManager input) {
        if(input.isButtonDown(Button.DOWN) && !input.isButtonDown(Button.UP)) {
            yvel = 1;
            direction = 0;
        } else if(input.isButtonDown(Button.UP) && !input.isButtonDown(Button.DOWN)) {
            yvel = -1;
            direction = 1;
        } else {
            yvel = 0;
        }

        if(input.isButtonDown(Button.LEFT) && !input.isButtonDown(Button.RIGHT)) {
            xvel = -1;
            direction = 2;
        } else if(input.isButtonDown(Button.RIGHT) && !input.isButtonDown(Button.LEFT)) {
            xvel = 1;
            direction = 3;
        } else {
            xvel = 0;
        }

        if(input.wasButtonPressed(Button.CANCEL)) {
            yvel = xvel = yacc = xacc = 0;
            GUIEngine.addGUI(new Menu(0, 0, "Party", "Inventory", "Config", "Save"));
        }

        return 0;
    }
}
