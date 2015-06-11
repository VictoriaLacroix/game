package net.victory.engine.input;


public interface InputController{
    /**
     * Updates the object in relation to control.
     * @param k keystatemanager to read from.
     * @return 
     */
    public int control(KeyStateManager k);
}
