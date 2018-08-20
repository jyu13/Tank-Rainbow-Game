/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;
import java.lang.Math;
import java.util.Observable;

/**
 *  Class for Shell objects
 *  These are shot and collide with other collidable objects, dealing damage.
 *
 *  Has the following class hierarchy:
 *      (top) GameObject > CollidableObj > MoveaboleObj > Shell (bottom)
 *
 *  Inherits the following:
 *
 *      From GameObject:
 *
 *          Attributes
 *          ----------
 *          # int x : x position
 *          # int y : y position
 *          # int width : width of image
 *          # int height : height of image
 *          # Image img : image used to represent the object in game
 *          # boolean visible : used to determine whether to draw the object
 *
 *          Methods
 *          -------
 *          + int getX() : get an object's x position
 *          + int getY() : get an object's y position
 *          + int getWidth() : get an object's image width
 *          + int getHeight() : get an shell's image height
 *          + void setX(int x) :
 *          + void setY(int y) :
 *          + void setVisible() :
 *          + void draw(Graphics2D g, ImageObserver obs) :
 *
 *      From CollidableObj:
 *
 *          Attributes
 *          ----------
 *          # boolean collided :
 *          # boolean collidable :
 *
 *          Methods
 *          -------
 *          + boolean detectCollision() :
 *          + void collision() {abstract} :
 *
 *      From MoveableObj:
 *
 *          Attributes
 *          ----------
 *          # int speed :
 *          # double angle :
 *
 *          Methods
 *          -------
 *          + void setSpeed(int spd) :
 *          + void setAngle(int dir) :
 *          + void move() {abstract} :
 */
public class Shell extends MoveableObj {

    // Class Variables
    final protected int xMax, yMax;
    private int damageVal;
    static private final int BASE_SHELL_SPEED = 8;
    static private final int BASE_DAMAGE = 1;
    private boolean visible, collided;

    /**
     * Constructor for Shell. Sets the shell to not be drawn and not collidable, as well as sets maximums for how
     * far the shell can move on the screen (these values are meant to keep the shell from traveling forever if
     * they never collide with anything).
     *
     * @param  image  the visual representation of the shell
     * @param  xloc   initial x-coordinate of the center of the shell
     * @param  yloc   initial y-coordinate of the center of the shell
     * @param  spd    speed of the shell
     * @param  angle  initial angle the shell is traveling in. 0 degrees is traveling only in the +y direction.
     */
    Shell(Image image, int xloc, int yloc, int spd, int angle, int xMax, int yMax) {

        // TODO: create a different MoveableObj constructor with no speed
        super(image, xloc, yloc, spd, angle);
        this.xMax = xMax;
        this.yMax = yMax;
        this.damageVal = BASE_DAMAGE;
        this.visible = false;
        this.collidable = false;
        this.collided = false;
    }

    /**
     * Returns the amount of damage this shell does.
     *
     * @return  the amount of damage this shell inflicts
     */
    public int getDamage() { return this.damageVal; }

    /**
     * Returns if this shell has collided with another object.
     *
     * @return  true if the shell collided with another collidable object, false otherwise
     */
    public boolean getCollided() { return this.collided; }

    /**
     * Creates a shell on the field. Should be used when a shell needs to be shot.
     *
     * @param xloc  the x-coordinate to spawn the shell at
     * @param yloc  the y-coordinate to spawn the shell at
     * @param dir   the angle the shell will travel in
     */
    public void spawnShell(int xloc, int yloc, int dir) {

        // Set state for the shell
        this.visible = true;
        this.collidable = true;
        this.collided = false;

        this.x = xloc;
        this.y = yloc;
        this.speed = BASE_SHELL_SPEED;
        this.angle = dir;
    }

    /**
     * Determines the next position of the shell. This is dependent on the shell's current angle and speed.
     * Also checks if the shell has gone past it's maximum limit on the screen, and destroys it if it has.
     * Overrides the move method from MoveableObj.
     *
     */
    @Override
    public void move() {

        // Assumes that non-visible shells should not be moving
        if (this.visible)
        {
            // Update shell position
            this.x += (int)(this.speed * Math.sin(Math.toRadians(this.angle)));
            this.y -= (int)(this.speed * Math.cos(Math.toRadians(this.angle)));

            // Check if it's still in the window
            if (this.x < 0 || (this.x > this.xMax) || this.y < 0 || (this.y > this.yMax))
                destroyShell();
        }
    }

    /**
     * Draws the shell on the field asynchronously.
     * Overrides the draw method from GameObject.
     *
     * @param  g  the graphics object to draw on
     * @param obs used to asynchronously update the wall on the field
     */
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
        if(visible) {
            AffineTransform old = g.getTransform();
            g.rotate(Math.toRadians(this.angle), this.x + (this.width) / 2, this.y + (this.height) / 2);
            g.drawImage(this.img, this.x, this.y, obs);
            g.setTransform(old);

        }
    }

    /**
     * Handles the behavior of the shell when it collides with another object. Shell do not have much durability,
     * so they should be destroyed when they collide with anything.
     * Overrides the collision method from CollidableObj.
     *
     */
    @Override
    public void collision(CollidableObj otherObj) {

        // Draw a small explosion
        destroyShell();
    }

    /**
     * Removes a shell from the field. Meant to be used in conjunction with a shell colliding.
     */
    private void destroyShell() {

        // Reset back to initial state
        this.visible = false;
        this.collidable = false;
        this.collided = true;

        this.x = 0;
        this.y = 0;
        this.speed = 0;
        this.angle = 0;
    }

}