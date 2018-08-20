/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainbowreef;

/**
 *
 * @author Arnold & Ian
 */

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;

public class RainbowReefLevel {

    private int numEnemies;
    private int mapHeight, mapWidth;

    private ArrayList<Wall> walls;
    private ArrayList<Blocks> blocks;
    private ArrayList<Enemy> enemies;
    private CollidableObj[][] collisionMap;

    RainbowReefLevel(int height, int width,  HashMap<String, Image> resources) {

        walls = new ArrayList<>();
        blocks = new ArrayList<>();
        enemies = new ArrayList<>();

        // Note: The space here could be optimized by using a sparse data structure
        collisionMap = new CollidableObj[width][height];

        numEnemies = 0;
        mapHeight = height;
        mapWidth = width;

        makeLevel(resources);
    }

    public boolean getLevelComplete() { return numEnemies > 0; }

    public void drawLevel(Graphics2D g2, ImageObserver obs) {

        int numWalls = walls.size();
        int numBlocks = blocks.size();

        for (int i = 0; i < numWalls; i++) {
            walls.get(i).draw(g2, obs);
        }

        for (int i = 0; i < numBlocks; i++) {
            blocks.get(i).draw(g2, obs);
        }

        for(int i = 0; i < this.numEnemies; i++) {
            enemies.get(i).draw(g2, obs);
        }
    }

    public void updateMap(Player player) {

        int starX, starY;
        Star playerStar = player.getMyStar();
        Slider playerSlider = player.getMySlider();

        starX = playerStar.getX();
        starY = playerStar.getY();

        // Update slider and star in collision map
        // TODO: Error check Star locations before assignment
        collisionMap[playerStar.getPrevX()][playerStar.getPrevY()] = null;
        collisionMap[playerSlider.getPrevX()][playerSlider.getPrevY()] = null;

        if (starX > 0 && starX < mapWidth && starY > 0 && starY < mapHeight)
            collisionMap[playerStar.getX()][playerStar.getY()] = playerStar;

        collisionMap[playerSlider.getX()][playerSlider.getY()] = playerSlider;

        // Remove destroyed objects
        for (Blocks block : blocks) {

            if (block.getDead()) {
                blocks.remove(block);
                collisionMap[block.getX()][block.getY()] = null;
                player.incScore(block.getScore());
            }
        }

        for (Enemy squid : enemies) {

            if (squid.getDead()) {
                enemies.remove(squid);
                collisionMap[squid.getX()][squid.getY()] = null;
                numEnemies--;
                player.incScore(squid.getScore());
            }
        }
    }

    /*
     * Goal of this alg. is to detect collisions between moveable objs and collidable ones:
     *     Assumes that non-moving objects will not collide with other non-moving objects
     *
     *     Collision detection broken into two phases
     *     1. Based on direction of the moving object, create a subset of collidable objs that might be collided with
     *        based on coordinates in the collision map
     *     2. From the subset of collidable objects, check if any of the moveable objects actually collided with something
     *     3. Handle collisions
     */
    public void collisionDetector(Player player) {

        Star playerStar = player.getMyStar();
        Slider playerSlider = player.getMySlider();

        this.detectCollisions(playerStar);
        this.detectCollisions(playerSlider);
    }

