package Online;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class Client extends JFrame {
    private Map<Integer, Point> players = new HashMap<>();
    private Map<Integer, String> playerDirections = new HashMap<>();  // ذخیره جهت هر بازیکن
    private List<Rectangle> walls = new ArrayList<>();
    private int playerId;
    private PrintWriter out;

    private final int tileSize = 32;

    private final String[] tileMap = {
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

    private Image wallImage;
    private Map<String, Image> pacmanImages = new HashMap<>(); // تصاویر پک‌من بر اساس جهت

    private String currentDirection = null;  // جهت فعلی حرکت
    private Timer moveTimer;
    private Timer renderTimer;

    public Client(String serverAddress, int port) throws IOException {
        // تنظیم اندازه پنجره طبق GameState
        GameState gameState = new GameState();
        int width = gameState.tileSize * gameState.columnCount - 16;
        int height = gameState.tileSize * gameState.rowCount;

        setTitle("Pac-Man Client");
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadWallsFromMap();

        // بارگذاری تصاویر
        wallImage = new ImageIcon("Media/Images/wall.png").getImage();
        pacmanImages.put("UP", new ImageIcon("Media/Images/pacmanUp.png").getImage());
        pacmanImages.put("DOWN", new ImageIcon("Media/Images/pacmanDown.png").getImage());
        pacmanImages.put("LEFT", new ImageIcon("Media/Images/pacmanLeft.png").getImage());
        pacmanImages.put("RIGHT", new ImageIcon("Media/Images/pacmanRight.png").getImage());

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                synchronized (walls) {
                    for (Rectangle wall : walls) {
                        g.drawImage(wallImage, wall.x, wall.y, wall.width, wall.height, this);
                    }
                }

                synchronized (players) {
                    for (Map.Entry<Integer, Point> entry : players.entrySet()) {
                        int id = entry.getKey();
                        Point p = entry.getValue();
                        String dir = playerDirections.getOrDefault(id, "DOWN"); // جهت پیش‌فرض DOWN

                        int px = p.x * tileSize;
                        int py = p.y * tileSize;
                        Image pacmanImg = pacmanImages.getOrDefault(dir, pacmanImages.get("DOWN"));
                        g.drawImage(pacmanImg, px, py, tileSize, tileSize, this);
                    }
                }
            }
        };

        add(gamePanel);

        // اتصال به سرور
        Socket socket = new Socket(serverAddress, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        playerId = Integer.parseInt(in.readLine());
        System.out.println("Connected as player " + playerId);

        Thread receiveThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("STATE:")) {
                        updatePlayers(line.substring(6));
                        SwingUtilities.invokeLater(gamePanel::repaint);
                    } else if (line.startsWith("DIRS:")) {
                        updatePlayerDirections(line.substring(5));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        // تایمر ارسال جهت هر 40 میلی‌ثانیه (حرکت پیوسته)
        moveTimer = new Timer(40, e -> {
            if (currentDirection != null) {
                out.println("DIR:" + currentDirection);
            }
        });
        moveTimer.start();

        // تایمر رندر 30fps برای repaint مستقل
        renderTimer = new Timer(25, e -> {
            gamePanel.repaint();
        });
        renderTimer.start();

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        currentDirection = "UP";
                        break;
                    case KeyEvent.VK_DOWN:
                        currentDirection = "DOWN";
                        break;
                    case KeyEvent.VK_LEFT:
                        currentDirection = "LEFT";
                        break;
                    case KeyEvent.VK_RIGHT:
                        currentDirection = "RIGHT";
                        break;
                }
            }
        });

        // وسط کردن پنجره
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updatePlayers(String data) {
        Map<Integer, Point> newPlayers = new HashMap<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
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

    private void updatePlayerDirections(String data) {
        // داده به صورت id,direction;id,direction;...
        String[] parts = data.split(";");
        synchronized (playerDirections) {
            for (String part : parts) {
                if (part.isEmpty()) continue;
                String[] pd = part.split(",");
                int id = Integer.parseInt(pd[0]);
                String dir = pd[1];
                playerDirections.put(id, dir);
            }
        }
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
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Client("localhost", 12345);
    }
}
