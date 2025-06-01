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
            }, 0, 40, TimeUnit.MILLISECONDS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int playerId = playerCounter++;
                gameState.addPlayer(playerId, 1, 1, 32, 32, "Player" + playerId, 3);
                pool.execute(new ClientHandler(clientSocket, playerId));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastGameState() {
        StringBuilder state = new StringBuilder("STATE:");
        for (int id : gameState.getPlayers().keySet()) {
            GameState.Player p = gameState.getPlayer(id);
            if (p != null) {
                int px = p.x / gameState.tileSize;
                int py = p.y / gameState.tileSize;
                state.append(id).append(",").append(px).append(",").append(py).append(";");
            }
        }
        String stateStr = state.toString();

        for (PrintWriter out : clientsOut.values()) {
            out.println(stateStr);
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
                            case "UP": dirChar = 'U'; break;
                            case "DOWN": dirChar = 'D'; break;
                            case "LEFT": dirChar = 'L'; break;
                            case "RIGHT": dirChar = 'R'; break;
                        }
                        gameState.updatePlayerDirection(playerId, dirChar);
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
