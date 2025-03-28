import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;


public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;
        char name;

        int startX;
        int startY;
        char direction = 'U'; // U D L R
        char nextDirection;
        int velocityX = 0;
        int velocityY = 0;

        int orangesLeft = 4;
        int cherrysLeft = 4;

        boolean setVulnerable = false;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
            this.setVulnerable = setVulnerable;
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

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize ;
    private int boardHeight = rowCount * tileSize;
    private int counter = 0;

    private Image wallImage;
    private Image wallRPImage;

    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image valnerableGhostImage;

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

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X       XX  X     X",
            "X XXX    X     X  X",
            "X o XXX  X XX XXX X",
            "X XXX         X X X",
            "XX    X     XXXpX X",
            "XXXXX   XXX       X",
            "X     X     X XXXXX",
            "X XXXXggg ggg     X",
            "N X   gNg gNg  XX N",
            "X X X gg Pggg   X X",
            "X     g g g   X   X",
            "X  X  gbg g XXX   X",
            "X  X              X",
            "X X X  XXX   XXX  X",
            "X              X  X",
            "X  XXX  X XX X X XX",
            "X       X r  X    X",
            "XXX XXX XXXX XXX  X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    HashSet<Block> cherrys;
    HashSet<Block> oranges;
    HashSet<Block> snake;
    Block pacman;

    Timer gameLoop;
    boolean running = true;
    char[] directions = {'U', 'U', 'U', 'D', 'L', 'L', 'L', 'R', 'L', 'U'};
    char[] directions1 = {'U', 'U', 'U', 'U', 'R', 'R', 'L', 'D', 'R', 'R'};
    char[] directions2 = {'D', 'D', 'D', 'D', 'L', 'L', 'R', 'U', 'L', 'L'};
    char[] directions3 = {'D', 'D', 'D', 'U', 'L', 'R', 'R', 'D', 'R', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    int phase = 1;
    boolean gameOver = false;

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        if(phase == 1)
            setBackground(Color.BLACK);
        else if(phase == 2)
            setBackground(new Color(30,0,0,200));
        addKeyListener(this);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("/Media/Images/wall.png")).getImage();
        wallRPImage = new ImageIcon(getClass().getResource("/Media/Images/wallrp.png")).getImage();

        blueGhostImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Media/Images/blueGhost.png"))).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/Media/Images/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/Media/Images/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/Media/Images/redGhost.png")).getImage();
        valnerableGhostImage = new ImageIcon(getClass().getResource("/Media/Images/vulnerableGhost.png")).getImage();

        cherryImage = new ImageIcon(getClass().getResource("/Media/Images/cherry.png")).getImage();
        orangeImage = new ImageIcon(getClass().getResource("/Media/Images/orange.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("/Media/Images/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("/Media/Images/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/Media/Images/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/Media/Images/pacmanRight.png")).getImage();

        snakeheadLImage = new ImageIcon(getClass().getResource("/Media/Images/snakeheadL.png")).getImage();
        snakeheadRImage = new ImageIcon(getClass().getResource("/Media/Images/snakeheadR.png")).getImage();
        snakeheadUImage = new ImageIcon(getClass().getResource("/Media/Images/snakeheadU.png")).getImage();
        snakeheadDImage = new ImageIcon(getClass().getResource("/Media/Images/snakeheadD.png")).getImage();
        snakebodyHImage = new ImageIcon(getClass().getResource("/Media/Images/snakebodyH.png")).getImage();
        snakebodyVImage = new ImageIcon(getClass().getResource("/Media/Images/snakebodyV.png")).getImage();
        snakeendRImage = new ImageIcon(getClass().getResource("/Media/Images/snakeendR.png")).getImage();
        snakeendUImage = new ImageIcon(getClass().getResource("/Media/Images/snakeendU.png")).getImage();
        snakeendLImage = new ImageIcon(getClass().getResource("/Media/Images/snakeendL.png")).getImage();
        snakeendDImage = new ImageIcon(getClass().getResource("/Media/Images/snakeendD.png")).getImage();



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
        ghosts = new HashSet<Block>();
        cherrys = new HashSet<Block>();
        oranges = new HashSet<Block>();

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


    public void paintComponent(Graphics g) {

        for(Block ghost : ghosts) {
            if(ghost.setVulnerable){
                counter += 1;
                break;
            }
        }

        for(Block ghost : ghosts) {
            if(counter == 300){
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
        }
        for(Block ghost : ghosts) {
            if(counter == 300) {
                counter = 0;
            }
        }



        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
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
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        if(pacman.x == 0){
            pacman.x = tileSize * columnCount;
        }
        else if(pacman.x == tileSize * columnCount){
            pacman.x = 0;
        }


        if(foods.size() == 190 && pacman.orangesLeft == 4){
            pacman.orangesLeft = 3;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block orange = new Block(orangeImage, x * tileSize, y * tileSize, tileSize, tileSize);
            oranges.add(orange);
        }
        else if(foods.size() == 130 && pacman.orangesLeft == 3){
            pacman.orangesLeft = 2;
            int x;
            int y;
            do {
                x = random.nextInt(columnCount);
                y = random.nextInt(rowCount);
            }while (tileMap[y].charAt(x) != ' ');
            Block orange = new Block(orangeImage, x * tileSize, y * tileSize, tileSize, tileSize);
            oranges.add(orange);
        }
        else if(foods.size() == 90 && pacman.orangesLeft == 2){
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





        //check wall collisions
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        //check ghost collisions
        for (Block ghost : ghosts) {


            if (collision(ghost, pacman)) {
                if(ghost.setVulnerable == false) {
                    lives -= 1;
                    if (lives == 0) {
                        gameOver = true;
                        return;
                    }
                    resetPositions();
                }
                else{
                    if (collision(pacman, ghost)) {
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

            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
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
                else if (random.nextInt(30) == 0) {  // مقدار را افزایش دادم تا تغییر جهت کمتر شود
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

        //check cherry collision
        Block cherryEaten = null;
        for (Block cherry : cherrys) {
            if (collision(pacman, cherry)) {
                cherryEaten = cherry;
                score += 100;
            }
        }
        cherrys.remove(cherryEaten);

        //check orange collision
        Block orangeEaten = null;
        for (Block orange : oranges) {
            if (collision(pacman, orange)) {
                orangeEaten = orange;
                for(Block ghost : ghosts){
                    ghost.setVulnerable = true;
                    ghost.image = valnerableGhostImage;
                }
            }
        }
        oranges.remove(orangeEaten);

        //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }


    public void resetPositions() {
        pacman.reset();
        pacman.orangesLeft = 4;
        pacman.cherrysLeft = 4;
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(pacman.nextDirection != pacman.direction){
            pacman.updateDirection(pacman.nextDirection);
            pacman.pacmanImage(pacman.nextDirection);
        }
        move();
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