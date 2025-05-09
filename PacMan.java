import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
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
        char direction = 'U'; // U D L R
        char previousDirection;
        char nextDirection;
        int velocityX = 0;
        int velocityY = 0;
        int slife = 2;

        int orangesLeft = 2;
        int cherrysLeft = 4;

        boolean setVulnerable = false;

        boolean isTurning = false;
        int turnTimer = 0;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
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
            this.slife = 2;
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
        public void pacmanImage(char directoin){
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
    private int slife = 2;

    private Image wallImage;
    private Image wallRPImage;

    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image scaredGhostImage;

    private Image cherryImage;
    private Image orangeImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

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
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XoXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXX  NNNNNpXXX",
        "XXXXXXXXX XXXXXXXXX",
        "XXXXXXXXX XXXXXXXXX",
        "XXXXXXggg gggXXXXXX",
        "XXXXXXgNgOgNgXNNNNM",
        "XXXXXXgg PgggXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXbXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XE54321HNNNNNNNNNNX",
        "XNNNNNNNNNNNNNXNNNX",
        "XXXXXXXXXXXXXXXXXXX",
        "XrXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX",
        "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    ArrayList<Block> ghosts;
    HashSet<Block> scaredGhosts;
    HashSet<Block> cherrys;
    HashSet<Block> oranges;
    ArrayList<Block> snake;
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
    boolean gameOver = false;
    int snakeDelay = 2;
    List<Position> previousPositions = new ArrayList<>();





    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon("./Media/Images/wall.png").getImage();
        wallRPImage = new ImageIcon("./Media/Images/wallrp.png").getImage();

        blueGhostImage = new ImageIcon("./Media/Images/blueGhost.png").getImage();
        orangeGhostImage = new ImageIcon("./Media/Images/orangeGhost.png").getImage();
        pinkGhostImage = new ImageIcon("./Media/Images/pinkGhost.png").getImage();
        redGhostImage = new ImageIcon("./Media/Images/redGhost.png").getImage();
        scaredGhostImage = new ImageIcon("./Media/Images/scaredGhost.png").getImage();

        cherryImage = new ImageIcon("./Media/Images/cherry.png").getImage();
        orangeImage = new ImageIcon("./Media/Images/orange.png").getImage();

        pacmanUpImage = new ImageIcon("./Media/Images/pacmanUp.png").getImage();
        pacmanDownImage = new ImageIcon("./Media/Images/pacmanDown.png").getImage();
        pacmanLeftImage = new ImageIcon("./Media/Images/pacmanLeft.png").getImage();
        pacmanRightImage = new ImageIcon("./Media/Images/pacmanRight.png").getImage();

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
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new ArrayList<Block>();
        cherrys = new HashSet<Block>();
        oranges = new HashSet<Block>();
        scaredGhosts = new HashSet<Block>();
        snake = new ArrayList<>();

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
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        cherrys = new HashSet<Block>();
        oranges = new HashSet<Block>();
        snake = new ArrayList<Block>();

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
                else if (tileMapChar == ' ') { //food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
                else if (tileMapChar == 'H') { //snakes
                    Block snake1 = new Block(snakeheadRImage, x, y, tileSize , tileSize ,"h");
                    snake.add(snake1);
                }
                else if (tileMapChar == '1') { //snakes
                    Block snake2 = new Block(snakebodyHImage, x, y, tileSize , tileSize ,"b");
                    snake.add(snake2);
                }
                else if (tileMapChar == '2') { //snakes
                    Block snake2 = new Block(snakebodyHImage, x, y, tileSize , tileSize ,"b");
                    snake.add(snake2);
                }
                else if (tileMapChar == '3') { //snakes
                    Block snake2 = new Block(snakebodyHImage, x, y, tileSize , tileSize ,"b");
                    snake.add(snake2);
                }
                else if (tileMapChar == '4') { //snakes
                    Block snake2 = new Block(snakebodyHImage, x, y, tileSize , tileSize ,"b");
                    snake.add(snake2);
                }
                else if (tileMapChar == '5') { //snakes
                    Block snake2 = new Block(snakebodyHImage, x, y, tileSize , tileSize ,"b");
                    snake.add(snake2);
                }
                else if (tileMapChar == 'E') { //snakes
                    Block snake2 = new Block(snakeendRImage, x, y, tileSize , tileSize ,"e");
                    snake.add(snake2);
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

        for (Block snake : snake) {
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


        g.setColor(Color.YELLOW);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
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

    private void phaseHandler() {
        if (foods.isEmpty()) {
            Block orange1 = new Block(orangeImage, ghosts.get(0).x, ghosts.get(0).y, tileSize, tileSize);
            Block orange2 = new Block(orangeImage, ghosts.get(1).x, ghosts.get(1).y, tileSize, tileSize);
            Block cherry1 = new Block(cherryImage, ghosts.get(2).x, ghosts.get(2).y, tileSize, tileSize);
            Block cherry2 = new Block(cherryImage, ghosts.get(3).x, ghosts.get(3).y, tileSize, tileSize);
            if(phase == 1){
                phase++;
                this.setBackground(new Color(30,30,0,200));
            }
            else if(phase == 2){
                loadMap2();
                oranges.add(orange1);
                oranges.add(orange2);
                cherrys.add(cherry1);
                cherrys.add(cherry2);
                ghosts.remove(ghosts.get(0));
                ghosts.remove(ghosts.get(0));
                ghosts.remove(ghosts.get(0));
                ghosts.remove(ghosts.get(0));
                phase++;
            }
            if (foods.isEmpty() && phase == 3) {
                loadMap();
                resetPositions();
            }
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void checkCollision() {
        //check wall collisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        snake();
        ghost();

        //check cherry collision
        Block cherryEaten = null;
        for (Block cherry : cherrys) {
            if (collision(pacman, cherry)) {
                play("Media/Musics/fruitEaten.mp3");
                cherryEaten = cherry;
                score += 100;
            }
        }
        cherrys.remove(cherryEaten);

        //check orange collision
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

        //check food collision
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
            if(direction == 'U'){
                snake.image = snakeendUImage;
            }
            else if(direction == 'D'){
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
        for (int i = 0; i < snakes.size(); i++) {
            Block snake = snakes.get(i);
            if (snake.sname == "h") {
                if (collision(snake, pacman)) {
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions2();
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
                    }
                    else if (random.nextInt(30) == 0) {
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
                    }
                    else{
                        previousPositions.add(new Position(snake.x, snake.y, snake.direction));
                    }

                }
                setSnakeImage(snake.direction, snake);
            }
            else {
                if(snake.previousDirection != snake.direction){
                    snake.turnTimer = 12;
                    snake.isTurning = true;
                    snake.previousDirection = snake.direction;
                }

                if(snake.turnTimer != 0)
                    snake.turnTimer--;

                if(snake.turnTimer == 0){
                    snake.isTurning = false;
                }


                if(previousPositions.size() > 18200) {
                    int delay = 3000;
                    int posIndex = previousPositions.size() - 1 - (i * delay);
                    if (posIndex >= 0 && posIndex < previousPositions.size()) {
                        Position pos = previousPositions.get(posIndex);
                        snake.x = pos.x;
                        snake.y = pos.y;
                        snake.direction = pos.direction;
                        setSnakeImage(snake.direction, snake);
                    }
                    for(int j = 0; j<100;j++){
                        previousPositions.remove(0);
                    }

                }
                else{
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
    }

    public void ghost(){
        //check ghost collisions
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
            pacman.pacmanImage(pacman.nextDirection);
        }
        move();
        ghostValnrability();
        changePacmanMusic();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
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

        pacman.pacmanImage(pacman.direction);
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

        pacman.pacmanImage(pacman.direction);
    }
}