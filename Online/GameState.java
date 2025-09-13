package Online;

import com.sun.source.tree.SynchronizedTree;

import java.awt.*;
import java.util.*;
import java.awt.Rectangle;
import java.util.List;


public class GameState {

    public class Player {
        int id;
        public int x, y;
        public int velocityX, velocityY;
        public char direction, prevDirection;
        public String userName;
        public int width, height;
        public int lives;
        public int startX, startY;
        public boolean haveGun = false;
        public boolean haveSwoard = false;
        public int bulletCount = 0;
        public int scifiBulletCount = 0;
        public String character = "pacman";
        public boolean isDead = false;
        public int kills = 0;
        public boolean speedster = false;
        public boolean ghost = false;
        public boolean blackout = false;

        Player(int x, int y, int width, int height, String userName, int lives) {
            this.startX = x;
            this.startY = y;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.userName = userName;
            if(character.equals("pacman")) {
                this.lives = lives * 2;
            }
            else{
                this.lives = lives;
            }
            this.velocityX = 0;
            this.velocityY = 0;

        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += velocityX;
            this.y += velocityY;
            for(Rectangle wall : walls){
                int m = 32;
                int n = 32;
                if(this.speedster){
                    m = this.width - 2;
                    n = this.height - 2;
                }
                if(collision(new Rectangle(this.x/32, this.y/32, m, n), wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                    break;
                }
            }

        }

        void updateVelocity() {
            if(this.speedster){
                if (this.direction == 'U') {
                    this.velocityX = 0;
                    this.velocityY = -tileSize * 4;
                } else if (this.direction == 'D') {
                    this.velocityX = 0;
                    this.velocityY = tileSize * 4;
                } else if (this.direction == 'L') {
                    this.velocityX = -tileSize * 4;
                    this.velocityY = 0;
                } else if (this.direction == 'R') {
                    this.velocityX = tileSize * 4;
                    this.velocityY = 0;
                }
            }
            else{
                if (this.direction == 'U') {
                    this.velocityX = 0;
                    this.velocityY = -tileSize * 2;
                } else if (this.direction == 'D') {
                    this.velocityX = 0;
                    this.velocityY = 2 * tileSize;
                } else if (this.direction == 'L') {
                    this.velocityX = -tileSize * 2;
                    this.velocityY = 0;
                } else if (this.direction == 'R') {
                    this.velocityX = tileSize * 2;
                    this.velocityY = 0;
                }
            }

        }

        void move() {
            x += velocityX;
            y += velocityY;

            for(Rectangle wall : walls){
                int m = 32;
                int n = 32;
                if(this.speedster){
                    m = this.width - 2;
                    n = this.height - 2;
                }
                if(collision(new Rectangle(this.x/32, this.y/32, m, n), wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    break;
                }
            }
        }

    }

    public class Bullet {
        public Image image;
        public int id;
        public int x, y;
        public int vx, vy;
        public char direction;
        int kind;

        public Bullet(int id, int x, int y, int vx, int vy, char dir) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.direction = dir;
        }

        public Bullet(int id, int x, int y, int vx, int vy, char dir, Image image) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.direction = dir;
            this.image = image;
        }

        public void moveBullet() {
            x += vx;
            y += vy;
        }
    }

    public static int rowCount = 24;
    public static int columnCount = 44;
    public static int tileSize = 32;
    List<Rectangle> walls = new ArrayList<>();
    List<Bullet> shootingBullets = new ArrayList<>();
    List<Rectangle> swoards = new ArrayList<>();
    List<Rectangle> hearts = new ArrayList<>();
    List<Rectangle> ghostFruits = new ArrayList<>();
    List<Rectangle> speedsterFruits = new ArrayList<>();
    List<Rectangle> blackoutFruits = new ArrayList<>();
    List<Rectangle> bullets = new ArrayList<>();
    List<Rectangle> scifiBullets = new ArrayList<>();
    List<Rectangle> guns = new ArrayList<>();



