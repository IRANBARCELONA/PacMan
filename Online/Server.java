package Online;

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
                gameState.update();
                broadcastGameState();
            }, 0, 25, TimeUnit.MILLISECONDS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int playerId = playerCounter++;
                gameState.addPlayer(playerId, 1024, 1024,32, 32, "Player" + playerId, 3);
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

        for (int id : gameState.getPlayers().keySet()) {
            GameState.Player p = gameState.getPlayer(id);
            if (p != null) {
                int px = p.x / gameState.tileSize;
                int py = p.y / gameState.tileSize;
                state.append(id).append(",").append(px).append(",").append(py).append(",").append(p.bulletCount).append(",")
                        .append(p.scifiBulletCount).append(",").append(p.haveGun).append(",").append(p.haveSwoard).append(";");
                dirs.append(id).append(",").append(p.direction).append(";");
            }
        }

        for(GameState.Bullet bullet : gameState.shootingBullets){
            if(Math.abs(bullet.vx) == 8 || Math.abs(bullet.vy) == 8){
                shootingBuleltsStr.append(bullet.id +
                        "," + bullet.x + "," + bullet.y + "," +
                        bullet.direction + "," + 0 + ";");

            }
            else{
                shootingBuleltsStr.append(bullet.id +
                        "," + bullet.x + "," + bullet.y + "," +
                        bullet.direction + "," + 1 + ";");
            }


        }

        String stateStr = state.toString();
        String dirsStr = dirs.toString();

        for (PrintWriter out : clientsOut.values()) {
            out.println(stateStr);
            out.println(dirsStr);
            out.println(shootingBuleltsStr);
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

                String input;
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
                    }
                    else if (input.startsWith("Bullet:")) {
                        String[] parts = input.substring(7).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        int bullet = Integer.parseInt(parts[1]);
                        int scifi = Integer.parseInt(parts[2]);

                        gameState.updatePlayerbullets(playerID, bullet, scifi);
                    }
                    else if (input.startsWith("Weapon:")) {
                        String[] parts = input.substring(7).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        int gun = Integer.parseInt(parts[1]);
                        int sward = Integer.parseInt(parts[2]);

                        gameState.updatePlayerWeapon(playerID, gun == 1, sward == 1);

                    }
                    else if (input.startsWith("ShootBullet:")) {
                        String[] parts = input.substring(12).split(",");
                        int playerID = Integer.parseInt(parts[0]);
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        String direction = parts[3];



                        gameState.updateShootingBullets(playerID, x, y, direction);

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
