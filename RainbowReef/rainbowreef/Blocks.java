/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Blocks extends CollidableObj {

    private int health;
    private int score;
    private boolean dead;

    public Blocks(Image img, int xloc, int yloc, int health) {

        super(img, xloc ,yloc);

        this.health = health;
        this.score = health * 10;
        this.dead = false;
    }

    public boolean getDead() { return this.dead; }

    public int getScore() {return this.score; }

    public void collision(CollidableObj otherObj) {

        if (otherObj instanceof Star) {

            if (--this.health <= 0)
                destoryBlock();
        }
    }

    private void destoryBlock() {
        this.visible = false;
        this.collidable = false;
        this.dead = true;
    }
  
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {

        if (this.visible)
            g.drawImage(this.img, this.x, this.y,obs);
    }

}