    String[] tileMap = {
            "XXsXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "X XXX              X    X            X  XGX",
            "X X   qq q qq XX X   XX   X X qqqqqq   XX X",
            "X X q         q  XX XXX X   q X    X q  X X",
            "X     X X X q   X X RXY X X q   XX   q X  X",
            "XX  q         q   X XXX X   q q    q q   XX",
            "X  X  qq q qq X X   XXX   X q X qq X q X  X",
            "XX X  X           X     X               X X",
            "XX  X   ggggggX X   g g   X ggggggX X X   X",
            "X X G X gNNNNNg X  gg gg  X gNNNNNg X G X X",
            "X   X   gggggg  X gg   gg X gggggg    X   X",
            "X XX XX              X         X X XXX XX X",
            "X X     g X g XX ggX G Xgg XgX          X X",
            "X   X X g X XgXX g   X   g  gX X X XX X   X",
            "XX X               X   X       X   X    X X",
            "XX   qqq qqq qqq XXXXXXXXX q q   q   q q  X",
            "XX q                       q  qq   qq  q  X",
            "XX qX X qq qqqqqX X XXX XX Xq    X    q   X",
            "X   q       q     X OXG XX  Xq     X q    X",
            "X X Xq qq q   q X X XXX X X    q qq    XX X",
            "X X  X    qX q    X XXX X   XX   qqX X  X X",
            "XGXX   Xq      XX         X    X       XXGX",
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    };

    public GameState() {
        players = new HashMap<>();
        loadWallsFromMap();
    }

    private void loadWallsFromMap() {
        walls.clear();
        for (int r = 0; r < tileMap.length; r++) {
            String row = tileMap[r];
            for (int c = 0; c < row.length(); c++) {
                char ch = row.charAt(c);
                if (ch == 'X') {
                    int x = c * tileSize;
                    int y = r * tileSize;
                    walls.add(new Rectangle(x, y, tileSize, tileSize));
                }
                else if (ch == 'g') {
                    int x = c * tileSize;
                    int y = r * tileSize;
                    walls.add(new Rectangle(x, y, tileSize, tileSize));
                }
                else if (ch == 'q') {
                    int x = c * tileSize;
                    int y = r * tileSize;
                    walls.add(new Rectangle(x, y, tileSize, tileSize));
                }
            }
        }
    }


    public Map<Integer, Player> players;

    public synchronized void addPlayer(int playerId, int x, int y, int width, int height, String username, int lives, String character) {
        players.put(playerId, new Player(x, y, width, height, username, lives));
        Player player = players.get(playerId);
        players.get(playerId).id = playerId;
        player.character = character;
        if(player.character.equals("pacman")) {
            player.lives *= 2;
        }
        if(player.character.equals("deadpool")) {
            player.haveGun = true;
            player.bulletCount = 3;
        }
        if(player.character.equals("leonardo")) {
            player.haveSwoard = true;
        }
    }

    public synchronized void removePlayer(int playerId) {
        players.remove(playerId);
    }

    public synchronized void updatePlayerDirection(int playerId, char direction) {
        Player player = players.get(playerId);
        if (player != null) {
            player.updateDirection(direction);
        }
    }

    public synchronized void updatePlayerbullets(int playerId, int bullet, int scifi) {
        Player player = players.get(playerId);
        if (player != null) {
            player.bulletCount += bullet;
            player.scifiBulletCount += scifi;
        }
    }

    public synchronized void updatePlayerWeapon(int playerId, boolean gun, boolean swoard) {
        Player player = players.get(playerId);
        if (player != null) {
            player.haveGun = gun;
            player.haveSwoard = swoard;
        }
    }

    public synchronized void updateShootingBullets(int playerId, int x, int y, String direction, int kind) {
        Player player = players.get(playerId);
        if (player != null) {
            char dir = 'U';
            int vx = 0;
            int vy = 0;

            if(kind == 0){
                switch (direction){
                    case "U":
                        dir = 'U';
                        vy = -8;
                        break;
                    case "D":
                        dir = 'D';
                        vy = 8;
                        break;
                    case "L":
                        dir = 'L';
                        vx = -8;
                        break;
                    case "R":
                        dir = 'R';
                        vx = 8;
                        break;
                }
                Bullet bullet = new Bullet(playerId, x, y, vx, vy, dir);
                shootingBullets.add(bullet);
                player.bulletCount--;
            }
            else{
                switch (direction){
                    case "U":
                        dir = 'U';
                        vy = -16;
                        break;
                    case "D":
                        dir = 'D';
                        vy = 16;
                        break;
                    case "L":
                        dir = 'L';
                        vx = -16;
                        break;
                    case "R":
                        dir = 'R';
                        vx = 16;
                        break;
                }
                Bullet bullet = new Bullet(playerId, x, y, vx, vy, dir);
                shootingBullets.add(bullet);
                player.scifiBulletCount--;
            }


        }
    }

