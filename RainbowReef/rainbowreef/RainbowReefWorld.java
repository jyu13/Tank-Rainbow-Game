/*
 *
 * @author Arnold & Ian
 */
package rainbowreef;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class RainbowReefWorld extends JApplet implements Runnable {

    private static final int WINDOW_W = 640, WINDOW_H = 480; //window size

    private Thread thread;
    private HashMap<String, Image> images;
    private HashMap<String, SoundPlayer> sounds;
    private Graphics2D g2;
    private Dimension windowSize;
    private int move = 0;
    private BufferedImage bimg1;
    private Player player1;
    private boolean status, getHelp;
    // private RainbowReefLevel[] levels;
    private RainbowReefLevel levels;

    /**
     * draw background image
     */
    private void drawBackGroundWithTileImage() {
        int TileWidth = images.get("background2").getWidth(this);
        int TileHeight = images.get("background2").getHeight(this);

        int NumberX = (int) (WINDOW_W / TileWidth);
        int NumberY = (int) (WINDOW_H / TileHeight);

        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(images.get("background2"), j * TileWidth, i * TileHeight + (move % TileHeight),
                        TileWidth, TileHeight, this);
            }
        }
    }

    /**
     * 3 stages if it is at first stage, click the start button to start if
     * player dies, then display score else in regular game mode
     */
    public void drawDemo() {

        Font stringFont;

        if (!status && !getHelp) {
            drawBackGroundWithTileImage();
            stringFont = new Font("SansSerif", Font.PLAIN, 60);
            g2.setFont(stringFont);
            g2.setColor(Color.blue);
            g2.drawString("Rainbow Reef", 130, 150);
            g2.drawImage(images.get("startImg"), 120, 350, this);
            g2.drawImage(images.get("quitImg"), 250, 350, this);
            g2.drawImage(images.get("helpImg"), 380, 350, this);
        }
      
        else if (!status && getHelp) {
            drawBackGroundWithTileImage();
            stringFont = new Font("SansSerif", Font.PLAIN, 60);
            g2.setFont(stringFont);
            g2.setColor(Color.blue);
            g2.drawString("Rainbow Reef", 130, 150);
            stringFont = new Font("SansSerif", Font.PLAIN, 20);
            g2.setFont(stringFont);
            g2.setColor(Color.black);
            g2.drawString("Press Start to start the Game", 200, 200);
            g2.drawString("Press quit to exit the game", 200, 240);
            g2.drawImage(images.get("startImg"), 120, 350, this);
            g2.drawImage(images.get("quitImg"), 250, 350, this);
            g2.drawImage(images.get("helpImg"), 380, 350, this);
        }

        if (player1.getGameOver()) {
          
            drawBackGroundWithTileImage();
            stringFont = new Font("SansSerif", Font.PLAIN, 50);
            g2.setFont(stringFont);
            g2.drawString("Player", 230, 100);
            g2.drawString("Score :" + player1.getScore(), 200, 300);
            g2.setColor(Color.red);
            g2.drawString("You Lose", 190, 400);
            sounds.get("audio").stop();
            sounds.get("lost").play();
        } else {

            drawBackGroundWithTileImage();
            stringFont = new Font("SansSerif", Font.PLAIN, 20);
            g2.setFont(stringFont);
            g2.drawString("Score :" + player1.getScore(), 20,420);
            g2.drawString("Lives :" + player1.getLife(), 20, 440);

            // draw player objects
            player1.drawItems(g2, this);
            levels.drawLevel(g2, this);

            player1.moveItems();

            levels.collisionDetector(player1);
            levels.updateMap(player1);
        }
    }

    /**
     * initialize the game
     */
    @Override
    public void init() {

        GameEvents gameEvent;

        setFocusable(true);
        setBackground(Color.white);

        images = new HashMap<>();
        sounds = new HashMap<>();
        status = false;
        getHelp = false;

        try {
            images.put("background1", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Background1.bmp")));
            images.put("background2", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Background1.bmp")));
            images.put("life", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Katch_small.gif")));
            images.put("wallImg", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Wall.gif")));
            images.put("startImg", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Button_start.gif")));
            images.put("helpImg",  ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Button_help.gif")));
            images.put("quitImg", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Button_quit.gif")));
            images.put("block1", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block1.gif")));
            images.put("block2", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block2.gif")));
            images.put("block3", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block3.gif")));
            images.put("block4", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block4.gif")));
            images.put("block5", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block5.gif")));
            images.put("block6", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block6.gif")));
            images.put("block7", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block7.gif")));
            images.put("blockDouble", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block_double.gif")));
            images.put("blockLife", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block_life.gif")));
            images.put("blockSolid", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block_solid.gif")));
            images.put("blockSplit", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Block_split.gif")));
            images.put("enemyBig",  ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Bigleg.gif")));
            images.put("enemySmall", ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Bigleg_small.gif")));
            
            sounds.put("audio", new SoundPlayer(1, "../Resources/music.wav"));
            sounds.put("click", new SoundPlayer(2,"../Resources/Sound_click.wav"));
            sounds.put("lost", new SoundPlayer(2, "../Resources/Sound_lost.wav"));
        } catch (IOException e) {
            System.out.println("Resource allocation failed.");
        }

        player1 = new Player(KeyEvent.VK_A, KeyEvent.VK_D);
        gameEvent = new GameEvents();
        gameEvent.addObserver(player1);
        KeyControl key1 = new KeyControl(gameEvent);
        addKeyListener(key1);

        // TODO: Fix this
        // levels = new RainbowReefLevel[1];
        // for (RainbowReefLevel level : levels) {
        //     level = new RainbowReefLevel(WINDOW_H, WINDOW_W, images);
        // }
        levels = new RainbowReefLevel(WINDOW_H, WINDOW_W, images);
        sounds.get("audio").play();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (start.contains(e.getPoint())) {
                    status = true;
                    sounds.get("click").play();
                }
                if (quit.contains(e.getPoint())) {
                    System.exit(0);
                    sounds.get("click").play();
                }
                if (help.contains(e.getPoint())) {
                    getHelp = true;
                    sounds.get("click").play();
                }
            }

            Rectangle start = new Rectangle(120, 350, 100, 32);
            Rectangle quit = new Rectangle(250, 350, 100, 32);
            Rectangle help = new Rectangle(380, 350, 100, 32);

        });

    }

    @Override
    public void paint(Graphics g) {

        BufferedImage bimg1;

        windowSize = getSize();
        bimg1 = (BufferedImage) createImage(WINDOW_W, WINDOW_H);
        g2 = bimg1.createGraphics();

        drawDemo();
        g.drawImage(bimg1, 0, 0, this);
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
                System.out.println("RainbowReef run method");
                break;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //draw the title image with new frame
        Image title = null;

        try {
            title = ImageIO.read(RainbowReefWorld.class.getResource("../Resources/Title.bmp"));
        } catch (IOException e) {
            System.out.println("Title image load failed.");
        }

        JFrame t = new JFrame("RainbowReefWorld");
        t.setSize(new Dimension(WINDOW_W, WINDOW_H));
        t.setBackground(Color.WHITE);
        t.setContentPane(new JLabel(new ImageIcon(title)));
        t.setVisible(true);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Main run method");
        }

        //draw really game frame
        final RainbowReefWorld demo = new RainbowReefWorld();
        demo.init();
        JFrame f = new JFrame("RainbowReefWorld");

        f.addWindowListener(new WindowAdapter() {});
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(WINDOW_W, WINDOW_H));
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        demo.start();
    }

}
