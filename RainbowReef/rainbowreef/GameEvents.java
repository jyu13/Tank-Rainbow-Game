/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.event.KeyEvent;
import java.util.Observable;

public class GameEvents extends Observable{

    protected int type;
    protected final int keyE = 1;
    protected Object event;
    
    public void setValue(KeyEvent e) {

        type = keyE; // key input
        event = e;
        setChanged();
        notifyObservers(this);
    }

    public void setValue(String msg) {
        type = 2;
        event = msg;
        setChanged();
        notifyObservers(this);
    }
}