    public synchronized void update() {
        for (Player player : players.values()) {
            player.move();
        }
        for(Bullet bullet : shootingBullets){
            bullet.moveBullet();
        }
        checkCollision();
        swoardSpawner();
        bulletSpawner();
        scifiBulletSpawner();
        heartSpawner();
        speedsterFruitSpawner();
        blackoutFruitSpawner();
        ghostFruitSpawner();
        gunSpawner();
    }

    public boolean collision(Rectangle a, Rectangle b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void checkCollision(){
        for (Rectangle wall : walls){
            List<Bullet> removeShootingBullets = new ArrayList<>();
            for(Bullet bullet : shootingBullets){
                if(collision(wall, new Rectangle(bullet.x, bullet.y, wall.width, wall.height))){
                    removeShootingBullets.add(bullet);
                }
            }
            shootingBullets.removeAll(removeShootingBullets);
        }
        for (Player player : players.values()) {

            for(Player otherPlayer : players.values()){
                if(collision(new Rectangle(player.x/32, player.y/32, tileSize, tileSize), new Rectangle(otherPlayer.x/32, otherPlayer.y/32, tileSize, tileSize))){
                    if(otherPlayer.haveSwoard && !player.haveSwoard && player.id!=otherPlayer.id){
                        player.lives--;
                        hitHandler(player);
                        if(player.lives==0){
                            otherPlayer.kills++;
                        }
                        if(otherPlayer.character == "leonardo"){
                            otherPlayer.lives++;
                        }
                    }
                    if(player.haveSwoard && !otherPlayer.haveSwoard && player.id!=otherPlayer.id){
                        otherPlayer.lives--;
                        hitHandler(otherPlayer);
                        if(otherPlayer.lives==0){
                            player.kills++;
                        }
                        if(player.character == "leonardo"){
                            player.lives++;
                        }
                    }
                    if(player.haveSwoard && otherPlayer.haveSwoard && player.id!=otherPlayer.id){
                        otherPlayer.lives--;
                        player.lives--;
                        hitHandler(player);
                        hitHandler(otherPlayer);
                        if(otherPlayer.lives==0){
                            player.kills++;
                        }
                        if(player.lives==0){
                            otherPlayer.kills++;
                        }
                        if(player.character == "leonardo"){
                            player.lives++;
                        }
                        if(otherPlayer.character == "leonardo"){
                            otherPlayer.lives++;
                        }
                    }
                    if(player.haveGun && player.id!=otherPlayer.id && player.character != "gholam" && otherPlayer.character == "gholam"){
                        otherPlayer.haveGun = true;
                        player.haveGun = false;
                    }
                    if(otherPlayer.haveGun && player.id!=otherPlayer.id && otherPlayer.character != "gholam" && player.character == "gholam"){
                        player.haveGun = true;
                        otherPlayer.haveGun = false;
                    }

                }
            }

            List<Bullet> removeShootingBullets = new ArrayList<>();
            for(Bullet bullet : shootingBullets){
                if(bullet.id != player.id &&
                    collision(new Rectangle(bullet.x, bullet.y, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    if(players.get(bullet.id).character == "deadpool"){
                        if(bullet.vx == 8 || bullet.vy == 8){
                            player.lives -= 1;
                            players.get(bullet.id).bulletCount++;
                        }
                        else{
                            player.lives -= 2;
                            players.get(bullet.id).scifiBulletCount++;
                        }
                    }


                    removeShootingBullets.add(bullet);
                    if(player.lives <= 0){
                        int id = bullet.id;
                        Player player2 = players.get(id);
                        player2.kills++;
                    }
                    hitHandler(player);
                }
            }
            shootingBullets.removeAll(removeShootingBullets);

            List<Rectangle> removeSoards = new ArrayList<>();
            for(Rectangle swoard : swoards){
                if(collision(new Rectangle(swoard.x * 32, swoard.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeSoards.add(swoard);
                    updatePlayerWeapon(player.id, false, true);
                }
            }
            swoards.removeAll(removeSoards);

            List<Rectangle> removeBullets = new ArrayList<>();
            for(Rectangle bullet : bullets){
                if(collision(new Rectangle(bullet.x * 32, bullet.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeBullets.add(bullet);
                    player.bulletCount++;

                }
            }
            bullets.removeAll(removeBullets);

            List<Rectangle> removeScifi = new ArrayList<>();
            for(Rectangle bullet : scifiBullets){
                if(collision(new Rectangle(bullet.x * 32, bullet.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeScifi.add(bullet);
                    player.scifiBulletCount++;

                }
            }
            scifiBullets.removeAll(removeScifi);

            List<Rectangle> removeGuns = new ArrayList<>();
            for(Rectangle gun : guns){
                if(collision(new Rectangle(gun.x * 32, gun.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeGuns.add(gun);
                    updatePlayerWeapon(player.id, true, false);
                }
            }
            guns.removeAll(removeGuns);

            List<Rectangle> removeHeart = new ArrayList<>();
            for(Rectangle heart : hearts){
                if(collision(new Rectangle(heart.x * 32, heart.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeHeart.add(heart);
                    player.lives++;
                }
            }
            hearts.removeAll(removeHeart);

            List<Rectangle> removeghostFruits = new ArrayList<>();
            for(Rectangle ghostfruit : ghostFruits){
                if(collision(new Rectangle(ghostfruit.x * 32, ghostfruit.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeghostFruits.add(ghostfruit);
                    player.ghost = true;
                }
            }
            ghostFruits.removeAll(removeghostFruits);

            List<Rectangle> removeblackoutFruit = new ArrayList<>();
            for(Rectangle blackoutFruit : blackoutFruits){
                if(collision(new Rectangle(blackoutFruit.x * 32, blackoutFruit.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removeblackoutFruit.add(blackoutFruit);
                    player.blackout = true;
                }
            }
            blackoutFruits.removeAll(removeblackoutFruit);

            List<Rectangle> removespeedsterFruit = new ArrayList<>();
            for(Rectangle speedsterFruit : speedsterFruits){
                if(collision(new Rectangle(speedsterFruit.x * 32, speedsterFruit.y * 32, tileSize, tileSize), new Rectangle(player.x/32, player.y/32, player.width, player.height))){
                    removespeedsterFruit.add(speedsterFruit);
                    player.speedster = true;
                }
            }
            speedsterFruits.removeAll(removespeedsterFruit);

        }
    }

    private void swoardSpawner(){
        if(swoards.size() <= 3){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(800);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                swoards.add(new Rectangle(x, y, tileSize, tileSize));}
        }
    }

    private void bulletSpawner(){
        if(bullets.size() <= 10){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(300);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                bullets.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }
    }

    private void scifiBulletSpawner(){
        if(scifiBullets.size() <= 5){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(100);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                scifiBullets.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }

    }

    private void gunSpawner(){
        if(guns.size() <= 3){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(800);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                guns.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }
    }

    private void heartSpawner(){
        if(hearts.size() <= 5){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(800);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                hearts.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }
    }

    private void speedsterFruitSpawner(){
        if(speedsterFruits.size() <= 1){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(1000);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                speedsterFruits.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }
    }

    private void blackoutFruitSpawner(){
        if(blackoutFruits.size() <= 1){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(1000);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                blackoutFruits.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }
    }

    private void ghostFruitSpawner(){
        if(ghostFruits.size() <= 1){
            Random rand = new Random();
            int gunSpawner = rand.nextInt(1000);
            if(gunSpawner == 41){
                int x;
                int y;
                do{
                    x = rand.nextInt(43);
                    y = rand.nextInt(23);
                } while(tileMap[y].charAt(x) != ' ');

                ghostFruits.add(new Rectangle(x, y, tileSize, tileSize));
            }
        }
    }

    private void hitHandler(Player player){
        if(player.lives > 0){
            Random random = new Random();
            int x, y;
            do{
                x = random.nextInt(43);
                y = random.nextInt(23);
            }while(tileMap[y].charAt(x) != ' ');
            player.x = 1024;
            player.y = 1024;
            player.haveSwoard = false;
            player.haveGun = false;
            player.speedster = false;
            player.ghost = false;
            players.put(player.id, player);
        }
        else{
            player.isDead = true;
        }

    }

    public synchronized Map<Integer, Player> getPlayers() {
        return new HashMap<>(players);
    }

    public synchronized Player getPlayer(int playerId) {
        return players.get(playerId);
    }


}
