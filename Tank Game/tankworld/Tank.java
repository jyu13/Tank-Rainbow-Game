/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;

public class Tank extends MoveableObj{

    private int prevX, prevY;
    private int health = 4;
    private boolean moveLeft, moveRight, moveUp, moveDown;
    //check if the tank is dead.
    private boolean dead;

    public Tank(Image img, int x, int y, int speed, int angle) {
        super(img, x, y, speed, angle);

        // Previous tank position, used during collisions
        this.prevX = x;
        this.prevY = y;

        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;

        dead = false;
        collidable = true;
    }

    protected void setMoveLeft(boolean value) { this.moveLeft = value; }

    protected void setMoveRight(boolean value) { this.moveRight = value; }

    protected void setMoveUp(boolean value) { this.moveUp = value; }

    protected void setMoveDown(boolean value) { this.moveDown = value; }

    /**
     * set life for toolbox??
     *
     * @param heal
     */
    public void repair(int heal) {
        health += heal;
    }

    /**
     * set dead to true if the health is not positive
     */
    private void checkDead() {

        if (health <= 0)
            this.dead = true;
            // Draw a big explosion
    }
    
    public boolean isDead(){
        checkDead();
        return this.dead;
    }

    /**
     * set tank to different shoot mode
     *
     */
    protected void fire(Shell tankShell) {
        if (!dead)
            tankShell.spawnShell(this.getXCenter()-2, this.getYCenter(), this.angle);
    }
    
    @Override
    public void move() {
        if (!dead) {
            if (moveLeft) {
                if (this.angle >= 0 && this.angle <= 360) {
                    this.angle -= 6;
                } else {
                    this.angle = 360;
                }
            }
            if (moveRight) {
                if (this.angle <= 360 && this.angle >= 0) {
                    this.angle += 6;
                } else {
                    this.angle = 0;
                }
            }
            if (moveUp) {

                // Record current location, in case there is a collision
                this.prevX = this.x;
                this.prevY = this.y;

                this.x += speed * Math.sin(Math.toRadians(this.angle));
                this.y -= speed * Math.cos(Math.toRadians(this.angle));
            }
            if (moveDown) {

                // Record current location, in case there is a collision
                this.prevX = this.x;
                this.prevY = this.y;


                this.x -= speed * Math.sin(Math.toRadians(this.angle));
                this.y += speed * Math.cos(Math.toRadians(this.angle));
            }
        }
    }

    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        AffineTransform old = g.getTransform();
        g.rotate(Math.toRadians(this.angle), this.x + (this.width)/2 , this.y + (this.height)/2);
        g.drawImage(this.img,this.x,this.y,obs);
        g.setTransform(old);
        
    }

    public void collision(CollidableObj otherObj){

        Shell enemyShell;

        if (!this.dead) {

            if (otherObj instanceof Shell) {
                enemyShell = (Shell) otherObj;
                this.health -= enemyShell.getDamage();
                checkDead();
            }
            else if (otherObj instanceof Wall || otherObj instanceof Tank) {
                this.x = this.prevX;
                this.y = this.prevY;
            }
        }
        
    }
}
