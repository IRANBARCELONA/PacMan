package Online;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class Client extends JFrame {
    private Map<Integer, GameState.Player> players = new HashMap<>();
    private Map<Integer, String> playerDirections = new HashMap<>();
    private List<Rectangle> walls = new ArrayList<>();
    private List<Rectangle> walls2 = new ArrayList<>();
    private List<Rectangle> walls3 = new ArrayList<>();
    private List<Rectangle> guns = new ArrayList<>();
    private List<Rectangle> bullets = new ArrayList<>();
    private List<GameState.Bullet> shootingBullets = new ArrayList<>();
    private int playerId;
    private PrintWriter out;

    private final int tileSize = 32;

    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "X XXX              X    X            X  XGX",
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
    private Image wall2Image;
    private Image wall3Image;
    private Image gunImage;
    private Image bulletUImage;
    private Image bulletDImage;
    private Image bulletRImage;
    private Image bulletLImage;
    private Map<String, Image> pacmanImages = new HashMap<>();

    private String currentDirection = null;
    private Timer moveTimer;
    private Timer renderTimer;

    GameState gameState;

    public Client(String serverAddress, int port) throws IOException {
        gameState = new GameState();
        int width = gameState.tileSize * gameState.columnCount - 16;
        int height = gameState.tileSize * gameState.rowCount;

        setTitle("Pac-Man Client");
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadWallsFromMap();

        wall3Image = new ImageIcon("Media/Images/wall.png").getImage();
        wallImage = new ImageIcon("Media/Images/wallrp.png").getImage();
        wall2Image = new ImageIcon("Media/Images/wallph3Image.png").getImage();
        gunImage = new ImageIcon("Media/Images/gun.png").getImage();
        bulletUImage = new ImageIcon("Media/Images/bulletU.png").getImage();
        bulletDImage = new ImageIcon("Media/Images/bulletd.png").getImage();
        bulletRImage = new ImageIcon("Media/Images/bulletr.png").getImage();
        bulletLImage = new ImageIcon("Media/Images/bulletl.png").getImage();
        pacmanImages.put("U", new ImageIcon("Media/Images/pacmanUP.png").getImage());
        pacmanImages.put("D", new ImageIcon("Media/Images/pacmanDown.png").getImage());
        pacmanImages.put("L", new ImageIcon("Media/Images/pacmanLeft.png").getImage());
        pacmanImages.put("R", new ImageIcon("Media/Images/pacmanRight.png").getImage());

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                gunSpawner();
                bulletSpawner();
                checkCollisions();
                super.paintComponent(g);

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                synchronized (walls) {
                    for (Rectangle wall : walls) {
                        g.drawImage(wallImage, wall.x, wall.y, wall.width, wall.height, this);
                    }
                }

                synchronized (walls2) {
                    for (Rectangle wall : walls2) {
                        g.drawImage(wall2Image, wall.x, wall.y, wall.width, wall.height, this);
                    }
                }

                synchronized (walls3) {
                    for (Rectangle wall : walls3) {
                        g.drawImage(wall3Image, wall.x, wall.y, wall.width, wall.height, this);
                    }
                }

                synchronized (guns) {
                    for (Rectangle gun : guns) {
                        g.drawImage(gunImage, gun.x + 6, gun.y + 6, 20, 20, this);
                    }
                }

                synchronized (bullets) {
                    if(!bullets.isEmpty()){
                        for (Rectangle bullet : bullets) {
                            g.drawImage(bulletUImage, bullet.x + 8, bullet.y + 8, 16, 20, this);
                        }
                    }

                }

                synchronized (shootingBullets) {
                    if(!shootingBullets.isEmpty()){
                        for (GameState.Bullet bullet : shootingBullets) {
                            if(bullet.direction == 'U')
                                g.drawImage(bullet.image, bullet.x + 8, bullet.y - 12, 16, 16, this);
                            if(bullet.direction == 'D')
                                g.drawImage(bullet.image, bullet.x + 8, bullet.y + 20, 16, 16, this);
                            if(bullet.direction == 'R')
                                g.drawImage(bullet.image, bullet.x + 20, bullet.y + 8, 16, 16, this);
                            if(bullet.direction == 'L')
                                g.drawImage(bullet.image, bullet.x - 12, bullet.y + 8, 16, 16, this);


                        }
                    }
                }

                synchronized (players) {
                    for (Map.Entry<Integer, GameState.Player> entry : players.entrySet()) {
                        int id = entry.getKey();
                        GameState.Player p = entry.getValue();
                        String dir = playerDirections.getOrDefault(id, "D");
                        switch (dir){
                            case "U":
                                dir = "U";
                                break;
                            case "D":
                                dir = "D";
                                break;
                            case "L":
                                dir = "L";
                                break;
                            case "R":
                                dir = "R";
                                break;
                        }
                        Image pacmanImg = pacmanImages.getOrDefault(dir, pacmanImages.get("D"));
                        g.drawImage(pacmanImg, p.x, p.y, tileSize, tileSize, this);
                    }
                }
            }
        };

        add(gamePanel);

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
                        UpdatePlayers(line.substring(6));
                        SwingUtilities.invokeLater(gamePanel::repaint);
                    } else if (line.startsWith("DIRS:")) {
                        UpdatePlayerDirections(line.substring(5));
                    } else if (line.startsWith("ShootingBullets:")) {
                        UpdateShootingBullets(line.substring(16));
                    }
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        moveTimer = new Timer(25, e -> {
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
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        currentDirection = "U";
                        playerDirections.put(playerId, "U");
                        break;
                    case KeyEvent.VK_DOWN:
                        currentDirection = "D";
                        playerDirections.put(playerId, "D");
                        break;
                    case KeyEvent.VK_LEFT:
                        currentDirection = "L";
                        playerDirections.put(playerId, "L");
                        break;
                    case KeyEvent.VK_RIGHT:
                        currentDirection = "R";
                        playerDirections.put(playerId, "R");
                        break;
                    case KeyEvent.VK_E:
                        shootBullet();
                        break;
                    case KeyEvent.VK_Q:
                        shootScifi();
                        break;
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void UpdatePlayers(String data) {
        Map<Integer, GameState.Player> newPlayers = new HashMap<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int id = Integer.parseInt(p[0]);
            int x = Integer.parseInt(p[1]);
            int y = Integer.parseInt(p[2]);
            int bulletCount = Integer.parseInt(p[3]);
            int scifiBulletCount = Integer.parseInt(p[4]);
            boolean haveGun = Boolean.parseBoolean(p[5]);
            boolean haveSwoard = Boolean.parseBoolean(p[6]);
            GameState.Player player = gameState.new Player(x, y , tileSize, tileSize, "Player", 3);
            player.bulletCount = bulletCount;
            player.scifiBulletCount = scifiBulletCount;
            player.haveGun = haveGun;
            player.haveSwoard = haveSwoard;
            newPlayers.put(id, player);
        }
        synchronized (players) {
            players.clear();
            players.putAll(newPlayers);
        }
    }

    private void UpdateShootingBullets(String data) {
        List<GameState.Bullet> newShootingBullets = new ArrayList<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int id = Integer.parseInt(p[0]);
            int x = Integer.parseInt(p[1]);
            int y = Integer.parseInt(p[2]);
            char direction = p[3].charAt(0);
            int kind = Integer.parseInt(p[4]);

            int vx = 0, vy = 0;
            Image image = null;

            if (kind == 1) {
                // مثلا تیر خاص؟
            } else {
                switch (direction) {
                    case 'U':
                        image = bulletUImage;
                        vy = -8;
                        break;
                    case 'D':
                        image = bulletDImage;
                        vy = 8;
                        break;
                    case 'L':
                        vx = -8;
                        image = bulletLImage;
                        break;
                    case 'R':
                        vx = 8;
                        image = bulletRImage;
                        break;
                }
            }
            GameState.Bullet bullet = gameState.new Bullet(id, x, y, vx, vy, direction, image);
            newShootingBullets.add(bullet);
        }

        synchronized (shootingBullets) {
            shootingBullets.clear();
            shootingBullets.addAll(newShootingBullets);
        }
    }

    private void UpdatePlayerDirections(String data) {
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
                else if (ch == 'g') {
                    int x = c * tileSize;
                    int y = r * tileSize;
                    walls2.add(new Rectangle(x, y, tileSize, tileSize));
                }
                else if (ch == 'q') {
                    int x = c * tileSize;
                    int y = r * tileSize;
                    walls3.add(new Rectangle(x, y, tileSize, tileSize));
                }
            }
        }
    }

    private void gunSpawner(){
        Random rand = new Random();
        int gunSpawner = rand.nextInt(400);
        if(gunSpawner == 7){
            int x;
            int y;
            do{
            x = rand.nextInt(43);
            y = rand.nextInt(24);
            } while(tileMap[y].charAt(x) != ' ');

            guns.add(new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize));
        }
    }

    private void bulletSpawner(){
        Random rand = new Random();
        int gunSpawner = rand.nextInt(100);
        if(gunSpawner == 21){
            int x;
            int y;
            do{
                x = rand.nextInt(44);
                y = rand.nextInt(24);
            } while(tileMap[y].charAt(x) != ' ');

            bullets.add(new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize));
        }
    }

    private void shootBullet(){
        GameState.Player player = players.get(playerId);
        if(player.haveGun && player.bulletCount > 0){
            out.println("ShootBullet:" + playerId + "," + player.x + "," + player.y + "," + playerDirections.get(playerId));
        }

    }

    private void shootScifi(){
        return;
    }

    private void checkCollisions(){
        GameState.Player player = players.get(playerId);
        List<Rectangle> removeBullets = new ArrayList<>();
        for(Rectangle bullet : bullets){
            if(gameState.collision(new Rectangle(player.x, player.y, tileSize, tileSize), bullet)){
                out.println("Bullet:" + playerId + "," + 1 + "," + 0);
                removeBullets.add(bullet);
            }
        }
        bullets.removeAll(removeBullets);

        List<Rectangle> removeGuns = new ArrayList<>();
        for(Rectangle gun : guns){
            if(gameState.collision(new Rectangle(player.x, player.y, tileSize, tileSize), gun)){
                out.println("Weapon:" + playerId + "," + 1 + "," + 0);
                player.haveGun = true;
                removeGuns.add(gun);
            }
        }
        guns.removeAll(removeGuns);

    }

    public static void main(String[] args) throws IOException {
        new Client("localhost", 12345);
    }
}
