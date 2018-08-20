/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.Image;

abstract public class MoveableObj extends CollidableObj{

    protected int xSpeed, ySpeed;
    protected int prevX, prevY;

    MoveableObj(Image image, int xloc, int yloc, int xspd, int yspd) {

        super(image, xloc, yloc);

        this.xSpeed = xspd;
        this.ySpeed = yspd;
        this.prevX = xloc;
        this.prevY = yloc;
    }

    public void setXSpeed(int xspd){ this.xSpeed = xspd; }

    public int getXSpeed(){
        return this.xSpeed;
    }

    public void setYSpeed(int yspd){
        this.ySpeed = yspd;
    }

    public int getYSpeed(){
        return this.ySpeed;
    }

    public int getPrevY() { return prevY; }

    public int getPrevX() { return prevX; }

    abstract public void move();
}
