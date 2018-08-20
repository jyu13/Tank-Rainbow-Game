/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.Image;
import java.awt.Rectangle;

abstract public class CollidableObj extends GameObject{
  
    protected boolean collidable;

    CollidableObj(Image image, int xloc, int yloc) {
        super(image, xloc, yloc);
        collidable = true;
    }

    public boolean getCollidable() { return this.collidable; }

    public void setCollidable(boolean collides) { this.collidable = collides; }
  
    public boolean detectCollision(CollidableObj otherObj) {

        if (otherObj == null || !otherObj.collidable)
            return false;

        Rectangle my_area =  new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        Rectangle otherObj_area = new Rectangle(otherObj.getX(), otherObj.getY(), otherObj.getWidth(), otherObj.getHeight());

        // Return true if there is a collision
        return my_area.intersects(otherObj_area);
    }

    abstract public void collision(CollidableObj otherObj);
}
