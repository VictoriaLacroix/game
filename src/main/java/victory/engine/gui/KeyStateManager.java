package victory.engine.gui;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

// USAGE:
// KeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
// KeyStateManager = new KeyStateManager();
// KeyboardFocusManager.addKeyEventDispatcher(KeyStateManager);
// In a JFrame class.
/**
 * Contains memory for and handles keyboard events. See "USAGE" in source code.
 *
 * @author Victoria Lacroix
 */
public class KeyStateManager implements KeyEventDispatcher {
    /**
     * Array of keys representing the state of keys as they are in the current frame.
     */
    private boolean keys[];
    /**
     * Array of keys representing the state of keys as they were in the last frame.
     */
    private boolean keysLast[];

    public KeyStateManager() {
        keys = new boolean[KeyEvent.KEY_LAST + 1];
        keysLast = new boolean[KeyEvent.KEY_LAST + 1];
    }

    /**
     * Moves the currently pressed keys into the "previous" keys. Useful for multi-frame input, such as checking for a
     * button's release before allowing another press of it.
     */
    public void update() {
        for(int i = 0; i < keys.length; i++) {
            keysLast[i] = keys[i];
        }
    }

    /**
     * Checks if a key was pressed (current frame yes, last frame no)
     *
     * @param i
     *            keyCode to check
     * @return whether the key was pressed.
     */
    public boolean isPressed(int i) {
        return (keys[i] && !keysLast[i]);
    }

    /**
     * Checks if a key was released (current frame no, last frame yes)
     *
     * @param i
     *            keyCode to check
     * @return whether the key was released.
     */
    public boolean isReleased(int i) {
        return (keysLast[i] && !keys[i]);
    }

    /**
     * Checks if the key is currently down (current frame yes, last frame doesn't matter)
     *
     * @param i
     *            keyCode to check
     * @return whether the key is down.
     */
    public boolean isDown(int i) {
        return keys[i];
    }

    // see button enum below that defines our keys.
    public boolean isButtonDown(Button b) {
        return isDown(b.index);
    }

    public boolean wasButtonPressed(Button b) {
        return isPressed(b.index);
    }

    /**
     * Button enumerated type. It contains 6 buttons.
     *
     * @author Victoria
     */
    public static enum Button {
        UP(KeyEvent.VK_W),
        DOWN(KeyEvent.VK_S),
        LEFT(KeyEvent.VK_A),
        RIGHT(KeyEvent.VK_D),
        ACCEPT(KeyEvent.VK_SPACE),
        CANCEL(KeyEvent.VK_TAB);

        int index;

        Button(int i) {
            index = i;
        }

        int get() {
            return index;
        }
    }

    /**
     * Overridden from keyEventDispatcher. Reads key events from the keyboard, and updates the array kept in this class
     * (keys) correspondingly.
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent key) {
        if(key.getKeyCode() < keys.length) {
            if(key.getID() == KeyEvent.KEY_PRESSED) {
                keys[key.getKeyCode()] = true;
            } else if(key.getID() == KeyEvent.KEY_RELEASED) {
                keys[key.getKeyCode()] = false;
            }
            return true; //the original method demands a boolean return so we'll just return true.
        } else {
            return false;
        }
    }
}
