/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.Image;

abstract public class GameObject {

    protected int x, y, xCenter, yCenter, width, height;
    protected Image img;
    protected boolean visible;
    
    public GameObject(Image img, int x, int y) {

        // Note: x and y refer to the top-left of the image.
        //
        // Coordinate system :     --> +x
        //                     +y |
        //                        v
        this.x = x;
        this.y = y;

        this.width = img.getWidth(null);
        this.height = img.getHeight(null);

        this.xCenter = x + (width / 2);
        this.yCenter = y + (width / 2);

        this.visible = true;
        this.img = img;
    }

    /**
     *
     * @return x position
     */
    public int getX() { return this.x; }

    /**
     *
     * @return y position
     */
    public int getY() { return this.y; }

    /**
     *
     * @return width of this image
     */
    public int getWidth() { return this.width; }

    /**
     *
     * @return height of this image
     */
    public int getHeight() { return this.height; }
  
    public int getXCenter() { return this.xCenter; }

    public int getYCenter() { return this.yCenter; }


    /**
     * set x position to x value
     * @param x 
     */
    public void setX(int x){
        this.x = x;
        this.xCenter = x + (width / 2);
    }

    /**
     * set y position to y value
     * @param y 
     */
    public void setY(int y) {
      
        this.y = y;
        this.yCenter = y + (height / 2);
    }

    abstract public void draw(Graphics2D g, ImageObserver obs);
}
