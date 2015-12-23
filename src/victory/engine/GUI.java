package net.victory.engine;

import net.victory.engine.graphics.ScreenController;
import net.victory.engine.input.InputController;

/**
 * Implements input control, time updating and output - a GUI.
 * @author Victoria Lacroix
 */
public interface GUI extends InputController, ScreenController, TimeController{}
