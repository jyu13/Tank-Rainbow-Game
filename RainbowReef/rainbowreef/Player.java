/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Observer;
import java.util.Observable;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player implements Observer {

    private final int STARTING_LIVES = 3;
    private final int MAX_LIVES = 5;
    private final int STAR_START_X = 200;
    private final int STAR_START_Y = 340;
    private final int SLIDER_START_X = 200;
    private final int SLIDER_START_Y = 440;
    private final int STAR_START_XSPEED = 0;
    private final int STAR_START_YSPEED = 4;

    private Star myStar;
    private Slider mySlider;
    private int lives, score;
    private boolean loser;
    private int left, right;
    private Image sliderImg, starImg, lifeImg;
    
    Player(int left, int right) {
        
        //control
        this.left = left;
        this.right = right;
        
        try{
            sliderImg = ImageIO.read(Player.class.getResource("../Resources/Katch.gif"));
            starImg= ImageIO.read(Player.class.getResource("../Resources/Pop.gif"));
            lifeImg = ImageIO.read(Player.class.getResource("../Resources/Katch_small.gif"));
        
        }catch (IOException e) {
            System.out.println("Player Resources allocates failed.");
        }

        //Misc. Values
        this.lives = STARTING_LIVES;
        this.loser = false;
        this.score = 0;

        mySlider = new Slider(sliderImg, SLIDER_START_X, SLIDER_START_Y, 4);
        myStar = new Star(starImg, STAR_START_X, STAR_START_Y, STAR_START_XSPEED, STAR_START_YSPEED);
    }
    
    public Slider getMySlider() { return this.mySlider;}
    
    public Star getMyStar() { return this.myStar; }
    
    public int getScore() { return this.score; }

    public int getLife() { return this.lives; }

    public boolean getGameOver() {

        if (lives < 1)
            loser = true;

        return loser;
    }

    public void resetScore() { this.score = 0; }

    public int incScore(int sc) { return this.score += sc;}

    public void incLife() { lives++; }
    
    public void decLife() { lives--; }

    public void drawItems(Graphics2D g2, ImageObserver obs) {
        myStar.draw(g2, obs);
        mySlider.draw(g2, obs);
    }

    public void moveItems() {
        mySlider.move();
        myStar.move();

        if (myStar.getY() > 480) {
            myStar.resetStar();
            decLife();
        }
    }

    @Override
    public void update(Observable obj, Object arg){

        GameEvents ge = (GameEvents) arg;

        if (ge.type == 1) {
            KeyEvent e = (KeyEvent)ge.event;

            if (e.getKeyCode() == left) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    mySlider.setMoveLeft(false);
                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    mySlider.setMoveLeft(true);
                }
            }

            if (e.getKeyCode() == right) {

                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    mySlider.setMoveRight(false);
                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    mySlider.setMoveRight(true);
                }
            }
        }
    }
}
