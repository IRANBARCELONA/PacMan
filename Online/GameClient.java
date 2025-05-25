package Online;

import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class GameClient extends JFrame {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 12345;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Point playerPosition = new Point(100, 100);

    public GameClient() throws IOException {
        setTitle("Client Game");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        socket = new Socket(SERVER_IP, PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                try {
                    switch (e.getKeyCode()) {
                        case java.awt.event.KeyEvent.VK_W -> out.writeObject("UP");
                        case java.awt.event.KeyEvent.VK_S -> out.writeObject("DOWN");
                        case java.awt.event.KeyEvent.VK_A -> out.writeObject("LEFT");
                        case java.awt.event.KeyEvent.VK_D -> out.writeObject("RIGHT");
                    }
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        new Thread(() -> {
            while (true) {
                try {
                    GameState gameState = (GameState) in.readObject();
                    playerPosition = gameState.playerPosition;
                    repaint();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerPosition.x, playerPosition.y, 50, 50);
    }

    public static void main(String[] args) throws IOException {
        new GameClient();
    }
}
