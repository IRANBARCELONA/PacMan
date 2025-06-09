package Online;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameState {

    public class Player {
        public int x, y;
        private int velocityX, velocityY;
        public char direction;
        private String userName;
        private int width, height;
        private int lives;
        private int bulletCount;
        private int startX, startY;

        Player(int x, int y, int width, int height, String userName, int lives) {
            this.startX = x;
            this.startY = y;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.userName = userName;
            this.lives = lives;
            this.bulletCount = 0;
            this.velocityX = 0;
            this.velocityY = 0;
        }

        void updateDirection(char direction) {
            if (direction == this.direction) {
                return;
            }
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize * 6;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize * 6;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize * 6;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize * 6;
                this.velocityY = 0;
            }
        }

        void move() {
            this.x += this.velocityX;
            this.y += this.velocityY;

            if (x % tileSize == 0 && y % tileSize == 0) {
                this.velocityX = 0;
                this.velocityY = 0;
            }
        }
    }

    public static int rowCount = 24;
    public static int columnCount = 44;
    public static int tileSize = 32;

    String[] tilemap = {
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "XGXXX              X    X            X  XGX",
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

    private final Map<Integer, Player> players;

    public GameState() {
        players = new HashMap<>();
    }

    public synchronized void addPlayer(int playerId, int x, int y, int width, int height, String username, int lives) {
        players.put(playerId, new Player(x, y, width, height, username, lives));
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

    public synchronized void update() {
        for (Player player : players.values()) {
            player.move();
        }
    }

    public synchronized Map<Integer, Player> getPlayers() {
        return new HashMap<>(players);
    }

    public synchronized Player getPlayer(int playerId) {
        return players.get(playerId);
    }
}
