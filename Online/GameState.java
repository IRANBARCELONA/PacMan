package Online;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.Rectangle;



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
        public boolean haveGun , haveSwoard;
        public int bulletCount = 0;
        public int scifiBulletCount = 0;
        public String character = "pacman";

        Player(int x, int y, int width, int height, String userName, int lives) {
            this.startX = x;
            this.startY = y;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.userName = userName;
            this.lives = lives;
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
                if(collision(new Rectangle(this.x/32, this.y/32, tileSize, tileSize), wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                    break;
                }
            }

        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize/8 * 16;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize/8 * 16;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize/8 * 16;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize/8 * 16;
                this.velocityY = 0;
            }
        }

        void move() {
            x += velocityX;
            y += velocityY;

            for(Rectangle wall : walls){
                if(collision(new Rectangle(this.x/32, this.y/32, tileSize, tileSize), wall)) {
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


    private Map<Integer, Player> players;

    public synchronized void addPlayer(int playerId, int x, int y, int width, int height, String username, int lives) {
        players.put(playerId, new Player(x, y, width, height, username, lives));
        players.get(playerId).id = playerId;
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

    public synchronized void updateShootingBullets(int playerId, int x, int y, String direction) {
        Player player = players.get(playerId);
        if (player != null) {
            char dir = 'U';
            int vx = 0;
            int vy = 0;

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
    }

    public synchronized void update() {
        for (Player player : players.values()) {
            player.move();
        }
        for(Bullet bullet : shootingBullets){
            bullet.moveBullet();
        }
        checkCollision();
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
                    System.out.println("yes");
                }
            }
            shootingBullets.removeAll(removeShootingBullets);
        }
        for (Player players : players.values()) {
            List<Bullet> removeShootingBullets = new ArrayList<>();
            for(Bullet bullet : shootingBullets){
                if(bullet.id != players.id &&
                    collision(new Rectangle(bullet.x, bullet.y, tileSize, tileSize), new Rectangle(players.x/32, players.y/32, players.width, players.height))){
                    removeShootingBullets.add(bullet);
                    players.lives -= 1;
                }
            }
            shootingBullets.removeAll(removeShootingBullets);
        }

    }

    public synchronized Map<Integer, Player> getPlayers() {
        return new HashMap<>(players);
    }

    public synchronized Player getPlayer(int playerId) {
        return players.get(playerId);
    }


}
