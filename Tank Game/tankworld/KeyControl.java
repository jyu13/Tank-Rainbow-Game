/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 */
public class KeyControl extends KeyAdapter {

    private GameEvents gameE;

    /**
     * Class Constructor
     *
     * @param  ge  a game event
     */
    public KeyControl(GameEvents ge){
        this.gameE = ge;
    }

    /**
     * Creates a game event for when a key is pressed down.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e){
        gameE.setValue(e);
    }

    /**
     * Creates a game event for when a key is released.
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        gameE.setValue(e);
    }
}
