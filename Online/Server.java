package Online;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static GameState gameState = new GameState();
    private static int playerCounter = 0;
    private static final Map<Integer, PrintWriter> clientsOut = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Game server started on port " + PORT);

            // اجرای دائمی آپدیت بازی و ارسال وضعیت به کلاینت‌ها
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    gameState.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                broadcastGameState();
            }, 0, 25, TimeUnit.MILLISECONDS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int playerId = playerCounter++;
                pool.execute(new ClientHandler(clientSocket, playerId));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastGameState() {
        StringBuilder state = new StringBuilder("STATE:");
        StringBuilder dirs = new StringBuilder("DIRS:");
        StringBuilder shootingBuleltsStr = new StringBuilder("ShootingBullets:");
        StringBuilder swoardSpawner = new StringBuilder("SwoardSpawner:");
        StringBuilder bulletSpawner = new StringBuilder("bulletSpawner:");
        StringBuilder ScifiBulletSpawner = new StringBuilder("ScifiBulletSpawner:");
        StringBuilder heartSpawner = new StringBuilder("heartSpawner:");
        StringBuilder gunSpawner = new StringBuilder("gunSpawner:");

        for (int id : gameState.getPlayers().keySet()) {
            GameState.Player p = gameState.getPlayer(id);
            if (p != null) {
                int px = p.x / gameState.tileSize;
                int py = p.y / gameState.tileSize;
                state.append(id).append(",").append(px).append(",").append(py).append(",").append(p.bulletCount).append(",")
                        .append(p.scifiBulletCount).append(",").append(p.haveGun).append(",").append(p.haveSwoard).
                        append(",").append(p.isDead).append(",").append(p.kills).append(",").append(p.character).append(";");
                dirs.append(id).append(",").append(p.direction).append(";");
            }
        }

        for (GameState.Bullet bullet : gameState.shootingBullets) {
            if (Math.abs(bullet.vx) == 8 || Math.abs(bullet.vy) == 8) {
                shootingBuleltsStr.append(bullet.id + "," + bullet.x + "," + bullet.y + "," +
                        bullet.direction + "," + 0 + ";");
            } else {
                shootingBuleltsStr.append(bullet.id + "," + bullet.x + "," + bullet.y + "," +
                        bullet.direction + "," + 1 + ";");
            }
        }

        for (Rectangle swoard : gameState.swoards) {
            swoardSpawner.append(swoard.x + "," + swoard.y + ";");
        }
        for (Rectangle gun : gameState.guns) {
            gunSpawner.append(gun.x + "," + gun.y + ";");
        }
        for (Rectangle bullet : gameState.bullets) {
            bulletSpawner.append(bullet.x + "," + bullet.y + ";");
        }
        for (Rectangle scifi : gameState.scifiBullets) {
            ScifiBulletSpawner.append(scifi.x + "," + scifi.y + ";");
        }
        for (Rectangle heart : gameState.hearts) {
            heartSpawner.append(heart.x + "," + heart.y + ";");
        }

        String stateStr = state.toString();
        String dirsStr = dirs.toString();

        for (PrintWriter out : clientsOut.values()) {
            out.println(stateStr);
            out.println(dirsStr);
            out.println(shootingBuleltsStr);
            out.println(swoardSpawner);
            out.println(bulletSpawner);
            out.println(ScifiBulletSpawner);
            out.println(gunSpawner);
            out.println(heartSpawner);
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private int playerId;

        public ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
        }

        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println(playerId);
                clientsOut.put(playerId, out);

                // 🔹 اولین پیام باید character باشه
                String input = in.readLine();
                String character = "Default";
                if (input != null && input.startsWith("character:")) {
                    String[] parts = input.substring(10).split(",");
                    character = parts[0];
                }

                // 🔹 حالا پلیر رو با character بساز
                Random random = new Random();
                int x, y;
                do {
                    x = random.nextInt(43);
                    y = random.nextInt(23);
                } while (gameState.tileMap[y].charAt(x) != ' ');
                gameState.addPlayer(playerId, x * 1024, y * 1024, 31, 31, "Player" + playerId, 3, character);

                while ((input = in.readLine()) != null) {
                    if (input.startsWith("DIR:")) {
                        char dirChar = ' ';
                        String dir = input.substring(4);
                        switch (dir) {
                            case "U": dirChar = 'U'; break;
                            case "D": dirChar = 'D'; break;
                            case "L": dirChar = 'L'; break;
                            case "R": dirChar = 'R'; break;
                        }
                        gameState.updatePlayerDirection(playerId, dirChar);
                    } else if (input.startsWith("Bullet:")) {
                        String[] parts = input.substring(7).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        int bullet = Integer.parseInt(parts[1]);
                        int scifi = Integer.parseInt(parts[2]);
                        gameState.updatePlayerbullets(playerID, bullet, scifi);
                    } else if (input.startsWith("Weapon:")) {
                        String[] parts = input.substring(7).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        int gun = Integer.parseInt(parts[1]);
                        int sward = Integer.parseInt(parts[2]);
                        gameState.updatePlayerWeapon(playerID, gun == 1, sward == 1);
                    } else if (input.startsWith("ShootBullet:")) {
                        String[] parts = input.substring(12).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        x = Integer.parseInt(parts[1]);
                        y = Integer.parseInt(parts[2]);
                        String direction = parts[3];
                        gameState.updateShootingBullets(playerID, x, y, direction, 0);
                    } else if (input.startsWith("ShootSBullet:")) {
                        String[] parts = input.substring(13).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        x = Integer.parseInt(parts[1]);
                        y = Integer.parseInt(parts[2]);
                        String direction = parts[3];
                        gameState.updateShootingBullets(playerID, x, y, direction, 1);
                    } else if (input.startsWith("remove:")) {
                        String[] parts = input.substring(12).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        gameState.removePlayer(playerID);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                gameState.removePlayer(playerId);
                clientsOut.remove(playerId);
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
