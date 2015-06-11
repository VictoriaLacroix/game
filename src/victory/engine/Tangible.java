package net.victory.engine;

import net.victory.engine.graphics.ScreenController;
import net.victory.engine.input.InputController;

/**
 * A simple interface that forces a class to implement a 'draw()' method detailed here.
 * @author Victoria Lacroix
 *
 */
public interface Tangible extends InputController, ScreenController, TimeController{}
