//import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PacmanOnline extends JPanel implements ActionListener {

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
        static int slife = 20;
        static int slife2 = 20;

        int orangesLeft = 2;
        int cherrysLeft = 4;

        boolean setVulnerable = false;

        boolean isTurning = false;
        int turnTimer = 0;
        static boolean isInPosition = false;

        boolean isGunner;
        boolean isBulletCounting = false;
        int bulletCount;
        boolean gunLoaded;
        int bulletTimer;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
            this.isGunner = false;
            this.bulletCount = 1000000;
            this.gunLoaded = false;
            this.bulletTimer = 10000;
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
            if (direction == this.direction) {
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

            if (!collisionCheck) {
                nextDirection = direction;
            }

        }

        void updateVelocity() {
            if (this.sname == "b") {
                if (this.direction == 'U') {
                    this.velocityX = 0;
                    this.velocityY = -tileSize / 4;
                } else if (this.direction == 'D') {
                    this.velocityX = 0;
                    this.velocityY = tileSize / 4;
                } else if (this.direction == 'L') {
                    this.velocityX = -tileSize / 4;
                    this.velocityY = 0;
                } else if (this.direction == 'R') {
                    this.velocityX = tileSize / 4;
                    this.velocityY = 0;
                }
            } else {
                if (this.direction == 'U') {
                    this.velocityX = 0;
                    this.velocityY = -tileSize / 8;
                } else if (this.direction == 'D') {
                    this.velocityX = 0;
                    this.velocityY = tileSize / 8;
                } else if (this.direction == 'L') {
                    this.velocityX = -tileSize / 8;
                    this.velocityY = 0;
                } else if (this.direction == 'R') {
                    this.velocityX = tileSize / 8;
                    this.velocityY = 0;
                }
            }
        }

        public void pacmanImage(char direction, boolean gunner) {
            if (isPacmanCanMove(pacman, direction) && phase == 1) {
                if (direction == 'U') {
                    pacman.image = pacmanUpImage;
                } else if (direction == 'D') {
                    pacman.image = pacmanDownImage;
                } else if (direction == 'L') {
                    pacman.image = pacmanLeftImage;
                } else if (direction == 'R') {
                    pacman.image = pacmanRightImage;
                }
            } else if (isPacmanCanMove(pacman, direction) && phase == 3) {
                if (direction == 'U') {
                    if (!gunner)
                        pacman.image = pacmanUpImage;
                    else
                        pacman.image = pacmanGUpImage;
                } else if (direction == 'D') {
                    if (!gunner)
                        pacman.image = pacmanDownImage;
                    else
                        pacman.image = pacmanGDownImage;
                } else if (direction == 'L') {
                    if (!gunner)
                        pacman.image = pacmanLeftImage;
                    else
                        pacman.image = pacmanGLeftImage;
                } else if (direction == 'R') {
                    if (!gunner)
                        pacman.image = pacmanRightImage;
                    else
                        pacman.image = pacmanGRightImage;
                }
            } else if (phase == 4 && isPacmanCanMove(pacman, direction)) {
                if (direction == 'U') {
                    pacman.image = pacmanSciUImage;
                } else if (direction == 'D') {
                    pacman.image = pacmanSciDImage;
                } else if (direction == 'L') {
                    pacman.image = pacmanSciLImage;
                } else if (direction == 'R') {
                    pacman.image = pacmanSciRImage;
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
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    private int counter = 0;

    private Image wallImage;
    private Image wallRPImage;
    private Image wallph2Image;
    private Image wallph3Image;

    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image scaredGhostImage;
    private Image lightGhostYImage;
    private Image lightGhostOImage;
    private Image lightGhostGImage;
    private Image lightGhostRImage;

    private Image cherryImage;
    private Image orangeImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private Image pacmanGUpImage;
    private Image pacmanGDownImage;
    private Image pacmanGLeftImage;
    private Image pacmanGRightImage;
    private Image pacmanSciUImage;
    private Image pacmanSciRImage;
    private Image pacmanSciDImage;
    private Image pacmanSciLImage;

    private Image gunImage;
    private Image bulletUImage;
    private Image bulletDImage;
    private Image bulletRImage;
    private Image bulletLImage;

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


    boolean foodEatingSound = true;
    boolean mainSound = true;
    int mainMusicCounter = 0;

    AdvancedPlayer player;
    Thread musicThread;
    boolean isPlaying = false;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
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
            "X              X  X",
            "X XXXXX X XX X X XX",
            "X       X r  X    X",
            "XXX XXX XXXX XXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    ArrayList<Block> walls;
    HashSet<Block> foods;
    ArrayList<Block> ghosts;
    HashSet<Block> scaredGhosts;
    ArrayList<Block> lightGhosts;
    HashSet<Block> cherrys;
    HashSet<Block> oranges;
    ArrayList<Block> snake;
    ArrayList<Block> snake2;
    ArrayList<Block> guns;
    Block pacman;

    Timer gameLoop;
    boolean running = true;
    // Ai Direction
    char[] directions = {'U', 'U', 'U', 'D', 'L', 'L', 'L', 'R', 'L', 'U'};
    char[] directions1 = {'U', 'U', 'U', 'U', 'R', 'R', 'L', 'D', 'R', 'R'};
    char[] directions2 = {'D', 'D', 'D', 'D', 'L', 'L', 'R', 'U', 'L', 'L'};
    char[] directions3 = {'D', 'D', 'D', 'U', 'L', 'R', 'R', 'D', 'R', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 1000;
    int phase = 1;
    boolean gameOver = false;
    java.util.List<Position> previousPositions = new ArrayList<>();
    java.util.List<Position> previousPositions2 = new ArrayList<>();


    public PacmanOnline() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon("./Media/Images/wall.png").getImage();
        wallRPImage = new ImageIcon("./Media/Images/wallrp.png").getImage();
        wallph2Image = new ImageIcon("./Media/Images/wallph2Image.png").getImage();
        wallph3Image = new ImageIcon("./Media/Images/wallph3Image.png").getImage();


        blueGhostImage = new ImageIcon("./Media/Images/blueGhost.png").getImage();
        orangeGhostImage = new ImageIcon("./Media/Images/orangeGhost.png").getImage();
        pinkGhostImage = new ImageIcon("./Media/Images/pinkGhost.png").getImage();
        redGhostImage = new ImageIcon("./Media/Images/redGhost.png").getImage();
        scaredGhostImage = new ImageIcon("./Media/Images/scaredGhost.png").getImage();
        lightGhostYImage = new ImageIcon("./Media/Images/lightGhostY.png").getImage();
        lightGhostOImage = new ImageIcon("./Media/Images/lightGhostO.png").getImage();
        lightGhostRImage = new ImageIcon("./Media/Images/lightGhostR.png").getImage();
        lightGhostGImage = new ImageIcon("./Media/Images/lightGhostG.png").getImage();


        cherryImage = new ImageIcon("./Media/Images/cherry.png").getImage();
        orangeImage = new ImageIcon("./Media/Images/orange.png").getImage();

        pacmanUpImage = new ImageIcon("./Media/Images/pacmanUp.png").getImage();
        pacmanDownImage = new ImageIcon("./Media/Images/pacmanDown.png").getImage();
        pacmanLeftImage = new ImageIcon("./Media/Images/pacmanLeft.png").getImage();
        pacmanRightImage = new ImageIcon("./Media/Images/pacmanRight.png").getImage();
        pacmanGUpImage = new ImageIcon("./Media/Images/pacmangunu.png").getImage();
        pacmanGDownImage = new ImageIcon("./Media/Images/pacmangund.png").getImage();
        pacmanGLeftImage = new ImageIcon("./Media/Images/pacmangunl.png").getImage();
        pacmanGRightImage = new ImageIcon("./Media/Images/pacmangunr.png").getImage();
        pacmanSciUImage = new ImageIcon("./Media/Images/pacsciu.png").getImage();
        pacmanSciDImage = new ImageIcon("./Media/Images/pacscid.png").getImage();
        pacmanSciLImage = new ImageIcon("./Media/Images/pacscil.png").getImage();
        pacmanSciRImage = new ImageIcon("./Media/Images/pacscir.png").getImage();

        bulletUImage = new ImageIcon("./Media/Images/bulletu.png").getImage();
        bulletDImage = new ImageIcon("./Media/Images/bulletd.png").getImage();
        bulletLImage = new ImageIcon("./Media/Images/bulletl.png").getImage();
        bulletRImage = new ImageIcon("./Media/Images/bulletr.png").getImage();
        gunImage = new ImageIcon("./Media/Images/gun.png").getImage();

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


        loadMap();

        for (Block ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        //how long it takes to start timer, milliseconds gone between frames
        gameLoop = new Timer(25, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for(Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
    }


    public void loadMap() {
        walls = new ArrayList<Block>();
        foods = new HashSet<Block>();
        ghosts = new ArrayList<Block>();
        cherrys = new HashSet<Block>();
        oranges = new HashSet<Block>();
        scaredGhosts = new HashSet<Block>();
        lightGhosts = new ArrayList<>();
        guns = new ArrayList<Block>();
        snake = new ArrayList<>();
        snake2 = new ArrayList<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'g') { //wallRP
                    Block wall = new Block(wallRPImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize, false, 'b');
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize, false, 'o');
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize, false, 'p');
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize, false, 'r');
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == 'C') { //pacman
                    Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                    cherrys.add(cherry);
                } else if (tileMapChar == 'O') { //pacman
                    Block orange = new Block(orangeImage, x, y, tileSize, tileSize);
                    oranges.add(orange);
                } else if (tileMapChar == ' ') { //food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public boolean isPacmanCanMove(Block block, char direction) {
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

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void actionPerformed(ActionEvent e) {
        return;
    }


}