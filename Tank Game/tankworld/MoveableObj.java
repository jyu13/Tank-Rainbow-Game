/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.Image;

/**
 *
 */
abstract public class MoveableObj extends CollidableObj{

    protected int speed;
    protected int angle;

    /**
     *
     * @param image
     * @param xloc
     * @param yloc
     * @param spd
     * @param dir
     */
    MoveableObj(Image image, int xloc, int yloc, int spd, int dir) {
        super(image, xloc, yloc);

        this.speed = spd;
        this.angle = dir;
    }

    /**
     *
     * @param spd
     */
    public void setSpeed(int spd){
        this.speed = spd;
    }

    /**
     *
     * @return
     */
    public int getSpeed(){
        return this.speed;
    }

    /**
     *
     * @param dir
     */
    public void setDirection(int dir) {
        this.angle = dir;
    }

    /**
     *
     * @return
     */
    public double getDirection () { 
        return this.angle; 
    }

    /**
     *
     */
    abstract public void move();
}
