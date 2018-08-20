/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 */
abstract public class CollidableObj extends GameObject{

    // Class variables
    protected boolean collidable, visible;

    /**
     *
     * @param image
     * @param xloc
     * @param yloc
     */
    CollidableObj(Image image, int xloc, int yloc) {
        super(image, xloc, yloc);
        collidable = false;
    }

    /**
     *
     * @param otherObj
     */
    abstract public void collision(CollidableObj otherObj);

    /**
     *
     * @param otherObj
     * @return
     */
    public boolean detectCollision(CollidableObj otherObj) {

        boolean collision = false;

        if (this.collidable && otherObj.collidable) {
            Rectangle my_area = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            Rectangle otherObj_area = new Rectangle(otherObj.getX(), otherObj.getY(), otherObj.getWidth(), otherObj.getHeight());

            collision = my_area.intersects(otherObj_area);
        }
        // Return true if there is a collision
        return collision;
    }
}
