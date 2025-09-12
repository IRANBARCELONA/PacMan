import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;


import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;
        char name;
        String sname;

        int startX;
        int startY;
        char direction; // U D L R
        char previousDirection;
        char nextDirection;
        int velocityX = 0;
        int velocityY = 0;
        static int slife;
        static int slife2;

        int orangesLeft = 2;
        int cherrysLeft = 4;
        boolean isPacManAGhost;

        boolean setVulnerable = false;

        boolean isTurning = false;
        int turnTimer = 0;

        boolean isGunner;
        boolean isBulletCounting = false;
        int bulletCount;
        boolean gunLoaded;
        int bulletTimer;
        int gunCoolDown ;
        boolean gunshoted;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
            this.isGunner = false;
            this.bulletCount = 0;
            this.gunLoaded = false;
            this.bulletTimer = 10000;
            this.gunCoolDown = 0;
            this.gunshoted = false;
            this.isPacManAGhost = false;
        }

        Block(Image image, int x, int y, int width, int height, boolean setVulnerable, char name) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
            this.setVulnerable = setVulnerable;
            this.name = name;
        }

        Block(Image image, int x, int y, int width, int height, String sname) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
            this.sname = sname;
        }


        void updateDirection(char direction) {
            if(direction == this.direction){
                return;
            }
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            boolean collisionCheck = false;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    nextDirection = direction;
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                    collisionCheck = true;
                }
            }

            if(!collisionCheck){
                nextDirection = direction;
            }

        }

        void updateVelocity() {
            if(this.sname == "b"){
                if (this.direction == 'U') {
                    this.velocityX = 0;
                    this.velocityY = -tileSize/4;
                }
                else if (this.direction == 'D') {
                    this.velocityX = 0;
                    this.velocityY = tileSize/4;
                }
                else if (this.direction == 'L') {
                    this.velocityX = -tileSize/4;
                    this.velocityY = 0;
                }
                else if (this.direction == 'R') {
                    this.velocityX = tileSize/4;
                    this.velocityY = 0;
                }
            }
            else{
                if (this.direction == 'U') {
                    this.velocityX = 0;
                    this.velocityY = -tileSize/8;
                }
                else if (this.direction == 'D') {
                    this.velocityX = 0;
                    this.velocityY = tileSize/8;
                }
                else if (this.direction == 'L') {
                    this.velocityX = -tileSize/8;
                    this.velocityY = 0;
                }
                else if (this.direction == 'R') {
                    this.velocityX = tileSize/8;
                    this.velocityY = 0;
                }
            }
        }

        public void PacManImage(char direction, boolean gunner){
            if(PacMan.isPacManAGhost){
                return;
            }

            if(isPacManCanMove(PacMan, direction) && phase == 1){
                if (direction == 'U') {
                    PacMan.image = PacManUpImage;
                }
                else if (direction == 'D') {
                    PacMan.image = PacManDownImage;
                }
                else if (direction == 'L') {
                    PacMan.image = PacManLeftImage;
                }
                else if (direction == 'R') {
                    PacMan.image = PacManRightImage;
                }
            }
            else if(isPacManCanMove(PacMan, direction) && phase == 3){
                if (direction == 'U') {
                    if(!gunner)
                        PacMan.image = PacManUpImage;
                    else
                        PacMan.image = PacManGUpImage;
                }
                else if (direction == 'D') {
                    if(!gunner)
                        PacMan.image = PacManDownImage;
                    else
                        PacMan.image = PacManGDownImage;
                }
                else if (direction == 'L') {
                    if(!gunner)
                        PacMan.image = PacManLeftImage;
                    else
                        PacMan.image = PacManGLeftImage;
                }
                else if (direction == 'R') {
                    if(!gunner)
                        PacMan.image = PacManRightImage;
                    else
                        PacMan.image = PacManGRightImage;
                }
            }
            else if((phase == 4 || phase == 5 || phase == 6) && isPacManCanMove(PacMan, direction)){
                if (direction == 'U') {
                    PacMan.image = PacManSciUImage;
                }
                else if (direction == 'D') {
                    PacMan.image = PacManSciDImage;
                }
                else if (direction == 'L') {
                    PacMan.image = PacManSciLImage;
                }
                else if (direction == 'R') {
                    PacMan.image = PacManSciRImage;
                }
            }
            if(isPacManCanMove(PacMan, direction) && pacmanK){
                if (direction == 'U') {
                    PacMan.image = PacManKingUImage;
                }
                else if (direction == 'D') {
                    PacMan.image = PacManKingDImage;
                }
                else if (direction == 'L') {
                    PacMan.image = PacManKingLImage;
                }
                else if (direction == 'R') {
                    PacMan.image = PacManKingRImage;
                }
            }



        }
        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    class Position {
        int x, y;
        char direction;

        Position(int x, int y, char direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }



    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize ;
    private int boardHeight = rowCount * tileSize;
    private int counter = 0;
    private int beingGhostCounter = 0;

    private Image wallImage;
    private Image wallRPImage;
    private Image wallph2Image;
    private Image wallph5Image;

    
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image scaredGhostImage;
    private Image lightGhostYImage;
    private Image lightGhostOImage;
    private Image lightGhostGImage;
    private Image lightGhostRImage;
    private Image InfinityGhostYImage;
    private Image InfinityGhostRImage;
    private Image InfinityGhostPImage;
    private Image InfinityGhostGImage;
    private Image InfinityGhostYNImage;
    private Image InfinityGhostRNImage;
    private Image InfinityGhostPNImage;
    private Image InfinityGhostGNImage;
    private Image ghostKilledImage;
    
    private Image portalYImage;
    private Image portalGImage;
    private Image portalRImage;
    private Image portalOImage;
    private Image InfinityPortalImage;
    private Image brokenPortal;
    private Image brokenMainPortal;
    
    private Image cherryImage;
    private Image orangeImage;
    private Image ghostFruitImage;
    private Image HPImage;
    
    private Image PacManUpImage;
    private Image PacManDownImage;
    private Image PacManLeftImage;
    private Image PacManRightImage;
    private Image PacManGUpImage;
    private Image PacManGDownImage;
    private Image PacManGLeftImage;
    private Image PacManGRightImage;
    private Image PacManSciUImage;
    private Image PacManSciRImage;
    private Image PacManSciDImage;
    private Image PacManSciLImage;
    private Image PacManKingRImage;
    private Image PacManKingLImage;
    private Image PacManKingUImage;
    private Image PacManKingDImage;
    private Image pacmanKilledImage;

    private Image doorImage;
    private Image crownImage;

    private Image gunImage;
    private Image gunShotRImage;
    private Image gunShotLImage;
    private Image gunShotUImage;
    private Image gunShotDImage;
    private Image sciShotRImage;
    private Image sciShotLImage;
    private Image sciShotUImage;
    private Image sciShotDImage;
    private Image bulletUImage;
    private Image bulletDImage;
    private Image bulletRImage;
    private Image bulletLImage;
    private Image scifiUBulletImage;
    private Image scifiDBulletImage;
    private Image scifiRBulletImage;
    private Image scifiLBulletImage;
    private Image bulletDestImage;
    private Image sciBulletDestImage;

    private Image snakeheadLImage;
    private Image snakeheadRImage;
    private Image snakeheadUImage;
    private Image snakeheadDImage;
    private Image snakebodyHImage;
    private Image snakebodyVImage;
    private Image snakeendRImage;
    private Image snakeendUImage;
    private Image snakeendLImage;
    private Image snakeendDImage;
    private Image snaketurnImage1;
    private Image snaketurnImage2;
    private Image snaketurnImage3;
    private Image snaketurnImage4;

    private Image ph2FirstImage;
    private Image ph2SecondImage;
    private Image Winpic;

    BufferedImage[] wallImages;
    int currentWallImageIndex = 0;
    BufferedImage wallph3Image;

    ScheduledExecutorService imageScheduler;

    int imagePh = 0;

    boolean foodEatingSound = true;
    boolean mainSound = true;
    int mainMusicCounter = 0;

    //int lastScore = App.user.getLastScore();
    //int highScore = App.user.getHighScore();

    static boolean pacmanK = false;
    static boolean pacmanExit = false;

    boolean firstLightGhostSpawn = true;
    boolean firstInfinityGhostSpawn = true;
    long phase5StartTime = 0;
    int portalHealth = 100;
    private float opacity = 0f;
    private float opacityGhost = 0f;
    long imageSpawnerDelay = 0;
    boolean firstphase2image = false;
    boolean secondphase2image = false;
    boolean phasechanger = false;
    long phase5to6;
    boolean winpicture;
    long winpictureDelay;
    boolean ph21play = true;
    boolean ph22play = true;
    boolean ph3play = false;



    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "XNNNNNNNXXNNXXNNNNX",
            "XNXXXNXNXXXNNNNXXNX",
            "XNoNXXXNNNXNXNXXXNX",
            "XNXXXXNNXNNNNNXNXNX",
            "XNNNNXNXNNXNXXXpXNX",
            "XXXNXNNNXXXNNNNNNNX",
            "XNNNNNXNNNNNXNXXXXX",
            "XNXXXXgggNgggNNNNNX",
            "NNXNNNgNgNgNgNXXXNM",
            "XNXNXNggNPgggNNNXNX",
            "XNNNNNgNgNgNNNXNNNX",
            "XNXXNXgbgNgNXXXXNXX",
            "XNNX              X",
            "XNXNXXNXXXNXNXXXNXX",
            "XNNNNNNNNNNNNNNNNNX",
            "XE11111HNNNNNNNNNNX",
            "XNNNNNNNXNrNNXNNNNX",
            "XXXNXXXNXXXXNXXXXNX",
            "XNNNNNNNNNNNNNNNNNX",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private String[] tileMap7 = {
            "XXXXXXXXXXXXXXXXXXX",
            "XNNNNNNNXXNNXXNNNNX",
            "XNXXXNXNXXXNNNNXXNX",
            "XNoNXXXNNNXNXNXXXNX",
            "XNXXXXNNXNNNNNXNXNX",
            "XNNNNXNXNNXNXXXpXNX",
            "XXXNXNNNXXXNNNNNNNX",
            "XNNNNNXNNNNNXNXXXXX",
            "XNXXXXgggNgggNNNNNX",
            "NNXNNNgNgNgNgNXXXNM",
            "XNXNXNggNPgggNNNXNX",
            "XNNNNNgNgNgNNNXNNNX",
            "XNXXNXgbgNgNXXXXNXX",
            "XNNNNNNNNNNNNNNNNNX",
            "XNNNXXXXXXXXXXXXNNX",
            "XNNNNNNXXXXXXNNNNNX",
            "XNNNNNNNXXXXNNNNNNX",
            "XNNNNNNNNXXNNNNNNNX",
            "XNNNNNNNNNNNNNNNNNX",
            "XNNNNNNNNNNNNNNNNNX",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private String[] tileMap5 = {
            "XXXXXXXXXXXXXXXXXXX",
            "X       XX  XX    X",
            "X XXX X XXX    XX X",
            "X o XXX   X X XXX X",
            "X XXXX  X     X X X",
            "X    X X  X XXXpX X",
            "XXX X   XXX       X",
            "X     X     X XXXXX",
            "X XXXXggg ggg     X",
            "N X   gNg gNg XXX M",
            "X X X gg Pggg   X X",
            "X     g g g   X   X",
            "X XX Xgbg g XXXX XX",
            "X  X              X",
            "X X XX XXX X XXX XX",
            "X                 X",
            "X XXXXX X XX X X XX",
            "X       X r  X    X",
            "XXX XXX XXXX XXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private String[] tileMap3 = {
            "XXXXXXXXXXXXXXXXXXX",
            "X0Y     XX  XX   1X",
            "X XXX X XXX    XX X",
            "X   XXX   X X XXX X",
            "X XXXX  X     X X X",
            "X    X X  X XXX X X",
            "XXX X   XXX       X",
            "X     X     X XXXXX",
            "X XXXXggg ggg     X",
            "N X   gNg gNg XXX M",
            "X X X gg Pggg   X X",
            "X     g g g   X   X",
            "X XX Xg g g XXXX XX",
            "X  X              X",
            "X X XX XXX X XXX XX",
            "X              X  X",
            "X XXXXX X XX X X XX",
            "X       X    X    X",
            "XXX XXX XXXX XXXX X",
            "X2               3X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private String[] tileMap4 = {
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XNXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXX  NNNNNpXXX",
            "XXXXXXXXX XXXXXXXXX",
            "XXXXXXXXX XXXXXXXXX",
            "XXXXXXggg gggXXXXXX",
            "XXXXXXgNgOgNgXNNNNM",
            "XXXXXXgg PgggXXXXXX",
            "XXXXXXNNNNNNNXXXXXX",
            "XXXXXXNNNNNNNXXXXXX",
            "XXXXXXNNNNNNNNXXXXX",
            "X        NNNNNNNNNX",
            "XNNNNNNNNNNNNNGoRYX",
            "XXXXXXXXXXXXXXXXXXX",
            "XrXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private String[] tileMap2 = {
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXX    XXXXXXXX",
            "XXXXX         XXXXX",
            "XXX             XXX",
            "X    X X  X XXXpX X",
            "XXX X   XXX       X",
            "X     X     X XXXXX",
            "X XXXXggg ggg     X",
            "N X   gNg gNg XXX M",
            "X X X gg Pggg   X X",
            "X     g g g   X   X",
            "X XX Xgbg g XXXX XX",
            "X  X              X",
            "X X XX XXX X XXX XX",
            "XE11111H          X",
            "XXX             XXX",
            "XXXXX         XXXXX",
            "XXXXXXX     XXXXXXX",
            "XXXXXXXXXXXXXXXXXXX",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private String[] tileMapW = {
        "XXXXXXXXXXXXXXXXXXX",
        "X  E              X",
        "XXXXXXXXXXXXXXXX  X",
        "X                 X",
        "X  XXXXXXXXXXXXXXXX",
        "X  g   g          X",
        "X   g g           X",
        "X    g  ggg g  g  X",
        "X    g  g g g  g  X",
        "N    g  ggg  ggg  M",
        "X                 X",
        "X                 X",
        "X   g g g g       X",
        "X   g g g   ggg   X",
        "X   g g g g gP g  X",
        "X   gg gg g g  g  X",
        "XXXXXXXXXXXXX  X  X",
        "X                 X",
        "X XXXXXXXXXXXXXXXXX",
        "X           K     X",
        "XXXXXXXXXXXXXXXXXXX"
};

    ArrayList<Block> walls;
    HashSet<Block> foods;
    ArrayList<Block> ghosts;
    HashSet<Block> scaredGhosts;
    ArrayList<PacMan.Block> lightGhosts;
    ArrayList<Block> InfinityGhosts;
    HashSet<Block> cherrys;
    HashSet<Block> oranges;
    HashSet<Block> ghostFruits;
    ArrayList<Block> smallPortals;
    ArrayList<Block> snake;
    ArrayList<Block> snake2;
    ArrayList<Block> guns;
    Block PacMan;
    Block InfinityPortal;

    Timer gameLoop;

    boolean running = true;
    static boolean running2 = false;

    int res = 0;

    char[] directions = {'U', 'U', 'U', 'D', 'L', 'L', 'L', 'R', 'L', 'U'};
    char[] directions1 = {'U', 'U', 'U', 'U', 'R', 'R', 'L', 'D', 'R', 'R'};
    char[] directions2 = {'D', 'D', 'D', 'D', 'L', 'L', 'R', 'U', 'L', 'L'};
    char[] directions3 = {'D', 'D', 'D', 'U', 'L', 'R', 'R', 'D', 'R', 'R'};
    char[] directions00 = {'U','D','R', 'L'};
    Random random = new Random();


    int score = 0;
    int lives = 10;
    int phase = 1;
    int bulletDest = 0;
    int sciBulletDest = 0;
    int ghostKilled = 0;
    int pacmanKilled = 0;
    int gunShotL = 0;
    int gunShotR = 0;
    int gunShotU = 0;
    int gunShotD = 0;
    int sciShotL = 0;
    int sciShotR = 0;
    int sciShotU = 0;
    int sciShotD = 0;


    boolean gameOver = false;
    List<Position> previousPositions = new ArrayList<>();
    List<Position> previousPositions2 = new ArrayList<>();
    List<Position> bulletPositions = new ArrayList<>();





    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);


        wallImage = new ImageIcon("./Media/Images/wall.png").getImage();
        wallRPImage = new ImageIcon("./Media/Images/wallrp.png").getImage();
        wallph2Image = new ImageIcon("./Media/Images/wallph2Image.png").getImage();
        try {
            wallImages = new BufferedImage[] {
                    ImageIO.read(new File("./Media/Images/wallB.png")),
                    ImageIO.read(new File("./Media/Images/wallph3Image.png"))
            };
            wallph3Image = wallImages[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        wallph5Image = new ImageIcon("./Media/Images/wallph5Image.png").getImage();

        blueGhostImage = new ImageIcon("./Media/Images/blueGhost.png").getImage();
        orangeGhostImage = new ImageIcon("./Media/Images/orangeGhost.png").getImage();
        pinkGhostImage = new ImageIcon("./Media/Images/pinkGhost.png").getImage();
        redGhostImage = new ImageIcon("./Media/Images/redGhost.png").getImage();
        scaredGhostImage = new ImageIcon("./Media/Images/scaredGhost.png").getImage();
        lightGhostYImage = new ImageIcon("./Media/Images/lightGhostY.png").getImage();
        lightGhostOImage = new ImageIcon("./Media/Images/lightGhostO.png").getImage();
        lightGhostRImage = new ImageIcon("./Media/Images/lightGhostR.png").getImage();
        lightGhostGImage = new ImageIcon("./Media/Images/lightGhostG.png").getImage();
        InfinityGhostRNImage = new ImageIcon("./Media/Images/armorR.png").getImage();
        InfinityGhostGNImage = new ImageIcon("./Media/Images/armorG.png").getImage();
        InfinityGhostYNImage = new ImageIcon("./Media/Images/armorY.png").getImage();
        InfinityGhostPNImage = new ImageIcon("./Media/Images/armorP.png").getImage();
        InfinityGhostPImage = new ImageIcon("./Media/Images/armorGhostP.png").getImage();
        InfinityGhostYImage = new ImageIcon("./Media/Images/armorGhostY.png").getImage();
        InfinityGhostRImage = new ImageIcon("./Media/Images/armorGhostR.png").getImage();
        InfinityGhostGImage = new ImageIcon("./Media/Images/armorGhostG.png").getImage();
        ghostKilledImage = new ImageIcon("./Media/Images/ghostKilled.png").getImage();

        ph2FirstImage = new ImageIcon("./Media/Images/phaseSt.png").getImage();
        ph2SecondImage = new ImageIcon("./Media/Images/phaseEn.png").getImage();


        portalYImage = new ImageIcon("./Media/Images/portalY.png").getImage();
        portalRImage = new ImageIcon("./Media/Images/portalR.png").getImage();
        portalGImage = new ImageIcon("./Media/Images/portalG.png").getImage();
        portalOImage = new ImageIcon("./Media/Images/portalB.png").getImage();
        InfinityPortalImage = new ImageIcon("./Media/Images/portal2.png").getImage();
        brokenPortal = new ImageIcon("./Media/Images/portalDead.png").getImage();
        brokenMainPortal = new ImageIcon("./Media/Images/portalDead2.png").getImage();

        cherryImage = new ImageIcon("./Media/Images/cherry.png").getImage();
        orangeImage = new ImageIcon("./Media/Images/orange.png").getImage();
        ghostFruitImage = new ImageIcon("./Media/Images/ghostFruit.png").getImage();
        HPImage = new ImageIcon("./Media/Images/HP.png").getImage();

        PacManUpImage = new ImageIcon("./Media/Images/PacManUp.png").getImage();
        PacManDownImage = new ImageIcon("./Media/Images/PacManDown.png").getImage();
        PacManLeftImage = new ImageIcon("./Media/Images/PacManLeft.png").getImage();
        PacManRightImage = new ImageIcon("./Media/Images/PacManRight.png").getImage();
        PacManGUpImage = new ImageIcon("./Media/Images/PacMangunu.png").getImage();
        PacManGDownImage = new ImageIcon("./Media/Images/PacMangund.png").getImage();
        PacManGLeftImage = new ImageIcon("./Media/Images/PacMangunl.png").getImage();
        PacManGRightImage = new ImageIcon("./Media/Images/PacMangunr.png").getImage();
        PacManSciUImage = new ImageIcon("./Media/Images/pacsciu.png").getImage();
        PacManSciDImage = new ImageIcon("./Media/Images/pacscid.png").getImage();
        PacManSciLImage = new ImageIcon("./Media/Images/pacscil.png").getImage();
        PacManSciRImage = new ImageIcon("./Media/Images/pacscir.png").getImage();
        PacManKingRImage = new ImageIcon("./Media/Images/pacmanKingr.png").getImage();
        PacManKingLImage = new ImageIcon("./Media/Images/pacmanKingl.png").getImage();
        PacManKingUImage = new ImageIcon("./Media/Images/pacmanKingu.png").getImage();
        PacManKingDImage = new ImageIcon("./Media/Images/pacmanKingd.png").getImage();
        pacmanKilledImage = new ImageIcon("./Media/Images/pacmanKilled.png").getImage();

        doorImage = new ImageIcon("./Media/Images/door.png").getImage();
        crownImage = new ImageIcon("./Media/Images/crown.png").getImage();
        Winpic = new ImageIcon("./Media/Images/winPic.png").getImage();

        bulletUImage = new ImageIcon("./Media/Images/bulletu.png").getImage();
        bulletDImage = new ImageIcon("./Media/Images/bulletd.png").getImage();
        bulletLImage = new ImageIcon("./Media/Images/bulletl.png").getImage();
        bulletRImage = new ImageIcon("./Media/Images/bulletr.png").getImage();
        bulletDestImage = new ImageIcon("./Media/Images/bulletDest.png").getImage();
        sciBulletDestImage = new ImageIcon("./Media/Images/sciBulletDest.png").getImage();
        scifiUBulletImage = new ImageIcon("./Media/Images/sciBulletu.png").getImage();
        scifiDBulletImage = new ImageIcon("./Media/Images/sciBulletd.png").getImage();
        scifiRBulletImage = new ImageIcon("./Media/Images/sciBulletr.png").getImage();
        scifiLBulletImage = new ImageIcon("./Media/Images/sciBulletl.png").getImage();
        gunImage = new ImageIcon("./Media/Images/gun.png").getImage();
        gunShotRImage = new ImageIcon("./Media/Images/gunShotR.png").getImage();
        gunShotLImage = new ImageIcon("./Media/Images/gunShotL.png").getImage();
        gunShotUImage = new ImageIcon("./Media/Images/gunShotU.png").getImage();
        gunShotDImage = new ImageIcon("./Media/Images/gunShotD.png").getImage();
        sciShotRImage = new ImageIcon("./Media/Images/sciShotR.png").getImage();
        sciShotLImage = new ImageIcon("./Media/Images/sciShotL.png").getImage();
        sciShotUImage = new ImageIcon("./Media/Images/sciShotU.png").getImage();
        sciShotDImage = new ImageIcon("./Media/Images/sciShotD.png").getImage();

        snakeheadLImage = new ImageIcon("./Media/Images/snakehl.png").getImage();
        snakeheadRImage = new ImageIcon("./Media/Images/snakehr.png").getImage();
        snakeheadUImage = new ImageIcon("./Media/Images/snakehu.png").getImage();
        snakeheadDImage = new ImageIcon("./Media/Images/snakehd.png").getImage();
        snakebodyHImage = new ImageIcon("./Media/Images/snakebh.png").getImage();
        snakebodyVImage = new ImageIcon("./Media/Images/snakebv.png").getImage();
        snakeendRImage = new ImageIcon("./Media/Images/snakeer.png").getImage();
        snakeendUImage = new ImageIcon("./Media/Images/snakeeu.png").getImage();
        snakeendLImage = new ImageIcon("./Media/Images/snakeel.png").getImage();
        snakeendDImage = new ImageIcon("./Media/Images/snakeed.png").getImage();
        snaketurnImage1 = new ImageIcon("./Media/Images/snaketrt1.png").getImage();
        snaketurnImage2 = new ImageIcon("./Media/Images/snaketrt2.png").getImage();
        snaketurnImage3 = new ImageIcon("./Media/Images/snaketrt3.png").getImage();
        snaketurnImage4 = new ImageIcon("./Media/Images/snaketrt4.png").getImage();


        PacMan.slife = 1;
        PacMan.slife2 = 1;


        loadMap();

        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        //how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(25, this);
        gameLoop.start();

    }

    public void loadMap() {
        walls = new ArrayList<Block>();
        foods = new HashSet<Block>();
        ghosts = new ArrayList<Block>();
        cherrys = new HashSet<Block>();
        oranges = new HashSet<Block>();
        ghostFruits = new HashSet<>();
        smallPortals = new ArrayList<>();
        scaredGhosts = new HashSet<Block>();
        lightGhosts = new ArrayList<PacMan.Block>();
        InfinityGhosts = new ArrayList<>();
        guns = new ArrayList<Block>();
        snake = new ArrayList<>();
        snake2 = new ArrayList<>();
        InfinityPortal = new Block(InfinityPortalImage, 32 * 7, 32 * 8, 32 * 5, 32 * 5);
        
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'g') { //wallRP
                    Block wall = new Block(wallRPImage, x, y, tileSize, tileSize );
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize, false, 'b');
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize, false, 'o');
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize, false, 'p');
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize, false, 'r');
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') { //PacMan
                    PacMan = new Block(PacManRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == 'C') { //PacMan
                    Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                    cherrys.add(cherry);
                }
                else if (tileMapChar == 'O') { //PacMan
                    Block orange = new Block(orangeImage, x, y, tileSize, tileSize);
                    oranges.add(orange);
                }
                else if (tileMapChar == ' ') { //food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public void loadMap2() {

        walls.clear();
        resetPositions();
        ArrayList<Integer> removeIndex = new ArrayList<>();
        for(int i = 0; i < walls.size(); i++) {
            Block wall = walls.get(i);
            int x = wall.x / tileSize;
            int y = wall.y / tileSize;
            char character = tileMap[y].charAt(x);
            if(character != 'X') {
                removeIndex.add(i);
            }
            walls.get(i).image = wallph2Image;
        }
        for (int i = removeIndex.size() - 1; i >= 0; i--) {
            walls.remove((int) removeIndex.get(i));
        }




        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;



                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallph2Image, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'g') { //wallRP
                    Block wall = new Block(wallRPImage, x, y, tileSize, tileSize );
                    walls.add(wall);
                }
                else if (tileMapChar == 'H') { //snakes
                    Block snake1 = new Block(snakeheadRImage, x, y, tileSize , tileSize ,"h");
                    snake.add(snake1);
                    Block snake4 = new Block(snakeheadRImage, x + 5 * tileSize, y, tileSize , tileSize ,"h");
                    snake2.add(snake4);
                }
                else if (tileMapChar == '1') { //snakes
                    Block snake3 = new Block(snakebodyHImage, x, y, tileSize , tileSize ,"b");
                    snake.add(snake3);
                    Block snake = new Block(snakebodyHImage, x + 5 * tileSize, y, tileSize , tileSize ,"b");
                    snake2.add(snake);
                }
                else if (tileMapChar == 'E') { //snakes
                    Block snake5 = new Block(snakeendRImage, x , y, tileSize , tileSize ,"e");
                    snake.add(snake5);
                    Block snake9 = new Block(snakeendRImage, x + 5 * tileSize, y , tileSize , tileSize ,"e");
                    snake2.add(snake9);
                }
                else if (tileMapChar == ' ') { //food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public void loadMap3() {
        synchronized(walls) {
            walls.clear();
        }

        if(imagePh == 0){
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    String row = tileMap3[r];
                    char tileMapChar = row.charAt(c);

                    int x = c*tileSize;
                    int y = r*tileSize;

                    if (tileMapChar == 'X') { //wall phase 3
                        PacMan.Block wall = new PacMan.Block(wallph3Image, x, y, tileSize, tileSize);
                        walls.add(wall);
                    }
                    else if (tileMapChar == 'g') { //wallRP
                        PacMan.Block wall = new PacMan.Block(wallRPImage, x, y, tileSize, tileSize, "g");
                        walls.add(wall);
                    }
                    else if (tileMapChar == 'Y') { //wallRP
                        PacMan.Block lightghostY = new Block(lightGhostYImage, x, y, tileSize, tileSize);
                        lightGhosts.add(lightghostY);
                    }
                    else if (tileMapChar == '0') {
                        Block portal = new Block(portalYImage, x - 24, y - 24, 50, 50);
                        smallPortals.add(portal);
                    }
                    else if (tileMapChar == '1') {
                        Block portal = new Block(portalRImage, x - 8, y - 24, 50, 50);
                        smallPortals.add(portal);
                    }
                    else if (tileMapChar == '2') {
                        Block portal = new Block(portalOImage, x - 24, y - 24, 50, 50);
                        smallPortals.add(portal);
                    }
                    else if (tileMapChar == '3') {
                        Block portal = new Block(portalGImage, x - 8, y - 24, 50, 50);
                        smallPortals.add(portal);
                    }
                    else if (tileMapChar == ' ') { //food
                        PacMan.Block food = new PacMan.Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                    }
                }
            }
        }
        else{
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    String row = tileMap3[r];
                    char tileMapChar = row.charAt(c);

                    int x = c*tileSize;
                    int y = r*tileSize;

                    if (tileMapChar == 'X') { //wall phase 3
                        PacMan.Block wall = new PacMan.Block(wallph3Image, x, y, tileSize, tileSize);
                        walls.add(wall);
                    }
                    else if (tileMapChar == 'g') { //wallRP
                        PacMan.Block wall = new PacMan.Block(wallRPImage, x, y, tileSize, tileSize, "g");
                        walls.add(wall);
                    }
                }
            }
        }
    }

    public void loadMapW() {
        synchronized(walls) {
            walls.clear();
        }

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMapW[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallph5Image, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'g') { //wallRP
                    Block wall = new Block(wallRPImage, x, y, tileSize, tileSize );
                    walls.add(wall);
                }
                else if (tileMapChar == 'K') {
                    Block king = new Block(crownImage, x, y, tileSize, tileSize, "K");
                    cherrys.add(king);
                }
                else if (tileMapChar == 'E') {
                    Block door = new Block(doorImage, x, y, tileSize, tileSize, "d");
                    cherrys.add(door);
                }
                
                
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (phase == 1) {
            g.setColor(Color.YELLOW);
            for (PacMan.Block food : foods) {
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        } else if (phase == 3) {
            AudioManager.stopLooping("normalMove");
            AudioManager.stopLooping("scaryGhostTime");
            g.setColor(Color.GREEN);
            for (PacMan.Block food : foods) {
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        } else if (phase == 4) {
            if (imagePh % 2 == 0) {
                g.setColor(Color.BLACK);
                for (PacMan.Block food : foods) {
                    g.fillRect(food.x, food.y, food.width, food.height);
                }
            } else {
                g.setColor(Color.PINK);
                for (PacMan.Block food : foods) {
                    g.fillRect(food.x, food.y, food.width, food.height);
                }
            }
        }

        if (phase == 5 && !phasechanger) {
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacity < 1f) {
                opacity += 0.0001f;
                if (opacity > 1f) opacity = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(InfinityPortalImage, 32 * 7, 32 * 8, 32 * 5, 32 * 5, null);

            g2d.dispose();
        }
        else if (phase == 5 && phasechanger) {
            g.drawImage(brokenMainPortal, 32 * 7, 32 * 8, 32 * 5, 32 * 5, null);
        }


        for (Block snake : snake) {
            g.drawImage(snake.image, snake.x, snake.y, snake.width, snake.height, null);
        }

        for (Block snake : snake2) {
            g.drawImage(snake.image, snake.x, snake.y, snake.width, snake.height, null);
        }

        synchronized (walls) {
            for (Block wall : walls) {
                g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
            }
        }

        for (Block cherry : cherrys) {
            if(!pacmanK || cherry.sname == "d")
                g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }


        for (Block orange : oranges) {
            g.drawImage(orange.image, orange.x, orange.y, orange.width, orange.height, null);
        }


        for (Block ghostFruit : ghostFruits) {
            g.drawImage(ghostFruit.image, ghostFruit.x, ghostFruit.y, ghostFruit.width, ghostFruit.height, null);
        }

        if (phase == 4) {
            for (Block portal : smallPortals) {
                g.drawImage(portal.image, portal.x + 8, portal.y + 8, portal.width, portal.height, null);
            }
        }
        else if(phase == 5){
            for (Block portal : smallPortals) {
                g.drawImage(brokenPortal, portal.x, portal.y, portal.width, portal.height, null);
            }
        }

        for (Block scaredGhost : scaredGhosts) {
            g.drawImage(scaredGhost.image, scaredGhost.x, scaredGhost.y, scaredGhost.width, scaredGhost.height, null);
        }

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (PacMan.Block lightGhost : lightGhosts) {
            g.drawImage(lightGhost.image, lightGhost.x, lightGhost.y, lightGhost.width, lightGhost.height, null);
        }

        for (PacMan.Block InfinityGhost : InfinityGhosts) {
            g.drawImage(InfinityGhost.image, InfinityGhost.x, InfinityGhost.y, InfinityGhost.width, InfinityGhost.height, null);
        }


        for (Block gun : guns) {
            g.drawImage(gun.image, gun.x, gun.y, gun.width, gun.height, null);
        }

        /*int bulletDest = 0;
        int sciBulletDest = 0;
        int ghostKilled = 0;
        int pacmanKilled = 0;
        int gunShotL = 0;
        int gunShotR = 0;
        int gunShotU = 0;
        int gunShotD = 0; */

        if (bulletDest > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();

            if (opacityGhost < 1f) {
                opacityGhost += 0.01f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            
            g2d.drawImage(bulletDestImage, bulletPositions.get( bulletPositions.size() - 1 ).x  ,
            bulletPositions.get( bulletPositions.size() - 1 ).y  , tileSize , tileSize , null);
            
            g2d.dispose();
            if(opacityGhost == 1f){
                bulletDest = 0;
                opacityGhost = 0;
            }
        }

        if (sciBulletDest > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();

            if (opacityGhost < 1f) {
                opacityGhost += 0.01f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            
            g2d.drawImage(sciBulletDestImage, bulletPositions.get( bulletPositions.size() - 1 ).x  ,
            bulletPositions.get( bulletPositions.size() - 1 ).y  , tileSize * 2 , tileSize * 2 , null);
            
            g2d.dispose();
            if(opacityGhost == 1f){
                sciBulletDest = 0;
                opacityGhost = 0;
            }
        }

        if (gunShotD > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(gunShotDImage, PacMan.x , PacMan.y + tileSize , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                gunShotD = 0;
                opacityGhost = 0;
            }
        }

        if (gunShotL > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(gunShotLImage, PacMan.x - tileSize , PacMan.y  , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                gunShotL = 0;
                opacityGhost = 0;
            }
        }

        if (gunShotU > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(gunShotUImage, PacMan.x  , PacMan.y - tileSize , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                gunShotU = 0;
                opacityGhost = 0;
            }
        }

        if (gunShotR > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(gunShotRImage, PacMan.x + tileSize , PacMan.y  , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                gunShotR = 0;
                opacityGhost = 0;
            }
        }

        if (sciShotD > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(sciShotDImage,  PacMan.x , PacMan.y + tileSize , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                sciShotD = 0;
                opacityGhost = 0;
            }
        }

        if (sciShotL > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(sciShotLImage, PacMan.x - tileSize , PacMan.y  , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                sciShotL = 0;
                opacityGhost = 0;
            }
        }

        if (sciShotU > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(sciShotUImage, PacMan.x  , PacMan.y - tileSize , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                sciShotU = 0;
                opacityGhost = 0;
            }
        }

        if (sciShotR > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(sciShotRImage,PacMan.x + tileSize , PacMan.y  , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                sciShotR = 0;
                opacityGhost = 0;
            }
        }

        if (ghostKilled > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.01f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(ghostKilledImage, bulletPositions.get( bulletPositions.size() - 1 ).x  , 
            bulletPositions.get( bulletPositions.size() - 1 ).y , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                ghostKilled = 0;
                opacityGhost = 0;
            }
        }

        if (pacmanKilled > 0){
            
            Graphics2D g2d = (Graphics2D) g.create();


            if (opacityGhost < 1f) {
                opacityGhost += 0.1f;
                if (opacityGhost > 1f) opacityGhost = 1f;
                repaint();
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1 - opacityGhost));
            g2d.drawImage(pacmanKilledImage, PacMan.x  , PacMan.y - tileSize , tileSize , tileSize , null);

            g2d.dispose();
            if(opacityGhost == 1f){
                pacmanKilled = 0;
                opacityGhost = 0;
            }
        }
        


        g.drawImage(PacMan.image, PacMan.x, PacMan.y, PacMan.width, PacMan.height, null);

        if (phase == 5) {
            Graphics2D g2d2 = (Graphics2D) g;
            int x = 32 * 5;
            int y = 32 * 2 + 4;
            int width = 32 * 9;
            int height = 16;


            int healthBarWidth = (int) (width * (portalHealth / 100.0));


            g2d2.setColor(Color.BLACK);
            g2d2.fillRect(x - 1, y - 1, width + 2, height + 2);


            g2d2.setColor(Color.RED);
            g2d2.fillRect(x, y, healthBarWidth, height);

        }



        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Playbill", Font.PLAIN, 30));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize / 2, tileSize / 2);
        } else {
            g.drawString("LIVES: ", tileSize / 1, tileSize / 1);
            if (lives < 11){
                for ( int i = 1 ; i <= lives ; i++ ){
                    g.drawImage(HPImage, tileSize * (i + 2) , tileSize / 2 - 8 , tileSize - 5 , tileSize - 5, null);
                }
                g.drawString("Score: " + String.valueOf(score) , tileSize * 15, tileSize / 1);
            }
            else {
                for ( int i = 1 ; i <= 11 ; i++ ){
                    g.drawImage(HPImage, tileSize * (i + 2) , tileSize / 2 - 8 , tileSize - 5 , tileSize - 5, null);
                }
                g.drawString("Score: " + String.valueOf(score) , tileSize * 15, tileSize / 1);
            }
        }

        if (phase == 3 && PacMan.isGunner) {
            g.drawString("Ammo: ", tileSize, (rowCount - 1) * tileSize);
            if (PacMan.bulletCount < 17){
                for ( int i = 1 ; i <= PacMan.bulletCount ; i++ ){
                    g.drawImage(bulletUImage, tileSize * (i + 2) - 15, (rowCount - 2) * tileSize + 8 , tileSize - 10 , tileSize - 10, null);
                }
            }
            else {
                for ( int i = 1 ; i <= 17 ; i++ ){
                    g.drawImage(bulletUImage, tileSize * (i + 2) - 15, (rowCount - 2) * tileSize + 8 , tileSize - 10 , tileSize - 10, null);
                }
            }
        }

        if (phase == 5) {
            g.drawString("Ammo: ", tileSize, (rowCount - 1) * tileSize);
            if (PacMan.bulletCount < 17){
                for ( int i = 1 ; i <= PacMan.bulletCount ; i++ ){
                    g.drawImage(scifiUBulletImage, tileSize * (i + 2) - 15, (rowCount - 2) * tileSize + 8 , tileSize - 10 , tileSize - 10, null);
                }
            }
            else {
                for ( int i = 1 ; i <= 17 ; i++ ){
                    g.drawImage(scifiUBulletImage, tileSize * (i + 2) - 15, (rowCount - 2) * tileSize + 8 , tileSize - 10 , tileSize - 10, null);
                }
            }
        }

        if(firstphase2image){
            g.drawImage(ph2FirstImage, 0, 0, 19 * tileSize , 22 * tileSize, null);
        }
        if(secondphase2image){
            g.drawImage(ph2SecondImage, 0, 0, 19 * tileSize , 22 * tileSize, null);
        }
        if(winpicture && winpictureDelay > 0){
            g.drawImage(Winpic, 0, 0, 19 * tileSize , 22 * tileSize, null);
        }


    }
    public boolean isPacManCanMove(Block block, char direction) {
        for (Block wall : walls) {
            if(direction == 'U' && wall.y == block.y + tileSize){
                return false;
            }
            else if(direction == 'R' && wall.x == block.x - tileSize){
                return false;
            }
            else if(direction == 'L' && wall.x == block.x + tileSize){
                return false;
            }
            else if(direction == 'D' && wall.y == block.y - tileSize){
                return false;
            }
        }
        return true;
    }

    public boolean canMove(Block block, Block wall, char direction){
        switch(direction){
            case 'U':
                if(block.x == wall.x && block.y > wall.y){
                    return true;
                }
            case 'D':
                if(block.x == wall.x && block.y < wall.y){
                    return true;
                }
            case 'L':
                if(block.x > wall.x && block.y == wall.y){
                    return true;
                }
            case 'R':
                if(block.x < wall.x && block.y == wall.y){
                    return true;
                }
        }
        return false;
    }

    public char oppositeDirection(char direction){
        switch(direction){
            case 'U':
                return 'D';
            case 'D':
                return 'U';
            case 'R':
                return 'L';
            case 'L':
                return 'R';
        }
        return 'X';
    }

    public void move() {
        PacManMove();
        checkCollision();
        phaseHandler();
        bulletMove();
        InfinityGhost();
        spawner();
    }

    public void spawner(){
        if(phase == 1) fruitSpawner();
        InfinityGhostSpawner();
        ghostFruitSpawner();
        lightGhostSpawner();
        ghostValnrability();
        bulletSpawner();
    }

    public void bulletSpawner() {
        if(phase == 3){
            if(random.nextInt(300) == 21){
                int x, y, attempts = 0;
                do {
                    x = random.nextInt(columnCount);
                    y = random.nextInt(rowCount);
                    attempts++;
                } while (tileMap[y].charAt(x) != ' ' && attempts < 100);

                if (attempts < 100) {
                    Block bullet = new Block(bulletUImage, x * tileSize + 5, y * tileSize + 5, tileSize * 2 / 3, tileSize * 2 / 3, "bullet");
                    guns.add(bullet);

                    if (guns.size() > 50) {
                        guns.remove(0);
                    }
                }
            }
        }
        else if (phase == 5) {
            if (phase5StartTime == 0) {
                phase5StartTime = System.currentTimeMillis();
            }

            long elapsed = System.currentTimeMillis() - phase5StartTime;

            if (elapsed < 15000) {
                if (random.nextInt(30) == 21) {
                    int x, y, attempts = 0;
                    do {
                        x = random.nextInt(columnCount);
                        y = random.nextInt(rowCount);
                        attempts++;
                    } while (tileMap3[y].charAt(x) != ' ' && attempts < 100);

                    if (attempts < 100) {
                        Block bullet = new Block(scifiUBulletImage, x * tileSize + 5, y * tileSize + 5, tileSize * 2 / 3, tileSize * 2 / 3, "bullet");
                        guns.add(bullet);

                        if (guns.size() > 50) {
                            guns.remove(0);
                        }
                    }
                }
            }
            else{
                guns.removeIf(bullet -> bullet.sname.equals("bullet"));
                AudioManager.stop("earthquick");
                if(!ph3play){
                    AudioManager.playLooping("ph3");
                    ph3play = true;
                }

            }
        }

    }

    public void ghostFruitSpawner() {
        if(phase == 4 && !PacMan.isPacManAGhost){
            if(random.nextInt(100) == 21){
                int x, y, attempts = 0;
                do {
                    x = random.nextInt(columnCount);
                    y = random.nextInt(rowCount);
                    attempts++;
                } while (tileMap3[y].charAt(x) != ' ' && attempts < 100);

                if (attempts < 100) {
                    PacMan.Block ghostFruit = new PacMan.Block(ghostFruitImage, x * tileSize + 6, y * tileSize + 6, 20, 20);
                    ghostFruits.add(ghostFruit);

                }
            }
        }
    }

    public void InfinityGhostSpawner() {
        if(phase == 5){
            if(firstInfinityGhostSpawn){
                Block InfinityGhost = new Block(InfinityGhostRImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorR");
                Block InfinityGhost1 = new Block(InfinityGhostYImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorY");
                Block InfinityGhost2 = new Block(InfinityGhostPImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorP");
                Block InfinityGhost3 = new Block(InfinityGhostGImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorG");

                InfinityGhosts.add(InfinityGhost);
                InfinityGhosts.add(InfinityGhost1);
                InfinityGhosts.add(InfinityGhost2);
                InfinityGhosts.add(InfinityGhost3);


                firstInfinityGhostSpawn = false;
            }

            int ghostspawnnum = random.nextInt(600);
            if(ghostspawnnum == 1){
                int color = random.nextInt(4);
                switch (color){
                    case 0:
                        Block InfinityGhost = new Block(InfinityGhostGImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorG");
                        InfinityGhosts.add(InfinityGhost);
                        AudioManager.play("ghostspawn");
                        break;
                    case 1:
                        Block InfinityGhost1 = new Block(InfinityGhostRImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorR");
                        InfinityGhosts.add(InfinityGhost1);
                        AudioManager.play("ghostspawn");
                        break;
                    case 2:
                        Block InfinityGhost2 = new Block(InfinityGhostPImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorP");
                        InfinityGhosts.add(InfinityGhost2);
                        AudioManager.play("ghostspawn");
                        break;
                    case 3:
                        Block InfinityGhost3 = new Block(InfinityGhostYImage, 9 * tileSize, 11 * tileSize, tileSize, tileSize, "ArmorY");
                        InfinityGhosts.add(InfinityGhost3);
                        AudioManager.play("ghostspawn");
                        break;

                }
            }
        }
    }

    public void lightGhostSpawner() {
        if(phase == 4){
            if(firstLightGhostSpawn){
                Block lighghostR1 = new Block(lightGhostRImage, 32 * 17, 32, tileSize, tileSize);
                lightGhosts.add(lighghostR1);
                Block lighghostR2 = new Block(lightGhostRImage, 32 * 17, 32, tileSize, tileSize);
                lightGhosts.add(lighghostR2);
                Block lighghostO1 = new Block(lightGhostOImage, 32, 19 * 32, tileSize, tileSize);
                lightGhosts.add(lighghostO1);
                Block lighghostO2 = new Block(lightGhostOImage, 32, 19 * 32, tileSize, tileSize);
                lightGhosts.add(lighghostO2);
                Block lighghostG1 = new Block(lightGhostGImage, 32 * 17, 32 * 19, tileSize, tileSize);
                lightGhosts.add(lighghostG1);
                Block lighghostG2 = new Block(lightGhostGImage, 32 * 17, 32 * 19, tileSize, tileSize);
                lightGhosts.add(lighghostG2);

                firstLightGhostSpawn = false;
            }


            int ghostspawnluck = random.nextInt(2000);
            switch (ghostspawnluck){
                case 21:
                    Block lighghostY1 = new Block(lightGhostYImage, 32, 32, tileSize, tileSize);
                    lightGhosts.add(lighghostY1);
                    AudioManager.play("ghostspawn");
                    break;
                case 7:
                    Block lighghostR1 = new Block(lightGhostRImage, 32 * 17, 32, tileSize, tileSize);
                    lightGhosts.add(lighghostR1);
                    AudioManager.play("ghostspawn");
                    break;
                case 10:
                    Block lighghostO2 = new Block(lightGhostGImage, 32 * 17, 32 * 19, tileSize, tileSize);
                    lightGhosts.add(lighghostO2);
                    AudioManager.play("ghostspawn");
                    break;
                case 17:
                    Block lighghostG1 = new Block(lightGhostGImage, 32 * 17, 32 * 19, tileSize, tileSize);
                    lightGhosts.add(lighghostG1);
                    AudioManager.play("ghostspawn");
                    break;
            }


        }
    }

    private void fruitSpawner() {
        if(foods.size() < 150 && PacMan.orangesLeft == 2){
            PacMan.orangesLeft = 1;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block orange = new Block(orangeImage, x * tileSize, y * tileSize, tileSize, tileSize);
            oranges.add(orange);
        }
        else if(foods.size() < 50 && PacMan.orangesLeft == 1){
            PacMan.orangesLeft = 0;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block orange = new Block(orangeImage, x * tileSize, y * tileSize, tileSize, tileSize);
            oranges.add(orange);
        }


        if(foods.size() < 180 && PacMan.cherrysLeft == 4){
            PacMan.cherrysLeft = 3;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
        else if(foods.size() < 120 && PacMan.cherrysLeft == 3){
            PacMan.cherrysLeft = 2;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
        else if(foods.size() < 100 && PacMan.cherrysLeft == 2){
            PacMan.cherrysLeft = 1;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
        else if(foods.size() < 30 && PacMan.cherrysLeft == 1){
            PacMan.cherrysLeft = 0;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
    }

    private void PacManMove() {
        PacMan.x += PacMan.velocityX;
        PacMan.y += PacMan.velocityY;

        if(PacMan.x == 0){
            PacMan.x = tileSize * columnCount;
        }
        else if(PacMan.x == tileSize * columnCount){
            PacMan.x = 0;
        }

        if(PacMan.isPacManAGhost){
            beingGhostCounter++;
        }
        if(beingGhostCounter == 200){
            PacMan.isPacManAGhost = false;
            beingGhostCounter = 0;
        }

        if(PacMan.gunshoted)
            PacMan.gunCoolDown++;

        if(PacMan.gunCoolDown > 60){
            PacMan.gunCoolDown = 0;
            PacMan.gunshoted = false;
        }
        if(PacMan.nextDirection != PacMan.direction){
            PacMan.updateDirection(PacMan.nextDirection);
            PacMan.PacManImage(PacMan.nextDirection, PacMan.isGunner);
        }

    }

    private void bulletMove() {
        for(Block bullet : guns){
            if(bullet.sname == "b"){
                bullet.x += bullet.velocityX;
                bullet.y += bullet.velocityY;

                if(bullet.x == 0){
                    bullet.x = tileSize * columnCount;
                }
                else if(bullet.x == tileSize * columnCount){
                    bullet.x = 0;
                }

            }
        }
    }

    private void gunShot(){
        if(phase == 3 || phase == 5) {
            if (PacMan.bulletCount > 0 && PacMan.isGunner && (PacMan.gunCoolDown > 30 || PacMan.gunCoolDown == 0)) {
                PacMan.gunshoted = true;
                AudioManager.play("gunShot");
                char direction = PacMan.direction;
                Block bullet;
                switch (direction) {
                    case 'L':
                        if(phase == 3){
                            bullet = new Block(bulletLImage, PacMan.x - tileSize, PacMan.y + 9, tileSize / 2, tileSize / 2, "b");
                            gunShotL++;
                        }
                        else{
                            bullet = new Block(scifiLBulletImage, PacMan.x - tileSize, PacMan.y + 9, tileSize / 2, tileSize / 2, "b");
                            sciShotL++;
                        }
                        guns.add(bullet);
                        bullet.updateDirection(direction);
                        PacMan.bulletCount--;
                        break;
                    case 'R':
                        if(phase == 3){
                            bullet = new Block(bulletRImage, PacMan.x + tileSize, PacMan.y + 9, tileSize / 2, tileSize / 2, "b");
                            gunShotR++;
                        }
                        else{
                            bullet = new Block(scifiRBulletImage, PacMan.x + tileSize, PacMan.y + 9, tileSize / 2, tileSize / 2, "b");
                            sciShotR++;
                        }
                        guns.add(bullet);
                        bullet.updateDirection(direction);
                        PacMan.bulletCount--;
                        break;
                    case 'U':
                        if(phase == 3){
                            bullet = new Block(bulletUImage, PacMan.x + 9, PacMan.y - tileSize, tileSize / 2, tileSize / 2, "b");
                            gunShotU++;
                        }
                        else{
                            bullet = new Block(scifiUBulletImage, PacMan.x + 9, PacMan.y - tileSize, tileSize / 2, tileSize / 2, "b");
                            sciShotU++;
                        }
                        guns.add(bullet);
                        bullet.updateDirection(direction);
                        PacMan.bulletCount--;
                        break;
                    case 'D':
                        if(phase == 3){
                            bullet = new Block(bulletDImage, PacMan.x + 7, PacMan.y + tileSize, tileSize / 2, tileSize / 2, "b");
                            gunShotD++;
                        }
                        else{
                            bullet = new Block(scifiDBulletImage, PacMan.x + 7, PacMan.y + tileSize, tileSize / 2, tileSize / 2, "b");
                            sciShotD++;
                        }
                        guns.add(bullet);
                        bullet.updateDirection(direction);
                        PacMan.bulletCount--;
                        break;
                }
            }
        }
    }

    private void phaseHandler() {
        if (foods.isEmpty()) {
            if (phase == 1){
                phase++;
                this.setBackground(new Color(30,30,0,200));
            }
            else if (phase == 2){
                phase++;
                imageSpawnerDelay = System.currentTimeMillis();
                PacMan.Block gun = new PacMan.Block(gunImage, ghosts.get(0).x, ghosts.get(0).y, tileSize, tileSize, "gun");
                PacMan.Block bullet = new PacMan.Block(bulletUImage, ghosts.get(1).x + 5, ghosts.get(1).y + 5, tileSize * 2/3, tileSize * 2/3, "bullet");
                PacMan.Block bullet2 = new PacMan.Block(bulletUImage, ghosts.get(2).x + 5, ghosts.get(2).y + 5, tileSize * 2/3, tileSize * 2/3, "bullet");
                PacMan.Block bullet3 = new PacMan.Block(bulletUImage, ghosts.get(3).x + 5, ghosts.get(3).y + 5, tileSize * 2/3, tileSize * 2/3, "bullet");
                guns.add(gun);
                guns.add(bullet);
                guns.add(bullet2);
                guns.add(bullet3);
                ghosts.remove(ghosts.get(0));
                ghosts.remove(ghosts.get(0));
                ghosts.remove(ghosts.get(0));
                ghosts.remove(ghosts.get(0));
                loadMap2();
            }
            else if (phase == 3) {
                resetPositions();
                this.setBackground(new Color(0,0,0,200));
                guns.clear();
                imageScheduler = Executors.newScheduledThreadPool(1);
                imageScheduler.scheduleAtFixedRate(() -> {
                    currentWallImageIndex = (currentWallImageIndex + 1) % wallImages.length;
                    wallph3Image = wallImages[currentWallImageIndex];
                    loadMap3();
                    repaint();
                    imagePh++;
                }, 0, 5, TimeUnit.SECONDS);
                if(snake.isEmpty() && snake2.isEmpty()) {
                    guns.clear();
                    imageSpawnerDelay = System.currentTimeMillis();
                    PacMan.PacManImage(PacMan.direction, PacMan.isGunner);
                    PacMan.isGunner = false;
                    PacMan.bulletCount = 0;
                }
                else{
                    snake.clear();
                    snake2.clear();
                    gameLoop.stop();
                    guns.clear();
                    App.win(App.frame , score , phase);
                    App.user.setLastScore(score);
                    if(score > App.user.getHighScore()){
                        App.user.setHighScore(score);
                    }
                    App.db.updateGameUserById(App.user);
                    resetGame();
                }
            }
            else if (phase == 4){
                gameLoop.stop();
                gameOver = true;
            }
            else if (phase == 6){
                loadMapW();
                AudioManager.stopLooping("ph3");
            }

            if(portalHealth == 76){
                phase5StartTime = 0;
                portalHealth = 75;
                shakeWindow(App.frame, 15000, 10);
            }
            else if(portalHealth == 51){
                phase5StartTime = 0;
                portalHealth = 50;
                shakeWindow(App.frame, 15000, 10);
            }
            else if(portalHealth == 26){
                phase5StartTime = 0;
                portalHealth = 25;
                shakeWindow(App.frame, 15000, 10);
            }
        }

        if(phase == 3){
            firstphase2image = false;
            secondphase2image = false;
            if(snake.isEmpty() && snake2.isEmpty() && System.currentTimeMillis() - imageSpawnerDelay < 12000){
                phasechanger = true;
                secondphase2image = true;
                if(ph22play) {
                    AudioManager.play("deafeated");
                    ph22play = false;
                }
            }
            else if(System.currentTimeMillis() - imageSpawnerDelay < 6000){
                firstphase2image = true;
                if(ph21play) {
                    AudioManager.play("deafeat");
                    ph21play = false;
                }
            }


        }

        if(phasechanger && !secondphase2image && phase == 3){
            phase++;
            loadMap3();
            AudioManager.playLooping("ph3");
            phasechanger = false;
        }


        if(phase == 4 && lightGhosts.isEmpty()){
            phase++;
            AudioManager.play("portalspawn");
            foods.clear();
            ghostFruits.clear();
            PacMan.isGunner = true;
            shakeWindow(App.frame, 15000, 10);
            AudioManager.play("earthquick");
            AudioManager.stopLooping("ph3");
            imageScheduler.close();
            wallph3Image = wallImages[1];
            loadMap3();
            repaint();
            phase5StartTime = System.currentTimeMillis();
            ArrayList<PacMan.Block> toRemove = new ArrayList<>();
            for(PacMan.Block b : walls){
                if(b.sname == "g"){
                    toRemove.add(b);
                }
            }
            walls.removeAll(toRemove);
        }

        if(winpicture && winpictureDelay == 0){
            winpicture = false;
            winpictureDelay = System.currentTimeMillis();
        }
        else if(System.currentTimeMillis() - winpictureDelay > 3000 && winpictureDelay > 1){
            winpicture = true;
            winpictureDelay = 1;
        }
        else if(winpicture && winpictureDelay == 1){
            gameLoop.stop();
            App.win(App.frame , score , phase);
            resetGame();
        }


        if(phase == 5 && portalHealth <= 0){
            InfinityGhosts.clear();
            AudioManager.stopLooping("ph3");

            /*App.user.setLastScore(score);
            if(score > App.user.getHighScore()){
                App.user.setHighScore(score);
            }
            App.db.updateGameUserById(App.user);*/
            if(!phasechanger) {
                phase5to6 = System.currentTimeMillis();
                phasechanger = true;
                AudioManager.play("explosion");
            }

            if(System.currentTimeMillis() - phase5to6 >= 3000) {
                resetPositions();
                phase++;
                AudioManager.playLooping("winsong");
            }
        }

        if(phase == 5){
            Block bulletdestroy = null;
            for(Block gun : guns){
                if(collision(gun, InfinityPortal) && gun.sname == "b"){
                    collision2(gun, InfinityPortal);
                    sciBulletDest++;
                    portalHealth -= 25;
                    bulletdestroy = gun;
                }
            }
            guns.remove(bulletdestroy);
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void collision2(Block a, Block b) {
        bulletPositions.add(new Position(b.x, b.y, a.direction));
    }

    public void checkCollision() {
        for (Block wall : walls) {
            if (collision(PacMan, wall)) {
                PacMan.x -= PacMan.velocityX;
                PacMan.y -= PacMan.velocityY;
                break;
            }
        }
        snake();
        ghost();
        lightGhost();

        Block cherryEaten = null;
        for (Block cherry : cherrys) {
            if (collision(PacMan, cherry) && phase < 6) {
                AudioManager.play("fruitEaten");
                cherryEaten = cherry;
                score += 100;
            }
            if (collision(PacMan, cherry) && phase == 6) {
                if(cherry.sname == "K") {
                    cherryEaten = cherry;
                    pacmanK = true;
                    pacmanExit = true;
                    PacMan.PacManImage(PacMan.direction, PacMan.isGunner);
                }
                else if(pacmanExit){
                    winpicture = true;

                }
            }
            
        }
        cherrys.remove(cherryEaten);

        Block orangeEaten = null;
        for (Block orange : oranges) {
            if (collision(PacMan, orange) && phase < 6) {
                mainSound = false;
                AudioManager.play("scaryGhostTime");
                orangeEaten = orange;
                for(Block ghost : ghosts){
                    ghost.setVulnerable = true;
                    ghost.image = scaredGhostImage;
                }
            }
        }
        oranges.remove(orangeEaten);

        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(PacMan, food) && (phase != 4 || PacMan.isPacManAGhost)) {
                if(foodEatingSound) {
                    AudioManager.play("foodEating1");
                    foodEatingSound = false;
                }
                else {
                    AudioManager.play("foodEating2");
                    foodEatingSound = true;
                }
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);


        Block gunTaken = null;
        Block bulletDestroyed = null;
        if(PacMan.isGunner && !PacMan.gunLoaded){
            AudioManager.playNTimes("loadGun", PacMan.bulletCount);
            PacMan.gunLoaded = true;
        }
        for(Block gun: guns){
            if(collision(PacMan, gun)){
                if(gun.sname == "gun"){
                    PacMan.isGunner = true;
                    PacMan.PacManImage(PacMan.direction, PacMan.isGunner);
                }
                else if(gun.sname == "bullet"){
                    PacMan.bulletCount += 1;
                    if(PacMan.isGunner){
                        AudioManager.play("loadGun");
                    }

                }
                gunTaken = gun;
            }

            for(Block wall : walls){
                if(collision(gun, wall) && phase == 5){
                    sciBulletDest++;
                    collision2(gun, wall);
                    bulletDestroyed = gun;
                }
                if(collision(gun, wall) && phase == 3){
                    bulletDest++;
                    collision2(gun, wall);
                    bulletDestroyed = gun;
                }
            }

            for(Block snake : snake){
                if(collision(gun, snake)){
                    if(gun.sname == "b") {
                        bulletDest++;
                        collision2(gun, snake);
                        bulletDestroyed = gun;
                        if(snake.sname == "h"){
                            snake.slife -= 3;
                        }
                        else{
                            snake.slife--;
                        }
                    }
                }
            }

            for(Block snake : snake2){
                if(collision(gun, snake)){
                    if(gun.sname == "b") {
                        bulletDest++;
                        collision2(gun, snake);
                        bulletDestroyed = gun;
                        if(snake.sname == "h"){
                            snake.slife2 -= 3;
                        }
                        else{
                            snake.slife2--;
                        }
                    }
                }
            }

        }
        guns.remove(gunTaken);
        guns.remove(bulletDestroyed);


    }

    public void setSnakeImage(char direction, Block snake){
        if(snake.sname.equals("h")){
            if(direction == 'U'){
                snake.image = snakeheadUImage;
            }
            else if(direction == 'D'){
                snake.image = snakeheadDImage;
            }
            else if(direction == 'L'){
                snake.image = snakeheadLImage;
            }
            else if(direction == 'R'){
                snake.image = snakeheadRImage;
            }
        }
        else if(snake.sname.equals("b")){
            if(snake.isTurning) {
                if ((snake.previousDirection == 'U' && snake.direction == 'R') ||
                        (snake.previousDirection == 'L' && snake.direction == 'D')) {
                    snake.image = snaketurnImage4;
                } else if ((snake.previousDirection == 'U' && snake.direction == 'L') ||
                        (snake.previousDirection == 'R' && snake.direction == 'D')) {
                    snake.image = snaketurnImage1;
                } else if ((snake.previousDirection == 'D' && snake.direction == 'R') ||
                        (snake.previousDirection == 'L' && snake.direction == 'U')) {
                    snake.image = snaketurnImage3;
                } else if ((snake.previousDirection == 'D' && snake.direction == 'L') ||
                        (snake.previousDirection == 'R' && snake.direction == 'U')) {
                    snake.image = snaketurnImage2;
                }
            }
            else if ((snake.direction == 'U' || snake.direction == 'D') && snake.turnTimer == 0){
                snake.image = snakebodyVImage;
            }
            else if ((snake.direction == 'L' || snake.direction == 'R') && snake.turnTimer == 0){
                snake.image = snakebodyHImage;
            }
        }
        else if(snake.sname.equals("e")){
            if(direction == 'D'){
                snake.image = snakeendUImage;
            }
            else if(direction == 'U'){
                snake.image = snakeendDImage;
            }
            else if(direction == 'L'){
                snake.image = snakeendLImage;
            }
            else if(direction == 'R'){
                snake.image = snakeendRImage;
            }
        }

    }

    public void snake() {
        ArrayList<Block> snakes = (ArrayList<Block>) snake.clone();
        Collections.reverse(snakes);
        ArrayList<Block> snakes2 = (ArrayList<Block>) snake2.clone();
        Collections.reverse(snakes2);
        for (int i = 0; i < snakes.size(); i++) {
            Block snake = snakes.get(i);
            if (snake.sname == "h") {
                if (collision(snake, PacMan)) {
                    lives -= 1;
                    if (lives == 0) {
                        pacmanKilled++;
                        gameOver = true;
                        return;
                    }

                }
                snake.x += snake.velocityX * 0.75;
                snake.y += snake.velocityY * 0.75;

                for (Block wall : walls) {
                    if (collision(snake, wall) || snake.x <= 0 || snake.x + snake.width >= boardWidth) {
                        snake.x -= snake.velocityX;
                        snake.y -= snake.velocityY;
                        if (PacMan.x >= snake.x && PacMan.y >= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (PacMan.x >= snake.x && PacMan.y <= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (PacMan.x <= snake.x && PacMan.y >= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (PacMan.x <= snake.x && PacMan.y <= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        }
                    } else if (random.nextInt(4) == 0) {
                        List<Character> possibleDirections = new ArrayList<>();

                        if (canMove(snake, wall, 'R')) possibleDirections.add('R');
                        if (canMove(snake, wall, 'L')) possibleDirections.add('L');
                        if (canMove(snake, wall, 'U')) possibleDirections.add('U');
                        if (canMove(snake, wall, 'D')) possibleDirections.add('D');

                        if (!possibleDirections.isEmpty()) {
                            char newDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                            if (newDirection != oppositeDirection(snake.direction)) {
                                previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                                if (previousPositions.size() > 100) {
                                    previousPositions.remove(0);
                                }
                                snake.updateDirection(newDirection);
                            }
                        }
                    } else {
                        previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                    }

                }
                setSnakeImage(snake.direction, snake);
            } else {

                if (collision(snake, PacMan)) {
                    lives -= 1;
                    if (lives == 0) {
                        pacmanKilled++;
                        gameOver = true;
                        return;
                    }

                }

                if (snake.previousDirection != snake.direction) {
                    snake.turnTimer = 0;
                    snake.isTurning = true;
                    snake.previousDirection = snake.direction;
                }

                if (snake.turnTimer != 0)
                    snake.turnTimer--;

                if (snake.turnTimer == 0) {
                    snake.isTurning = false;
                }


                if (previousPositions.size() > 30200) {
                    int delay = 1500;
                    int posIndex = previousPositions.size() - 1 - (i * delay);
                    if (posIndex >= 0 && posIndex < previousPositions.size()) {
                        Position pos = previousPositions.get(posIndex);
                        snake.x = pos.x;
                        snake.y = pos.y;
                        snake.direction = pos.direction;
                        setSnakeImage(snake.direction, snake);
                    }
                    for (int j = 0; j < 100; j++) {
                        previousPositions.remove(0);
                    }

                } else {
                    int delay = 1500;
                    int posIndex = previousPositions.size() - 1 - (i * delay);
                    if (posIndex >= 0 && posIndex < previousPositions.size()) {
                        Position pos = previousPositions.get(posIndex);
                        snake.previousDirection = snake.direction;
                        snake.x = pos.x;
                        snake.y = pos.y;
                        snake.direction = pos.direction;
                        setSnakeImage(snake.direction, snake);
                    }
                }
            }

        }

        if (snake.size() > 0 && snake.get(0).slife <= 0) {
            destroySnake();
        }

        if (snake2.size() > 0 && snake2.get(0).slife2 <= 0) {
            destroySnake2();
        }

        for (int i = 0; i < snakes2.size(); i++) {
            Block snake = snakes2.get(i);
            if (snake.sname == "h") {
                if (collision(snake, PacMan)) {
                    lives -= 1;
                    if (lives == 0) {
                        pacmanKilled++;
                        gameOver = true;
                        return;
                    }

                }

                snake.x += snake.velocityX * 0.75;
                snake.y += snake.velocityY * 0.75;

                for (Block wall : walls) {
                    if (collision(snake, wall) || snake.x <= 0 || snake.x + snake.width >= boardWidth) {
                        snake.x -= snake.velocityX;
                        snake.y -= snake.velocityY;
                        if (PacMan.x >= snake.x && PacMan.y >= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (PacMan.x >= snake.x && PacMan.y <= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (PacMan.x <= snake.x && PacMan.y >= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (PacMan.x <= snake.x && PacMan.y <= snake.y) {
                            char newDirection = directions00[random.nextInt(4)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        }
                    } else if (random.nextInt(4) == 0) {
                        List<Character> possibleDirections = new ArrayList<>();

                        if (canMove(snake, wall, 'R')) possibleDirections.add('R');
                        if (canMove(snake, wall, 'L')) possibleDirections.add('L');
                        if (canMove(snake, wall, 'U')) possibleDirections.add('U');
                        if (canMove(snake, wall, 'D')) possibleDirections.add('D');

                        if (!possibleDirections.isEmpty()) {
                            char newDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                            if (newDirection != oppositeDirection(snake.direction)) {
                                previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                                if (previousPositions2.size() > 100) {
                                    previousPositions2.remove(0);
                                }
                                snake.updateDirection(newDirection);
                            }
                        }
                    } else {
                        previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                    }

                }
                setSnakeImage(snake.direction, snake);
            } else {
                if (collision(snake, PacMan)) {
                    lives -= 1;
                    if (lives == 0) {
                        pacmanKilled++;
                        gameOver = true;
                        return;
                    }

                }
                if (snake.previousDirection != snake.direction) {
                    snake.turnTimer = 0;
                    snake.isTurning = true;
                    snake.previousDirection = snake.direction;
                }

                if (snake.turnTimer != 0)
                    snake.turnTimer--;

                if (snake.turnTimer == 0) {
                    snake.isTurning = false;
                }


                if (previousPositions2.size() > 30200) {
                    int delay = 1500;
                    int posIndex = previousPositions2.size() - 1 - (i * delay);
                    if (posIndex >= 0 && posIndex < previousPositions2.size()) {
                        Position pos = previousPositions2.get(posIndex);
                        snake.x = pos.x;
                        snake.y = pos.y;
                        snake.direction = pos.direction;
                        setSnakeImage(snake.direction, snake);
                    }
                    for (int j = 0; j < 100; j++) {
                        previousPositions2.remove(0);
                    }

                } else {
                    int delay = 1500;
                    int posIndex = previousPositions2.size() - 1 - (i * delay);
                    if (posIndex >= 0 && posIndex < previousPositions2.size()) {
                        Position pos = previousPositions2.get(posIndex);
                        snake.previousDirection = snake.direction;
                        snake.x = pos.x;
                        snake.y = pos.y;
                        snake.direction = pos.direction;
                        setSnakeImage(snake.direction, snake);
                    }
                }
            }

        }
    }


    public void destroySnake(){
        AudioManager.play("deadSnake");
        for(int i = 0; i < snake.size(); i++){
            snake.remove(i);
        }
    }

    public void destroySnake2(){
        AudioManager.play("deadSnake");
        for(int i = 0; i < snake2.size(); i++){
            snake2.remove(i);
        }
    }

    public void ghost(){
        for (Block ghost : ghosts) {


            if (collision(ghost, PacMan)) {
                if(!ghost.setVulnerable) {
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                }
                else{
                    if (collision(ghost, PacMan)) {
                        AudioManager.play("ghostEaten");
                        score += 400;
                        ghost.x = ghost.startX;
                        ghost.y = ghost.startY;
                        ghost.setVulnerable = false;
                        if(ghost.name == 'o'){
                            ghost.image = orangeGhostImage;
                        }
                        else if(ghost.name == 'b'){
                            ghost.image = blueGhostImage;
                        }
                        else if(ghost.name == 'r'){
                            ghost.image = redGhostImage;
                        }
                        else if(ghost.name == 'p'){
                            ghost.image = pinkGhostImage;
                        }
                    }
                }
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    if(PacMan.x >= ghost.x && PacMan.y >= ghost.y){
                        char newDirection = directions3[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                    else if(PacMan.x >= ghost.x && PacMan.y <= ghost.y){
                        char newDirection = directions[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                    else if(PacMan.x <= ghost.x && PacMan.y >= ghost.y){
                        char newDirection = directions2[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                    else if(PacMan.x <= ghost.x && PacMan.y <= ghost.y){
                        char newDirection = directions1[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                }
                else if (random.nextInt(30) == 0) {
                    List<Character> possibleDirections = new ArrayList<>();

                    if (canMove(ghost, wall, 'R')) possibleDirections.add('R');
                    if (canMove(ghost, wall, 'L')) possibleDirections.add('L');
                    if (canMove(ghost, wall, 'U')) possibleDirections.add('U');
                    if (canMove(ghost, wall, 'D')) possibleDirections.add('D');

                    if (!possibleDirections.isEmpty()) {
                        char newDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                        if (newDirection != oppositeDirection(ghost.direction)) {
                            ghost.updateDirection(newDirection);
                        }
                    }
                }

            }
        }
    }

    public void lightGhost() {
        List<Block> toRemove = new ArrayList<>();

        for (Block lightGhost : lightGhosts) {

            if (collision(lightGhost, PacMan)) {
                toRemove.add(lightGhost);
                AudioManager.play("ghostEaten");
                score += 800;
                collision2(PacMan, lightGhost);
                ghostKilled++;
            }

            int ghostnum = 0;
            boolean collision = false;
            int x = 0, y = 0;
            char direction = 'U';
            for(Block ghostFruit : ghostFruits){
                if (collision(lightGhost, ghostFruit) && lightGhosts.size() > 1) {
                    PacMan.isPacManAGhost = true;
                    int numberofghosts = lightGhosts.size();
                    ghostnum = random.nextInt(numberofghosts);
                    PacMan.Block chosenGhost = lightGhosts.get(ghostnum);
                    x = PacMan.x;
                    y = PacMan.y;
                    direction = PacMan.direction;
                    PacMan.x = chosenGhost.x;
                    PacMan.y = chosenGhost.y;
                    PacMan.image = chosenGhost.image;
                    PacMan.direction = chosenGhost.direction;
                    collision = true;
                }
            }
            if(collision){
                ghostFruits.clear();
                lightGhosts.get(ghostnum).x = x;
                lightGhosts.get(ghostnum).y = y;
                lightGhosts.get(ghostnum).direction = direction;
            }




            Iterator<PacMan.Block> foodIterator = foods.iterator();
            while (foodIterator.hasNext()) {
                PacMan.Block food = foodIterator.next();
                if (collision(lightGhost, food)) {
                    foodIterator.remove();
                    break;
                }
            }

            lightGhost.x += lightGhost.velocityX;
            lightGhost.y += lightGhost.velocityY;

            for (PacMan.Block wall : new ArrayList<>(walls)) { // احتیاط
                if (collision(lightGhost, wall) || lightGhost.x <= 0 || lightGhost.x + lightGhost.width >= boardWidth) {
                    lightGhost.x -= lightGhost.velocityX;
                    lightGhost.y -= lightGhost.velocityY;

                    char newDirection;
                    if (PacMan.x >= lightGhost.x && PacMan.y >= lightGhost.y) {
                        newDirection = directions[random.nextInt(10)];
                    } else if (PacMan.x >= lightGhost.x && PacMan.y <= lightGhost.y) {
                        newDirection = directions3[random.nextInt(10)];
                    } else if (PacMan.x <= lightGhost.x && PacMan.y >= lightGhost.y) {
                        newDirection = directions1[random.nextInt(10)];
                    } else {
                        newDirection = directions2[random.nextInt(10)];
                    }
                    lightGhost.updateDirection(newDirection);
                }
                else if (random.nextInt(30) == 0) {
                    List<Character> possibleDirections = new ArrayList<>();
                    if (canMove(lightGhost, wall, 'R')) possibleDirections.add('R');
                    if (canMove(lightGhost, wall, 'L')) possibleDirections.add('L');
                    if (canMove(lightGhost, wall, 'U')) possibleDirections.add('U');
                    if (canMove(lightGhost, wall, 'D')) possibleDirections.add('D');

                    if (!possibleDirections.isEmpty()) {
                        char newDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                        if (newDirection != oppositeDirection(lightGhost.direction)) {
                            lightGhost.updateDirection(newDirection);
                        }
                    }
                }
            }
        }

        lightGhosts.removeAll(toRemove);
    }

    public void InfinityGhost(){
        Block destroyinfinity = null;
        for (Block InfinityGhost : InfinityGhosts) {
            Block destroybullet = null;
            for(Block gun : guns){
                if(collision(InfinityGhost, gun) && gun.sname == "b"){
                    sciBulletDest++;
                    collision2(gun, InfinityGhost);
                    destroybullet = gun;
                    if(InfinityGhost.sname == "ArmorY"){
                        InfinityGhost.sname = "Y";
                        InfinityGhost.image = InfinityGhostYNImage;
                        AudioManager.play("armor");

                    }
                    else if (InfinityGhost.sname == "ArmorG"){
                        InfinityGhost.sname = "G";
                        InfinityGhost.image = InfinityGhostGNImage;
                        AudioManager.play("armor");
                    }
                    else if (InfinityGhost.sname == "ArmorP"){
                        InfinityGhost.sname = "P";
                        InfinityGhost.image = InfinityGhostPNImage;
                        AudioManager.play("armor");
                    }
                    else if (InfinityGhost.sname == "ArmorR"){
                        InfinityGhost.sname = "R";
                        InfinityGhost.image = InfinityGhostRNImage;
                        AudioManager.play("armor");
                    }
                    else{
                        destroyinfinity = InfinityGhost;
                        AudioManager.play("ghostEaten");

                    }
                }
            }
            guns.remove(destroybullet);

            //if(lives == 0){
            //    pacmanKilled++;
              //  gameOver = true;
            //}

            if (collision(InfinityGhost, PacMan)) {
                if(InfinityGhost.sname.length() != 1){
                    lives--;
                }
            }
            InfinityGhost.x += InfinityGhost.velocityX;
            InfinityGhost.y += InfinityGhost.velocityY;
            for (Block wall : walls) {
                if (collision(InfinityGhost, wall) || InfinityGhost.x <= 0 || InfinityGhost.x + InfinityGhost.width >= boardWidth) {
                    InfinityGhost.x -= InfinityGhost.velocityX;
                    InfinityGhost.y -= InfinityGhost.velocityY;
                    if(PacMan.x >= InfinityGhost.x && PacMan.y >= InfinityGhost.y){
                        char newDirection = directions3[random.nextInt(10)];
                        InfinityGhost.updateDirection(newDirection);
                    }
                    else if(PacMan.x >= InfinityGhost.x && PacMan.y <= InfinityGhost.y){
                        char newDirection = directions[random.nextInt(10)];
                        InfinityGhost.updateDirection(newDirection);
                    }
                    else if(PacMan.x <= InfinityGhost.x && PacMan.y >= InfinityGhost.y){
                        char newDirection = directions2[random.nextInt(10)];
                        InfinityGhost.updateDirection(newDirection);
                    }
                    else if(PacMan.x <= InfinityGhost.x && PacMan.y <= InfinityGhost.y){
                        char newDirection = directions1[random.nextInt(10)];
                        InfinityGhost.updateDirection(newDirection);
                    }
                }
                else if (random.nextInt(30) == 0) {
                    List<Character> possibleDirections = new ArrayList<>();

                    if (canMove(InfinityGhost, wall, 'R')) possibleDirections.add('R');
                    if (canMove(InfinityGhost, wall, 'L')) possibleDirections.add('L');
                    if (canMove(InfinityGhost, wall, 'U')) possibleDirections.add('U');
                    if (canMove(InfinityGhost, wall, 'D')) possibleDirections.add('D');

                    if (!possibleDirections.isEmpty()) {
                        char newDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                        if (newDirection != oppositeDirection(InfinityGhost.direction)) {
                            InfinityGhost.updateDirection(newDirection);
                        }
                    }
                }

            }
        }
        InfinityGhosts.remove(destroyinfinity);
    }

    public static void shakeWindow(JFrame frame, int durationMs, int intensity) {
        Point originalLocation = frame.getLocation();
        Random random = new Random();

        new Thread(() -> {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < durationMs) {
                int x = originalLocation.x + random.nextInt(intensity * 2 + 1) - intensity;
                int y = originalLocation.y + random.nextInt(intensity * 2 + 1) - intensity;
                frame.setLocation(x, y);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            frame.setLocation(originalLocation);
        }).start();
    }

    public void resetPositions() {
        PacMan.reset();
        PacMan.velocityX = 0;
        PacMan.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    public void resetGame(){
        score = 0;
        lives = 5;
        phase = 1;
    }

    public void changePacManMusic() {
        System.out.println(mainSound);
        if(mainSound) {
            if (foods.size() > 30 && mainMusicCounter == 0) {
                AudioManager.playLooping("normalMove");
                mainMusicCounter = 1;
            }
            else if (foods.size() <= 30 && foods.size() > 20 && mainMusicCounter == 1) {
                AudioManager.changeMusic("normalMove" , "spurtMove1");
                mainMusicCounter = 2;
            }
            else if (foods.size() <= 20 && foods.size() > 12 && mainMusicCounter == 2) {
                AudioManager.changeMusic("spurtMove1" , "spurtMove2");
                mainMusicCounter = 3;
            }
            else if (foods.size() <= 12 && foods.size() > 5 && mainMusicCounter == 3) {
                AudioManager.changeMusic("spurtMove2" , "spurtMove3");
                mainMusicCounter = 4;
            }
            else if (foods.size() <= 5 && mainMusicCounter == 4) {
                AudioManager.changeMusic("spurtMove3" , "spurtMove4");
                mainMusicCounter = 5;
            }
            else if (foods.size() == 0){
                AudioManager.stop("spurtMove4");
            }
        }
    }

    public void ghostValnrability(){
        for(Block ghost : ghosts) {
            if(ghost.setVulnerable){
                counter += 1;
                break;
            }
        }

        if(counter == 240) {
            for(Block ghost : ghosts) {
                if(ghost.name == 'o'){
                    ghost.image = orangeGhostImage;
                }
                else if(ghost.name == 'b'){
                    ghost.image = blueGhostImage;
                }
                else if(ghost.name == 'r'){
                    ghost.image = redGhostImage;
                }
                else if(ghost.name == 'p'){
                    ghost.image = pinkGhostImage;
                }
                ghost.setVulnerable = false;

            }
            AudioManager.stop("scaryGhostTime");
            mainSound = true;
            counter = 0;
        }

    }

    public void resume(){
        gameLoop.start();
        running = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //changePacManMusic();
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            App.GameOver(App.frame , score);
            //App.user.setLastScore(score);
            if(score > App.user.getHighScore()){
                App.user.setHighScore(score);
            }
            //App.db.updateGameUserById(App.user);
            resetGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            PacMan.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            PacMan.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            PacMan.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            PacMan.updateDirection('R');
        }
        else if(e.getKeyCode() == KeyEvent.VK_P){
            if(running){
                gameLoop.stop();
                running = false;
            }
            else{
                gameLoop.start();
                running = true;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_E){
            gunShot();
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            if(running){
                gameLoop.stop();
                running = false;
                App.showPause(App.frame);
                res++;
                if(res == 2){
                    App.resume2();
                    res = 0;
                }
            }
            else{
                resume();
            }
        }


        PacMan.PacManImage(PacMan.direction, PacMan.isGunner);
    }

    @Override
    public void keyReleased(KeyEvent e) {

        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            PacMan.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            PacMan.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            PacMan.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            PacMan.updateDirection('R');
        }
        PacMan.PacManImage(PacMan.direction, PacMan.isGunner);
    }
}