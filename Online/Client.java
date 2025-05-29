package Online;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class Client extends JFrame {
    private Map<Integer, Point> players = new HashMap<>();
    private int playerId;
    private PrintWriter out;

    public Client(String serverAddress, int port) throws IOException {
        setTitle("Pac-Man Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // پنل سفارشی برای نقاشی بازیکن‌ها
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // پس‌زمینه مشکی
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // رسم بازیکن‌ها (دایره زرد رنگ)
                g.setColor(Color.YELLOW);
                synchronized (players) {
                    for (Point p : players.values()) {
                        g.fillOval(p.x * 20, p.y * 20, 18, 18);
                    }
                }
            }
        };

        add(gamePanel);

        // اتصال به سرور
        Socket socket = new Socket(serverAddress, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // گرفتن playerId از سرور
        playerId = Integer.parseInt(in.readLine());
        System.out.println("Connected as player " + playerId);

        // Thread دریافت وضعیت سرور
        Thread receiveThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("STATE:")) {
                        updatePlayers(line.substring(6));
                        // بروزرسانی گرافیک در EDT (Event Dispatch Thread)
                        SwingUtilities.invokeLater(gamePanel::repaint);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        // کنترل کیبورد برای حرکت دادن بازیکن
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        gamePanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                String dir = null;
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP:    dir = "UP"; break;
                    case java.awt.event.KeyEvent.VK_DOWN:  dir = "DOWN"; break;
                    case java.awt.event.KeyEvent.VK_LEFT:  dir = "LEFT"; break;
                    case java.awt.event.KeyEvent.VK_RIGHT: dir = "RIGHT"; break;
                }
                if (dir != null) {
                    out.println("DIR:" + dir);
                }
            }
        });

        setVisible(true);
    }

    private void updatePlayers(String data) {
        // داده به صورت: "0,5,7;1,2,3;..."
        Map<Integer, Point> newPlayers = new HashMap<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty()) continue;
            String[] p = part.split(",");
            int id = Integer.parseInt(p[0]);
            int x = Integer.parseInt(p[1]);
            int y = Integer.parseInt(p[2]);
            newPlayers.put(id, new Point(x, y));
        }
        synchronized (players) {
            players.clear();
            players.putAll(newPlayers);
        }
    }

    public static void main(String[] args) throws IOException {
        new Client("localhost", 12345);
    }
}
