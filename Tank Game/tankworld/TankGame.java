/*
 *
 * @author Arnold & Ian
 */

package tankworld;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.KeyEvent;

public class TankGame extends JApplet implements Runnable {

    final static private int MAX_SHELLS_PER_PLAYER = 20;
    final private static int REAL_W = 1920, REAL_H = 1440; //real world size
    final private static int WINDOW_W = 960, WINDOW_H = 720; //window size

    private Thread thread;
    private BufferedImage bimg1, bimg2;
    private Dimension windowSize;
    private Graphics2D g2;
    private int move = 0;
    private int subX1, subY1, subX2, subY2; //split window positions
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private HashMap<String, Image> images;
    private HashMap<String, SoundPlayer> sounds;
    private Player player1, player2;

    private void drawBackGroundWithTileImage() {
        int TileWidth = images.get("background").getWidth(this);
        int TileHeight = images.get("background").getHeight(this);

        int NumberX = (int) (REAL_W / TileWidth);
        int NumberY = (int) (REAL_H / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(images.get("background"), j * TileWidth,
                        i * TileHeight + (move % TileHeight), TileWidth,
                        TileHeight, this);
            }
        }

    }
    
    private void mapSetUp(){
        int xNumber = 64;
        int yNumber = 45;
        
        try{
            //walls around real world
            for(int i = 0 ; i < xNumber; i++ ){
                walls.add(new Wall(images.get("wallImg1"),i*32,0,false));
                walls.add(new Wall(images.get("wallImg1"),i*32,1408,false));
            }
            for(int i = 1 ; i < yNumber; i++ ){
                walls.add(new Wall(images.get("wallImg1"), 0,i*32,false));
                walls.add(new Wall(images.get("wallImg1"), 1888, i*32,false));
            }
            
            //walls at center
            for(int i = 1; i < 10; i ++){
                walls.add(new Wall(images.get("wallImg2"), 980,544 + i*32, true));
            }
            for(int i = 1; i < 7; i ++ ){
                walls.add(new Wall(images.get("wallImg2"),756 + i*32 ,704,true));
                walls.add(new Wall(images.get("wallImg2"),980 + i*32 ,704,true));
            }
            //walls around
            for(int i = 1; i < 10; i ++){
                walls.add(new Wall(images.get("wallImg1"), 1408, 1408 - i*32, false ));
                walls.add(new Wall(images.get("wallImg1"), 480, 0 + i*32 ,false));
            }
            for(int i = 1; i < 15; i++){
                walls.add(new Wall(images.get("wallImg1"), 0 + i*32 ,960,false));
                walls.add(new Wall(images.get("wallImg1"),1888 - i*32 , 480,false));
            }
            
            //walls around tank1
            for(int i = 1; i < 4; i++){
                walls.add(new Wall(images.get("wallImg1"),168 + i*32, 168,false));
                walls.add(new Wall(images.get("wallImg1"),168 + i*32, 264,false ));
                walls.add(new Wall(images.get("wallImg1"),264, 168+ (i-1)*32, false));
            }
            //walls around tank2
            for(int i = 1; i < 4; i++){
                walls.add(new Wall(images.get("wallImg1"),1588 + i*32, 1148,false));
                walls.add(new Wall(images.get("wallImg1"),1588 + i*32, 1244,false ));
                walls.add(new Wall(images.get("wallImg1"),1620, 1148 + (i-1)*32, false));
            }

            
            //walls for power up
            for(int i = 1; i < 5; i++){
                walls.add(new Wall(images.get("wallImg2"), 1588 + i*32 ,168,true));
                walls.add(new Wall(images.get("wallImg2"), 1588 + i*32 , 264,true));
                walls.add(new Wall(images.get("wallImg2"), 168 + i*32 , 1148,true));
                walls.add(new Wall(images.get("wallImg2"), 168 + i*32 , 1244,true));
            }
            for(int i =1; i< 3; i++){
                walls.add(new Wall(images.get("wallImg2"), 1620 ,168 + i*32,true));
                walls.add(new Wall(images.get("wallImg2"), 1716 , 168 + i*32,true));
                walls.add(new Wall(images.get("wallImg2"), 200 ,1148 + i*32,true));
                walls.add(new Wall(images.get("wallImg2"), 296 , 1148 + i*32,true));
            }
            
            //walls random
            for(int i = 1; i <10; i++){
                for(int j = i; j< 5; j ++){
                    walls.add(new Wall(images.get("wallImg2"), 400+j*32, 1000+i*32, true));
                    walls.add(new Wall(images.get("wallImg2"), 1500 - j*32, 400 - i*32, true));
                    walls.add(new Wall(images.get("wallImg2"), 1000+ j*64, 1200 - i* 64, true));
                    walls.add(new Wall(images.get("wallImg2"), 900 - j*64, 300 + i* 64, true));
                }
            }
        }catch(Exception e){
            System.out.println("Adding walls fail");
        }
    }

    private void drawDemo() {
        Font stringFont;
        BufferedImage leftScreen, rightScreen;
        Image miniMap;


        if (!player1.isLoser() && !player2.isLoser()) {
            drawBackGroundWithTileImage();


            for (int i = 0; i < walls.size(); i++) {
                walls.get(i).draw(g2, this);
            }

            player1.updateItems(g2, this);
            player2.updateItems(g2, this);

            checkForCollisions(player1, player2);
            checkBound();

            bimg2 = (BufferedImage) createImage(WINDOW_W, WINDOW_H);
            g2 = bimg2.createGraphics();

            leftScreen = bimg1.getSubimage(subX1, subY1, WINDOW_W / 2, WINDOW_H);
            rightScreen = bimg1.getSubimage(subX2, subY2, WINDOW_W / 2, WINDOW_H);
            miniMap = bimg1.getScaledInstance(320, 240, Image.SCALE_FAST);

            g2.drawImage(leftScreen, 0, 0, this);
            g2.drawImage(rightScreen, WINDOW_W / 2, 0, this);
            g2.drawImage(bimg1, WINDOW_W / 2 - 160, 480, 320, 240, this);
        }

        else {

            stringFont = new Font( "SansSerif", Font.PLAIN, 40 );
            g2.setFont( stringFont );
            drawBackGroundWithTileImage();
            if (player1.isLoser())
                g2.drawString("*** PLAYER 1 ***", REAL_H / 2, REAL_W / 2);
            else
                g2.drawString("*** PLAYER 2 ***", REAL_H / 2, REAL_W / 2);

            g2.drawString("***** YOU WIN *****", (REAL_H / 2 - 80), REAL_W / 2);
            sounds.get("audio").stop();
            sounds.get("bigExplosion").play();
        }
    }

    private void checkForCollisions(Player p1, Player p2) {

        // Check for bullet collisions
        p1.bulletCollisions(walls, p2.getMyTank());
        p2.bulletCollisions(walls, p1.getMyTank());

        // Check for tank collisions
        p1.tankCollisions(walls, p2.getMyTank());
        p2.tankCollisions(walls, p1.getMyTank());
    }

    private void checkBound(){
        subX1 = player1.getMyTank().getXCenter() - WINDOW_W / 4;
        subY1 = player1.getMyTank().getY() - WINDOW_H / 3;
        subX2 = player2.getMyTank().getXCenter() - WINDOW_W / 4;
        subY2 = player2.getMyTank().getY() - WINDOW_H / 3;

        if (subX1 < 0) {
            subX1 = 0;
        } else if (subX1 >= REAL_W - (WINDOW_W / 2)) {
            subX1 = REAL_W - (WINDOW_W / 2);
        }

        if (subY1 < 0) {
            subY1 = 0;
        } else if (subY1 >= (REAL_H - WINDOW_H)) {
            subY1 = REAL_H - WINDOW_H;
        }

        if (subX2 < 0) {
            subX2 = 0;
        } else if (subX2 >= REAL_W - (WINDOW_W / 2)) {
            subX2 = REAL_W - (WINDOW_W / 2);
        }

        if (subY2 < 0) {
            subY2 = 0;
        } else if (subY2 >= (REAL_H - WINDOW_H)) {
            subY2 = REAL_H - WINDOW_H;
        }
    }

    @Override
    public void init() {
        Tank tank1, tank2;
        ArrayList<Shell> p1ammo, p2ammo;
        GameEvents gameEvent1, gameEvent2;

        setFocusable(true);
        setBackground(Color.white);

        this.images = new HashMap<>();
        this.sounds = new HashMap<>();

        try {
            images.put("background", ImageIO.read(TankGame.class.getResource("../Resources/Background.bmp")));
            images.put("p1Tank", ImageIO.read(TankGame.class.getResource("../Resources/Tank1.png")));
            images.put("p2Tank", ImageIO.read(TankGame.class.getResource("../Resources/Tank2.png")));
            images.put("shell",  ImageIO.read(TankGame.class.getResource("../Resources/Shell.gif")));
            images.put("wallImg1", ImageIO.read(TankGame.class.getResource("../Resources/Wall1.gif")));
            images.put("wallImg2", ImageIO.read(TankGame.class.getResource("../Resources/Wall2.gif")));

            sounds.put("bigExplosion", new SoundPlayer(2, "../Resources/Explosion_large.wav"));
            sounds.put("smallExplosion", new SoundPlayer(2, "../Resources/Explosion_small.wav"));
            sounds.put("audio", new SoundPlayer(1, "../Resources/Music.wav"));
        } catch (IOException e) {
            System.out.println("Resources allocates failed.");
        }

        p1ammo = new ArrayList<>();
        p2ammo = new ArrayList<>();

        for (int i = 0; i < MAX_SHELLS_PER_PLAYER; i++)
        {
            p1ammo.add(new Shell(images.get("shell"), 0, 0, 0, 0, REAL_W, REAL_H));
            p2ammo.add(new Shell(images.get("shell"), 0, 0, 0, 0, REAL_W, REAL_H));
        }

        tank1 = new Tank(images.get("p1Tank"), 300, 360, 5, 90);
        tank2 = new Tank(images.get("p2Tank"), 1400, 900, 5, 270);

        player1 = new Player(tank1, p1ammo, 1, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_Q);
        player2 = new Player(tank2, p2ammo, 2, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_O);

        gameEvent1 = new GameEvents();
        gameEvent2 = new GameEvents();
        gameEvent1.addObserver(player1);
        gameEvent2.addObserver(player2);
        KeyControl key1 = new KeyControl(gameEvent1);
        KeyControl key2 = new KeyControl(gameEvent2);
        addKeyListener(key1);
        addKeyListener(key2);
        mapSetUp();
        sounds.get("audio").play();
    }

    @Override
    public void paint(Graphics g) {

        windowSize = getSize();
        bimg1 = (BufferedImage) createImage(REAL_W, REAL_H);
        g2 = bimg1.createGraphics();

        drawDemo();
        g.drawImage(bimg2, 0, 0, this);
    }

    @Override
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    @Override
    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                thread.sleep(25);
            } catch (InterruptedException e) {
                System.out.println("TankGame run method");
                break;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final TankGame demo = new TankGame();
        demo.init();
        JFrame f = new JFrame("TankGame");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(WINDOW_W, WINDOW_H+22));
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.start();
    }

}
