/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Graphics2D;

/**
 *  Wall Class
 *
 *  Responsible for drawing, and maintaining the walls on the map.
 */
public class Wall extends CollidableObj{

    private int health;
    private boolean destructible;

    /**
     * Constructor for walls. Sets the wall to be visible and collidable by default. Destructibility of the
     * wall is dependent on user input.
     *
     * @param  image  Image representing the Wall object
     * @param  xloc   x-location of the center of the wall
     * @param  yloc   y-location of the center of the wall
     * @param  destructible  used to determine if the wall can be destroyed by collision
     */
    Wall(Image image, int xloc, int yloc, boolean destructible) {

        super(image, xloc, yloc);
        this.visible = true;
        this.collidable = true;
        this.health = 1;
        this.destructible = destructible;
    }

    /**
     * Handles the result of the collision with the wall. If the wall is indestructible, then
     * collision with the wall will not have any effect on the wall.
     *
     * @param  otherObj  another collidable object
     */
    public void collision(CollidableObj otherObj) {

        if (this.destructible)
        {
            if (otherObj instanceof Shell) {
                destroyWall();
            }
        }
    }

    /**
     * Removes the wall from the game if the wall is destructible. Sets the wall so that it won't collide with other
     * objects, and won't be drawn on the field.
     *
     * Used in conjunction with the collision method.
     */
    private void destroyWall() {

        if (this.destructible) {
            this.visible = false;
            this.collidable = false;
        }
    }

    /**
     * Used to respawn a wall after it has been destroyed. Sets the wall to be drawn again, and allows it to have
     * collisions with other collidable objects.
     */
    public void spawnWall() {

        if (!this.visible) {
            this.visible = true;
            this.collidable = true;
            this.health = 1;
        }

    }

    /**
     * Draws the wall on the field asynchronously. Overrides the draw method from GameObject, one of the wall
     * class's parent classes.
     *
     * @param  g  the graphics object to draw on
     * @param obs used to asynchronously update the wall on the field
     */
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {

        if (this.visible)
            g.drawImage(img, x, y, obs);
    }
}
