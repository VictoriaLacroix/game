package victory.engine.graphics;


public interface ScreenController {
    /**
     * Draws this object on the given screen.
     * @param sx screen's x-coord to start drawing
     * @param sy screen's y-coord to start drawing
     * @param s screen to draw to
     */
    public void draw(int sx, int sy, Screen s);
}
