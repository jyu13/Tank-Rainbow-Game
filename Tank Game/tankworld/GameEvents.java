/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 * GameEvents Class
 *
 * Responsible for asynchronously notifying it's subscribers about events that take place in the game.
 * It notifies observers when a keyboard related event occurs, and has a general notification method for
 * other game events.
 */
public class GameEvents extends Observable{

    // Class Variables
    private final int KEY_EVENT = 1;

    private int type;
    private Object event;

    /**
     * Returns the type of game event that occurred. It's either a keyboard event, or a general game related event.
     *
     * @return  the type of game event
     */
    public int getEventType() { return this.type; }

    /**
     * Returns the type of game event the specific game event that occurred.
     *
     * @return  the specific event that occurred
     */
    public Object getEvent() { return this.event; }

    /**
     * Notifies observers of a keyboard related event. It is up to the observers to handle the event.
     *
     * @param e  a particular keyboard related event
     */
    public void setValue(KeyEvent e) {

        this.type = KEY_EVENT; // key input
        event = e;
        setChanged();
        notifyObservers(this);
    }

    /**
     * Notifies observers about other game related events. Up to the observers to determine how they handle
     * the event.
     *
     * @param msg  a message describing the event that occurred
     */
    public void setValue(String msg) {

        type = 2;
        event = msg;
        setChanged();
        notifyObservers(this);
    }
}
