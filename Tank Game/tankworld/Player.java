/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.awt.event.KeyEvent;

/**
 *
 */
public class Player implements Observer{

    private ArrayList<Shell> unusedShells;
    private ArrayList<Shell> activeShells;
    private Tank myTank;
    private int score, id;
    private int left, right, up, down, shootKey;
    private boolean loser;

    /**
     *
     * @param aTank
     * @param shells
     * @param playerID
     * @param left
     * @param right
     * @param up
     * @param down
     * @param shootKey
     */
    Player(Tank aTank, ArrayList<Shell> shells, int playerID, int left, int right, int up, int down, int shootKey) {

        // Controls
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.shootKey = shootKey;

        // Tank and Shells
        this.unusedShells = shells;
        this.activeShells = new ArrayList<>();
        this.myTank = aTank;

        // Misc. Values
        this.score = 0;
        this.id = playerID;
        this.loser = false;
    }

    /**
     *
     * @return
     */
    public Tank getMyTank() { return this.myTank; }

    /**
     *
     * @return
     */
    public ArrayList<Shell> getActiveShells() { return this.activeShells; }

    /**
     *
     * @return
     */
    public int getScore() { return this.score; }

    /**
     *
     * @param walls
     * @param enemyTank
     */
    protected void tankCollisions(ArrayList<Wall> walls, Tank enemyTank) {

        // Check for collision with enemy tank
        if (myTank.detectCollision(enemyTank)) {
            myTank.collision(enemyTank);
            enemyTank.collision(myTank);
        }

        else {
            // Check for collision on walls if it didn't hit the enemy tank
            for (Wall aWall : walls) {

                // Assume the first wall it hits will resolve the collision
                if (myTank.detectCollision(aWall)) {
                    myTank.collision(aWall);
                    aWall.collision(myTank);
                    break;
                }
            } // end for
        }

    }

    /**
     * Check for collision on all shells shot by the player
     *
     * @param walls
     * @param enemyTank
     */
    protected void bulletCollisions(ArrayList<Wall> walls, Tank enemyTank) {
        // check collision on each shot shell
        for (Shell activeShell : activeShells) {

            // Check for collision against enemyTank
            if (activeShell.detectCollision(enemyTank)) {
                activeShell.collision(enemyTank);
                enemyTank.collision(activeShell);
            }

            else {
                // Check for collision on walls if it didn't hit the enemy tank
                for (Wall aWall : walls) {

                    // Assume the first wall it hits will resolve the collision with bullet
                    if (activeShell.detectCollision(aWall)) {
                        aWall.collision(activeShell);
                        activeShell.collision(aWall);
                        break;
                    }

                }
            }
        }
    }

    /**
     *
     * @param g
     * @param obs
     */
    public void updateItems(Graphics2D g, ImageObserver obs) {
        int numShells = activeShells.size();
        Shell curShell;

        for (int i = 0; i < numShells; i++) {
           curShell = activeShells.get(i);
           numShells--;
           curShell.move();
           curShell.draw(g, obs);

           if(curShell.getCollided()) {
               activeShells.remove(curShell);
               unusedShells.add(curShell);
           }
        }
        myTank.draw(g, obs);
        myTank.move();
    }

    /**
     *
     * @return
     */
    public boolean isLoser() {
        if (myTank.isDead() && !loser)
            loser = true;

        return loser;
    }

    /**
     *
     * @param obj
     * @param arg
     */
    @Override
    public void update(Observable obj, Object arg) {

        Shell shooter;

        GameEvents ge = (GameEvents) arg;
        if (ge.getEventType() == 1) {
            KeyEvent e = (KeyEvent) ge.getEvent();

            if (e.getKeyCode() == left) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    myTank.setMoveLeft(false);

                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    myTank.setMoveLeft(true);
                }
            }

            if (e.getKeyCode() == right) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    myTank.setMoveRight(false);

                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    myTank.setMoveRight(true);
                }
            }

            if (e.getKeyCode() == up) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    myTank.setMoveUp(false);

                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    myTank.setMoveUp(true);
                }
            }

            if (e.getKeyCode() == down) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    myTank.setMoveDown(false);

                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    myTank.setMoveDown(true);
                }

            }
            if (e.getKeyCode() == shootKey) {

                if(e.getID() == KeyEvent.KEY_PRESSED){

                    if (!unusedShells.isEmpty()) {
                        shooter = unusedShells.remove(0);
                        myTank.fire(shooter);
                        activeShells.add(shooter);
                    }
                }
            }
        }
    }
}
