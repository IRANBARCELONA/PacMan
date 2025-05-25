import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


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
        public void pacmanImage(char direction, boolean gunner){
            if(isPacmanCanMove(pacman, direction) && phase == 1){
                if (direction == 'U') {
                    pacman.image = pacmanUpImage;
                }
                else if (direction == 'D') {
                    pacman.image = pacmanDownImage;
                }
                else if (direction == 'L') {
                    pacman.image = pacmanLeftImage;
                }
                else if (direction == 'R') {
                    pacman.image = pacmanRightImage;
                }
            }
            else if(isPacmanCanMove(pacman, direction) && phase == 3){
                if (direction == 'U') {
                    if(!gunner)
                        pacman.image = pacmanUpImage;
                    else
                        pacman.image = pacmanGUpImage;
                }
                else if (direction == 'D') {
                    if(!gunner)
                        pacman.image = pacmanDownImage;
                    else
                        pacman.image = pacmanGDownImage;
                }
                else if (direction == 'L') {
                    if(!gunner)
                        pacman.image = pacmanLeftImage;
                    else
                        pacman.image = pacmanGLeftImage;
                }
                else if (direction == 'R') {
                    if(!gunner)
                        pacman.image = pacmanRightImage;
                    else
                        pacman.image = pacmanGRightImage;
                }
            }
            else if(phase == 4 && isPacmanCanMove(pacman, direction)){
                if (direction == 'U') {
                    pacman.image = pacmanSciUImage;
                }
                else if (direction == 'D') {
                    pacman.image = pacmanSciDImage;
                }
                else if (direction == 'L') {
                    pacman.image = pacmanSciLImage;
                }
                else if (direction == 'R') {
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
    private int boardWidth = columnCount * tileSize ;
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
    private String[] tileMap2 = {
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

    private String[] tileMap3 = {
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

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XoXXXXXXXXXXXXXXXXX",
        "XNXXXXXXXXXXXXXXXXX",
        "XNNNNNNNNNNNNNNpXXX",
        "XXXXXXXXX XXXXXXXXX",
        "XXXXXXXXX XXXXXXXXX",
        "XXXXXXggg gggXXXXXX",
        "XXXXXXgNgOgNgXNNNNM",
        "XXXXXXgg PgggXXXXXX",
        "XXXXXXXXXNXXXXXXXXX",
        "XXXXXXXXXbXXXXXXXXX",
        "XXXXXXXXXNXXXXXXXXX",
        "XE11111HNNNNNNNNNNX",
        "XNNNNNNNNNNNNNXNNNX",
        "XXXXXXXXXXXXXXXXXXX",
        "XrXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX"
    };

    ArrayList<Block> walls;
    HashSet<Block> foods;
    ArrayList<Block> ghosts;
    HashSet<Block> scaredGhosts;
    ArrayList<PacMan.Block> lightGhosts;
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
    int lives = 10;
    int phase = 1;
    static int phaseImage = 1;
    boolean gameOver = false;
    List<Position> previousPositions = new ArrayList<>();
    List<Position> previousPositions2 = new ArrayList<>();


    boolean subtitle1;
    boolean subtitle2;



    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
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
                else if (tileMapChar == 'P') { //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == 'C') { //pacman
                    Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                    cherrys.add(cherry);
                }
                else if (tileMapChar == 'O') { //pacman
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
        walls.clear();


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
                    PacMan.Block wall = new PacMan.Block(wallRPImage, x, y, tileSize, tileSize );
                    walls.add(wall);
                }
                else if (tileMapChar == 'G') { //light green ghost
                    PacMan.Block lightGhost = new PacMan.Block(lightGhostGImage, x, y, tileSize, tileSize, "G");
                    lightGhosts.add(lightGhost);
                }
                else if (tileMapChar == 'o') { //light orange ghost
                    PacMan.Block lightGhost = new PacMan.Block(lightGhostOImage, x, y, tileSize, tileSize, "O");
                    lightGhosts.add(lightGhost);
                }
                else if (tileMapChar == 'Y') { //pink ghost
                    PacMan.Block lightGhost = new PacMan.Block(lightGhostYImage, x, y, tileSize, tileSize, "Y");
                    lightGhosts.add(lightGhost);
                }
                else if (tileMapChar == 'R') { //red ghost
                    PacMan.Block lightGhost = new PacMan.Block(lightGhostRImage, x, y, tileSize, tileSize, "T");
                    lightGhosts.add(lightGhost);
                }
                else if (tileMapChar == 'P') { //pacman
                    pacman = new PacMan.Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' ') { //food
                    PacMan.Block food = new PacMan.Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (PacMan.Block lightGhost : lightGhosts) {
            g.drawImage(lightGhost.image, lightGhost.x, lightGhost.y, lightGhost.width, lightGhost.height, null);
        }

        for (Block snake : snake) {
            g.drawImage(snake.image, snake.x, snake.y, snake.width, snake.height, null);
        }

        for(Block snake : snake2){
            g.drawImage(snake.image, snake.x, snake.y, snake.width, snake.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        for (Block cherry : cherrys) {
            g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }

        for (Block orange : oranges) {
            g.drawImage(orange.image, orange.x, orange.y, orange.width, orange.height, null);
        }

        for (Block scaredGhost : scaredGhosts) {
            g.drawImage(scaredGhost.image, scaredGhost.x, scaredGhost.y, scaredGhost.width, scaredGhost.height, null);
        }

        for(Block gun : guns) {
            g.drawImage(gun.image, gun.x, gun.y, gun.width, gun.height,null);
        }


        if(phase == 1){
            g.setColor(Color.YELLOW);
            for (PacMan.Block food : foods) {
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        }
        else if(phase == 2){
            g.setColor(Color.GREEN);
            for (PacMan.Block food : foods) {
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        }
        else if(phase == 3){
            g.setColor(Color.PINK);
            for (PacMan.Block food : foods) {
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        }

        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Playbill", Font.PLAIN, 30));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("LIVES: x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/1, tileSize/1);
        }

        if(pacman.isBulletCounting && phase == 3){
            g.drawString("Bullets : " + String.valueOf(pacman.bulletCount) + "X", tileSize, (rowCount - 1) * tileSize);
        }

        if(subtitle1){
            g.drawString("You can't defeat me. I AM DANGER!!!", tileSize * 8, (rowCount - 1) * tileSize);
        }

        if(subtitle2){
            g.drawString("You have defeated me , but you are very powerful now.", tileSize * 8, (rowCount - 2) * tileSize);
            g.drawString("Kill the ghosts and become the KING!", tileSize * 8, (rowCount - 1) * tileSize);
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
        pacmanMove();
        fruitSpawner();
        checkCollision();
        phaseHandler();
        bulletMove();
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
    }

    private void fruitSpawner() {
        if(foods.size() == 150 && pacman.orangesLeft == 2){
            pacman.orangesLeft = 1;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block orange = new Block(orangeImage, x * tileSize, y * tileSize, tileSize, tileSize);
            oranges.add(orange);
        }
        else if(foods.size() == 50 && pacman.orangesLeft == 1){
            pacman.orangesLeft = 0;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block orange = new Block(orangeImage, x * tileSize, y * tileSize, tileSize, tileSize);
            oranges.add(orange);
        }


        if(foods.size() == 180 && pacman.cherrysLeft == 4){
            pacman.cherrysLeft = 3;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
        else if(foods.size() == 120 && pacman.cherrysLeft == 3){
            pacman.cherrysLeft = 2;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
        else if(foods.size() == 100 && pacman.cherrysLeft == 2){
            pacman.cherrysLeft = 1;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block cherry = new Block(cherryImage, x * tileSize, y * tileSize, tileSize, tileSize);
            cherrys.add(cherry);
        }
        else if(foods.size() == 30 && pacman.cherrysLeft == 1){
            pacman.cherrysLeft = 0;
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

    private void pacmanMove() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        if(pacman.x == 0){
            pacman.x = tileSize * columnCount;
        }
        else if(pacman.x == tileSize * columnCount){
            pacman.x = 0;
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
        if(pacman.bulletCount > 0 && pacman.isGunner) {
            play("Media/Musics/gunshot.mp3");
            char direction = pacman.direction;
            Block bullet;
            switch (direction) {
                case 'L':
                    bullet = new Block(bulletLImage, pacman.x - tileSize, pacman.y + 9, tileSize / 2, tileSize / 2, "b");
                    guns.add(bullet);
                    bullet.updateDirection(direction);
                    pacman.bulletCount--;
                    break;
                case 'R':
                    bullet = new Block(bulletRImage, pacman.x + tileSize, pacman.y + 9, tileSize / 2 , tileSize / 2, "b");
                    guns.add(bullet);
                    bullet.updateDirection(direction);
                    pacman.bulletCount--;
                    break;
                case 'U':
                    bullet = new Block(bulletUImage, pacman.x + 9, pacman.y - tileSize, tileSize / 2, tileSize / 2, "b");
                    guns.add(bullet);
                    bullet.updateDirection(direction);
                    pacman.bulletCount--;
                    break;
                case 'D':
                    bullet = new Block(bulletDImage, pacman.x + 7 , pacman.y + tileSize, tileSize / 2, tileSize / 2, "b");
                    guns.add(bullet);
                    bullet.updateDirection(direction);
                    pacman.bulletCount--;
                    break;
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
                if (phase == 2){ //score == 800
                    phase++;
                    stopMusic();
                    pacman.isBulletCounting = true;
                    Block gun = new Block(gunImage, ghosts.get(0).x, ghosts.get(0).y, tileSize, tileSize, "gun");
                    Block bullet = new Block(bulletUImage, ghosts.get(1).x + 5, ghosts.get(1).y + 5, tileSize * 2/3, tileSize * 2/3, "bullet");
                    Block bullet2 = new Block(bulletUImage, ghosts.get(2).x + 5, ghosts.get(2).y + 5, tileSize * 2/3, tileSize * 2/3, "bullet");
                    Block bullet3 = new Block(bulletUImage, ghosts.get(3).x + 5, ghosts.get(3).y + 5, tileSize * 2/3, tileSize * 2/3, "bullet");
                    guns.add(gun);
                    guns.add(bullet);
                    guns.add(bullet2);
                    guns.add(bullet3);
                    ghosts.remove(ghosts.get(0));
                    ghosts.remove(ghosts.get(0));
                    ghosts.remove(ghosts.get(0));
                    ghosts.remove(ghosts.get(0));
                    phase2();
                    loadMap2();
                }
                else {
                    gameLoop.stop();
                    App.win(App.frame , score , phase);
                    resetGame();
                }
            }
            else if (phase == 3) {
                if (phase == 3) { //snake.isEmpty() && snake2.isEmpty()
                    subtitle1 = false;
                    phase3();
                    phase++;
                    snake.clear();
                    snake2.clear();
                    guns.clear();
                    loadMap3();
                    pacman.pacmanImage(pacman.direction, pacman.isGunner);
                }
                else {
                    subtitle1 = false;
                    gameLoop.stop();
                    App.win(App.frame , score , phase);
                    resetGame();
                }
            }
            else if (phase == 4 && lightGhosts.isEmpty()){
                subtitle2 = false;
                gameLoop.stop();
                App.win(App.frame , score , phase);
                phase++;
                resetGame();
            }
        }
    }

    public void phase2(){
        play("Media/Musics/ph2Start.mp3");
        subtitle1 = true;
    }

    public void phase3(){
        play("Media/Musics/ph2End.mp3");
        subtitle2 = true;
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void checkCollision() {
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
        snake();
        ghost();
        lightGhost();

        Block cherryEaten = null;
        for (Block cherry : cherrys) {
            if (collision(pacman, cherry)) {
                play("Media/Musics/fruitEaten.mp3");
                cherryEaten = cherry;
                score += 100;
            }
        }
        cherrys.remove(cherryEaten);

        Block orangeEaten = null;
        for (Block orange : oranges) {
            if (collision(pacman, orange)) {
                mainSound = false;
                changeMusic("Media/Musics/scaryGhostTime.mp3");
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
            if (collision(pacman, food)) {
                if(foodEatingSound) {
                    play("Media/Musics/foodEating1.mp3");
                    foodEatingSound = false;
                }
                else {
                    play("Media/Musics/foodEating2.mp3");
                    foodEatingSound = true;
                }
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        //check gun and bullet collision
        Block gunTaken = null;
        Block bulletDestroyed = null;
        if(pacman.isGunner && !pacman.gunLoaded){
            playNTimes("Media/Musics/loadgun.mp3", pacman.bulletCount);
            pacman.gunLoaded = true;
        }
        for(Block gun: guns){
            if(collision(pacman, gun)){
                if(gun.sname == "gun"){
                    pacman.isGunner = true;
                    pacman.pacmanImage(pacman.direction, pacman.isGunner);
                }
                else if(gun.sname == "bullet"){
                    pacman.bulletCount += 1;
                    if(pacman.isGunner){
                        play("Media/Musics/loadgun.mp3");
                    }

                }
                gunTaken = gun;
            }

            for(Block wall : walls){
                if(collision(gun, wall)){
                    bulletDestroyed = gun;
                }
            }

            for(Block snake : snake){
                if(collision(gun, snake)){
                    if(gun.sname == "b") {
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

    public void snake(){
        ArrayList<Block> snakes = (ArrayList<Block>) snake.clone();
        Collections.reverse(snakes);
        ArrayList<Block> snakes2 = (ArrayList<Block>) snake2.clone();
        Collections.reverse(snakes2);
        for (int i = 0; i < snakes.size(); i++) {
            Block snake = snakes.get(i);
            if (snake.sname == "h") {
                if (collision(snake, pacman)) {
                    lives -= 1;
                    if (lives == 0) {
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
                        if (pacman.x >= snake.x && pacman.y >= snake.y) {
                            char newDirection = directions3[random.nextInt(10)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (pacman.x >= snake.x && pacman.y <= snake.y) {
                            char newDirection = directions[random.nextInt(10)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (pacman.x <= snake.x && pacman.y >= snake.y) {
                            char newDirection = directions2[random.nextInt(10)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (pacman.x <= snake.x && pacman.y <= snake.y) {
                            char newDirection = directions1[random.nextInt(10)];
                            previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions.size() > 100) {
                                previousPositions.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        }
                    } else if (random.nextInt(30) == 0) {
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

                if (collision(snake, pacman)) {
                    lives -= 1;
                    if (lives == 0) {
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
                    int delay = 5000;
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
                    int delay = 3000;
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

        if(snake.size() > 0 && snake.get(0).slife <= 0){
            destroySnake();
        }

        if(snake2.size() > 0 && snake2.get(0).slife2 <= 0){
            destroySnake2();
        }

        for (int i = 0; i < snakes2.size(); i++) {
            Block snake = snakes2.get(i);
            if (snake.sname == "h") {
                if (collision(snake, pacman)) {
                    lives -= 1;
                    if (lives == 0) {
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
                        if (pacman.x >= snake.x && pacman.y >= snake.y) {
                            char newDirection = directions3[random.nextInt(10)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (pacman.x >= snake.x && pacman.y <= snake.y) {
                            char newDirection = directions[random.nextInt(10)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (pacman.x <= snake.x && pacman.y >= snake.y) {
                            char newDirection = directions2[random.nextInt(10)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        } else if (pacman.x <= snake.x && pacman.y <= snake.y) {
                            char newDirection = directions1[random.nextInt(10)];
                            previousPositions2.add(new Position(snake.x, snake.y, snake.direction));
                            if (previousPositions2.size() > 100) {
                                previousPositions2.remove(0);
                            }
                            snake.updateDirection(newDirection);
                        }
                    } else if (random.nextInt(30) == 0) {
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
                if (collision(snake, pacman)) {
                    lives -= 1;
                    if (lives == 0) {
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
                    int delay = 5000;
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
                    int delay = 3000;
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

        if(snake.size() > 0 && snake.get(0).slife == 0){
            destroySnake();
        }


    }

    public void destroySnake(){
        for(int i = 0; i < snake.size(); i++){
            snake.remove(i);
        }
    }
    public void destroySnake2(){
        for(int i = 0; i < snake2.size(); i++){
            snake2.remove(i);
        }
    }

    public void ghost(){
        for (Block ghost : ghosts) {


            if (collision(ghost, pacman)) {
                if(!ghost.setVulnerable) {
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                }
                else{
                    if (collision(ghost, pacman)) {
                        play("Media/Musics/ghostEaten.mp3");
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
                    if(pacman.x >= ghost.x && pacman.y >= ghost.y){
                        char newDirection = directions3[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                    else if(pacman.x >= ghost.x && pacman.y <= ghost.y){
                        char newDirection = directions[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                    else if(pacman.x <= ghost.x && pacman.y >= ghost.y){
                        char newDirection = directions2[random.nextInt(10)];
                        ghost.updateDirection(newDirection);
                    }
                    else if(pacman.x <= ghost.x && pacman.y <= ghost.y){
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

    public void lightGhost(){
        List<Block> toRemove = new ArrayList<>();
        for (Block lightGhost : lightGhosts) {

            if (collision(lightGhost, pacman)) {
                toRemove .add(lightGhost);
                score += 800;
            }


            PacMan.Block foodEaten = null;
            for (PacMan.Block food : foods) {
                if (collision(lightGhost, food)) {
                    foodEaten = food;
                }
            }
            foods.remove(foodEaten);

            lightGhost.x += lightGhost.velocityX;
            lightGhost.y += lightGhost.velocityY;
            for (PacMan.Block wall : walls) {
                if (collision(lightGhost, wall) || lightGhost.x <= 0 || lightGhost.x + lightGhost.width >= boardWidth) {
                    lightGhost.x -= lightGhost.velocityX;
                    lightGhost.y -= lightGhost.velocityY;
                    if(pacman.x >= lightGhost.x && pacman.y >= lightGhost.y){
                        char newDirection = directions[random.nextInt(10)];
                        lightGhost.updateDirection(newDirection);
                    }
                    else if(pacman.x >= lightGhost.x && pacman.y <= lightGhost.y){
                        char newDirection = directions3[random.nextInt(10)];
                        lightGhost.updateDirection(newDirection);
                    }
                    else if(pacman.x <= lightGhost.x && pacman.y >= lightGhost.y){
                        char newDirection = directions1[random.nextInt(10)];
                        lightGhost.updateDirection(newDirection);
                    }
                    else if(pacman.x <= lightGhost.x && pacman.y <= lightGhost.y){
                        char newDirection = directions2[random.nextInt(10)];
                        lightGhost.updateDirection(newDirection);
                    }
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

    public void resetPositions() {
        pacman.reset();
        pacman.orangesLeft = 2;
        pacman.cherrysLeft = 4;
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    public void resetPositions2() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
    }

    public void play(String filePath) {
        new Thread(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
                player.play();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playNTimes(String filePath, int times) {
        new Thread(() -> {
            for (int i = 0; i < times; i++) {
                try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                    AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
                    player.play();
                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void playRepeatMusic(String filePath) {
        if (isPlaying) return;

        isPlaying = true;
        musicThread = new Thread(() -> {
            while (isPlaying) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    player = new AdvancedPlayer(fileInputStream);
                    player.play();

                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        musicThread.start();
    }

    public void changeMusic(String filePath) {
        try {
            stopMusic();
            Thread.sleep(1);
            playRepeatMusic(filePath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (player != null) {
            player.close();
            isPlaying = false;
        }
    }

    public void changePacmanMusic() {
        if(mainSound) {
            if (foods.size() > 30 && mainMusicCounter == 0) {
                changeMusic("Media/Musics/normalMove.mp3");
                mainMusicCounter = 1;
            }
            else if (foods.size() <= 30 && foods.size() > 20 && mainMusicCounter == 1) {
                changeMusic("Media/Musics/spurtMove1.mp3");
                mainMusicCounter = 2;
            }
            else if (foods.size() <= 20 && foods.size() > 12 && mainMusicCounter == 2) {
                changeMusic("Media/Musics/spurtMove2.mp3");
                mainMusicCounter = 3;
            }
            else if (foods.size() <= 12 && foods.size() > 5 && mainMusicCounter == 3) {
                changeMusic("Media/Musics/spurtMove3.mp3");
                mainMusicCounter = 4;
            }
            else if (foods.size() <= 5 && mainMusicCounter == 4) {
                changeMusic("Media/Musics/spurtMove4.mp3");
            }
            else if (foods.size() == 0){
                stopMusic();
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

            changeMusic("Media/Musics/normalMove.mp3");
            mainSound = true;
            counter = 0;
        }
        for(Block ghost : ghosts) {
            if(counter == 300) {
                counter = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(pacman.nextDirection != pacman.direction){
            pacman.updateDirection(pacman.nextDirection);
            pacman.pacmanImage(pacman.nextDirection, pacman.isGunner);
        }
        move();
        ghostValnrability();
        changePacmanMusic();
        if(phase == 3);
            bulletSpawner();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            App.GameOver(App.frame , score);
            resetGame();
        }
    }

    public void resetGame(){
        score = 0;
        lives = 5;
        phase = 1;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
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

        pacman.pacmanImage(pacman.direction, pacman.isGunner);
    }

    @Override
    public void keyReleased(KeyEvent e) {

        // System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        pacman.pacmanImage(pacman.direction, pacman.isGunner);
    }
}