package Online;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameState {

    public static class Player {
        private int x, y;
        private String direction;

        public Player(int x, int y) {
            this.x = x;
            this.y = y;
            this.direction = "NONE";
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }

        public void move() {
            switch (direction) {
                case "UP":
                    y -= 1;
                    break;
                case "DOWN":
                    y += 1;
                    break;
                case "LEFT":
                    x -= 1;
                    break;
                case "RIGHT":
                    x += 1;
                    break;
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Point getPosition() {
            return new Point(x, y);
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final Map<Integer, Player> players;

    public GameState() {
        players = new HashMap<>();
    }

    public synchronized void addPlayer(int playerId, int x, int y) {
        players.put(playerId, new Player(x, y));
    }

    public synchronized void removePlayer(int playerId) {
        players.remove(playerId);
    }

    public synchronized void updatePlayerDirection(int playerId, String direction) {
        Player player = players.get(playerId);
        if (player != null) {
            player.setDirection(direction);
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

