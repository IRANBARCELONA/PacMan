package Online;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static GameState gameState = new GameState();
    private static int playerCounter = 0;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Game server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int playerId = playerCounter++;
                gameState.addPlayer(playerId, 1, 1);
                pool.execute(new ClientHandler(clientSocket, playerId));
            }

        } catch (IOException e) {
            e.printStackTrace();
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

                while (true) {
                    String input = in.readLine();
                    if (input == null) break;

                    if (input.startsWith("DIR:")) {
                        String dir = input.substring(4);
                        gameState.updatePlayerDirection(playerId, dir);
                    }

                    gameState.update();

                    StringBuilder state = new StringBuilder("STATE:");
                    for (int id : gameState.getPlayers().keySet()) {
                        GameState.Player p = gameState.getPlayer(id);
                        state.append(id).append(",").append(p.getX()).append(",").append(p.getY()).append(";");
                    }

                    out.println(state);
                }

            } catch (IOException e) {
                System.out.println("Player " + playerId + " disconnected.");
            } finally {
                gameState.removePlayer(playerId);
                try {
                    socket.close();
                } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
}