    private void detectCollisions(MoveableObj collider) {
        int xMax, xMin, yMax, yMin;
        int offset = blocks.get(0).getWidth() / 2; // Used to extend search range for collidables

        ArrayList<CollidableObj> potentialCollisions = new ArrayList<>();

        // Calculate bounds for checking for collidable objects
        xMax = collider.getX() + collider.getWidth() + offset;
        xMax = (xMax > this.mapWidth) ? mapWidth : xMax;

        xMin = collider.getX() - offset;
        xMin = (xMin < 0) ? 0 : xMin;

        yMax = collider.getY() + collider.getHeight() + offset;
        yMax = (yMax > this.mapHeight) ? mapHeight : yMax;

        yMin = collider.getY() - offset;
        yMin = (yMin < 0) ? 0 : yMin;

        // Gather possible collidable objects
        for (int j = yMin; j < yMax; j++) {

            for (int i = xMin; i < xMax; i++) {
                if (collisionMap[i][j] != null && !(collider.getClass().equals(collisionMap[i][j].getClass()))) {
                    potentialCollisions.add(collisionMap[i][j]);
                }
            }
        }

        for (CollidableObj collidable : potentialCollisions) {
            if (collider.detectCollision(collidable)) {
                collider.collision(collidable);
                collidable.collision(collider);
            }
        }
    }

    private void makeLevel(HashMap<String, Image> resources) {

        Wall wall;
        Blocks block;
        Enemy squid;

        for (int i = 20; i < 640; i += 20) {

            wall = new Wall(resources.get("wallImg"), i, 0);
            walls.add(wall);
            collisionMap[i][0] = wall;
        }
        for (int i = 0; i < 460; i += 20) {

            wall = new Wall(resources.get("wallImg"), 0, i);
            walls.add(wall);
            collisionMap[0][i] = wall;

            wall = new Wall(resources.get("wallImg"), 620, i);
            walls.add(wall);
            collisionMap[620][i] = wall;
        }

        for ( int i = 20; i < 300; i+= 20) {

            block = new Blocks(resources.get("block1"), 20, i, 1);
            blocks.add(block);
            collisionMap[20][i] = block;

            block = new Blocks(resources.get("block2"), 60, i, 2);
            blocks.add(block);
            collisionMap[60][i] = block;

            wall = new Wall(resources.get("wallImg"), 100, i);
            walls.add(wall);
            collisionMap[100][i] = wall;

            block = new Blocks(resources.get("block3"), 120, i, 3);
            blocks.add(block);
            collisionMap[120][i] = block;

            block = new Blocks(resources.get("block4"), 160, i, 4);
            blocks.add(block);
            collisionMap[160][i] = block;

            block = new Blocks(resources.get("block1"), 580, i, 1);
            blocks.add(block);
            collisionMap[580][i] = block;

            block = new Blocks(resources.get("block2"), 540, i, 2);
            blocks.add(block);
            collisionMap[540][i] = block;

            wall = new Wall(resources.get("wallImg"), 520, i);
            walls.add(wall);
            collisionMap[520][i] = block;

            block = new Blocks(resources.get("block3"), 480, i, 3);
            blocks.add(block);
            collisionMap[480][i] = block;

            block = new Blocks(resources.get("block4"), 440, i, 4);
            blocks.add(block);
            collisionMap[440][i] = block;
        }

        for (int i = 200; i < 440; i +=40) {

            block = new Blocks(resources.get("blockSplit"), i, 20, 1);
            blocks.add(block);
            collisionMap[i][20] = block;

            block = new Blocks(resources.get("block5"), i, 40, 5);
            blocks.add(block);
            collisionMap[i][40] = block;

            block = new Blocks(resources.get("block6"), i, 60, 6);
            blocks.add(block);
            collisionMap[i][60] = block;
        }

        squid = new Enemy(resources.get("enemyBig"),280,80);
        enemies.add(squid);
        collisionMap[280][80] = squid;
        numEnemies++;

        for (int i = 200; i < 440; i += 20) {

            wall = new Wall(resources.get("wallImg"), i, 160);
            walls.add(wall);
            collisionMap[i][160] = wall;
        }

        for (int i = 200; i < 440; i += 40) {

            block = new Blocks(resources.get("block7"), i , 180, 7);
            blocks.add(block);
            collisionMap[i][180] = block;

            block = new Blocks(resources.get("block7"), i,200, 7);
            blocks.add(block);
            collisionMap[i][200] = block;
        }

    } // end makeLevel

} // end RainbowReefLevel
