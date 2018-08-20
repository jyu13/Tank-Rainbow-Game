/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.Image;

abstract public class GameObject {
    protected int x, y, width, height; //angle for tank, bullet , bomb, rocket
    protected Image img;
    
    public GameObject(Image img, int x, int y){
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }

    public int getXCenter(){
        return this.x + this.width / 4;
    }

    public int getYCenter(){
        return this.y + this.height / 4;
    }

    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }

    
    abstract public void draw(Graphics2D g, ImageObserver obs);
}
