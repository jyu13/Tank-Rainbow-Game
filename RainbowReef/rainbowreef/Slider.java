/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Slider extends MoveableObj {

    private boolean moveLeft, moveRight;

    public Slider(Image img, int xloc, int yloc, int xspd) {

        super(img, xloc, yloc, xspd, 0); // slider only moves left and right
    }

    public void setMoveLeft (boolean left) { this.moveLeft = left; }

    public void setMoveRight (boolean right) { this.moveRight = right; }

    public boolean getMovingRight() { return this.moveRight; }

    public boolean getMovingLeft() { return this.moveLeft; }
    
    /**
     * update movement of the slider
     */
    @Override
    public void move() {
        // Assumes the Slider never changes it's y-coordinate

        if (moveLeft) {
            this.prevX = this.x;
            setX(x - this.xSpeed);
        }

        if (moveRight) {
            this.prevX = this.x;
            setX(x + this.xSpeed);
        }
    }

    /**
     * 
     * @param g
     * @param obs 
     */
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        g.drawImage(this.img, this.x, this.y, obs);
    }

    public void collision(CollidableObj otherObj){

        if (otherObj instanceof Wall) {
            if (moveLeft) {
                setX(x + (2 * xSpeed));
            }

            if (moveRight) {
                setX(x - (2 * xSpeed));
            }
        }
    }

}
