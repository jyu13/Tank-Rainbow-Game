/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Wall extends CollidableObj {

    public Wall(Image img, int xloc, int yloc) {

        super(img, xloc, yloc);

    }

    // Walls don't have behavior on collision
    public void collision(CollidableObj otherObj) {}
    
    /**
     * 
     * @param g
     * @param obs 
     */
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {

        g.drawImage(this.img, this.x, this.y, obs);
    }
}
