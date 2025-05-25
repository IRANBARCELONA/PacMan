package Online;

import java.net.*;
import java.io.*;
import java.awt.Point;

public class GameServer {
    private static final int PORT = 12345;
    private Point playerPosition = new Point(100, 100); // Start position

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started. Waiting for client...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected!");

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

        while (true) {
            String command = (String) in.readObject();

            switch (command) {
                case "UP" -> playerPosition.translate(0, -10);
                case "DOWN" -> playerPosition.translate(0, 10);
                case "LEFT" -> playerPosition.translate(-10, 0);
                case "RIGHT" -> playerPosition.translate(10, 0);
            }

            GameState gameState = new GameState(playerPosition);
            out.reset();
            out.writeObject(gameState);
            out.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        new GameServer().start();
    }
}
