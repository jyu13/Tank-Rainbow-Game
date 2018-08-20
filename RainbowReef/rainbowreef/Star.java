/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;

public class Star extends MoveableObj {

    private int initialX, initalY, initialXspd, initalYspd;

    public Star(Image img, int xloc, int yloc, int xspd, int yspd) {

        super(img, xloc, yloc, xspd, yspd);

        initialX = xloc;
        initalY = yloc;
        initialXspd = xspd;
        initalYspd = yspd;
    }
    
    public void collision(CollidableObj otherObj) {

       int side;

       side = whichSideForCollision(otherObj);

       //System.out.println(side);

       if (side == 2 || side == 3) {

           this.x = prevX;
           xSpeed = -xSpeed;
       }
       else if (side == 1 || side == 0) {

           this.y = prevY;

           if (otherObj instanceof Slider) {
               if (((Slider) otherObj).getMovingRight())
                   xSpeed += ((Slider) otherObj).getXSpeed() / 2;

               else if (((Slider) otherObj).getMovingLeft())
                   xSpeed -= ((Slider) otherObj).getXSpeed() / 2;
           }

           ySpeed = -ySpeed;
       }
       else
           System.out.println("Congratulations, collision is broken");
    }

    public void resetStar() {

        setX(initialX);
        setY(initalY);
        this.prevX = initialX;
        this.prevY = initalY;
        this.xSpeed = initialXspd;
        this.ySpeed = initalYspd;

    }

    @Override
    public void draw(Graphics2D g, ImageObserver obs){
        g.drawImage(this.img, this.x, this.y, obs);
    }
    /**
     * change x, y position based on direction
     */
    public void move(){

        prevX = x;
        prevY = y;
        setX(x + xSpeed);
        setY(y + ySpeed);
    }

    /*
     * Check which side of the other collidable object the star collided with
     * done by checking coordinates of the star relative to the object it collided with
     *
     * Side translation:
     *  0 - bottom
     *  1 - top
     *  2 - right side
     *  3 - left side
     *
     *  Using 1/2 of the width, height was chosen somewhat arbitrarily
     *
     * Assumptions:
     *  1. The star collided with the other object
     *  2. The object can be well represented by a rectangle
     */
    private int whichSideForCollision(CollidableObj otherObj) {

        boolean collisionFound = false;
        int side = -1;
        int collidableX, collidableY, collidableWidth, collidableHeight;
        Rectangle starArea = null;
        Rectangle sideCollisionBox = null;

        starArea = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        collidableX = otherObj.getX();
        collidableY = otherObj.getY();
        collidableWidth = otherObj.getWidth();
        collidableHeight = otherObj.getHeight();

        // loop until a collision is found
        for (int i = 0; i < 4 && !collisionFound; i++) {

            // Check each side of the other collidable object for collisions until one it is found
            switch (i) {
                case 0:  sideCollisionBox = new Rectangle(collidableX, collidableY + collidableHeight - (collidableHeight / 2),
                            collidableWidth, collidableHeight / 2);
                         break;
                case 1:  sideCollisionBox = new Rectangle(collidableX, collidableY, collidableWidth, collidableHeight / 2);
                         break;
                case 2:  sideCollisionBox = new Rectangle(collidableX + collidableWidth - (collidableWidth / 2), collidableY,
                        collidableWidth / 2, collidableHeight);
                         break;
                case 3:  sideCollisionBox = new Rectangle(collidableX, collidableY, collidableWidth / 2, collidableHeight);
                         break;
            }

            if (starArea.intersects(sideCollisionBox)) {
                side = i;
                collisionFound = true;
            }
        }

        return side;
    }
}
