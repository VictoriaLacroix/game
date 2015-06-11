package net.victory.engine;


public interface TimeController{
    /**
     * Updates the this object in relation to time.
     * @param delta relative length of the tick made, for animations and stuff.
     */
    public void update(double delta);   
}
