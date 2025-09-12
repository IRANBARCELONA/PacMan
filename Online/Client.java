package Online;

import javazoom.jl.player.Player;

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
    private List<Rectangle> scifiBullets = new ArrayList<>();
    private List<Rectangle> hearts = new ArrayList<>();
    private List<GameState.Bullet> shootingBullets = new ArrayList<>();
    private List<Rectangle> swoards = new ArrayList<>();
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
    private Image swoardImage;
    private Image gunImage;
    private Image heartImage;
    private Image sbulletUImage;
    private Image sbulletDImage;
    private Image sbulletRImage;
    private Image sbulletLImage;
    private Image bulletUImage;
    private Image bulletDImage;
    private Image bulletRImage;
    private Image bulletLImage;
    private Map<String, Image> pacmanImages = new HashMap<>();
    private Map<String, Image> pacmanGunnerImages = new HashMap<>();
    private Map<String, Image> pacmanswoardImages = new HashMap<>();
    private Map<String, Image> gholamImages = new HashMap<>();
    private Map<String, Image> gholamGunnerImages = new HashMap<>();
    private Map<String, Image> gholamswoardImages = new HashMap<>();
    private Map<String, Image> deadpoolImages = new HashMap<>();
    private Map<String, Image> deadpoolGunnerImages = new HashMap<>();
    private Map<String, Image> deadpoolswoardImages = new HashMap<>();
    private Map<String, Image> leonardoImages = new HashMap<>();
    private Map<String, Image> leonardoGunnerImages = new HashMap<>();
    private Map<String, Image> leonardoswoardImages = new HashMap<>();


    private String currentDirection = null;
    private Timer moveTimer;
    private Timer renderTimer;

    GameState gameState;

    public Client(String serverAddress, int port, String character) throws IOException {
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
        swoardImage = new ImageIcon("Media/Images/sword.png").getImage();
        gunImage = new ImageIcon("Media/Images/gun.png").getImage();
        sbulletUImage = new ImageIcon("Media/Images/sciBulletu.png").getImage();
        sbulletDImage = new ImageIcon("Media/Images/sciBulletd.png").getImage();
        sbulletRImage = new ImageIcon("Media/Images/sciBulletlr.png").getImage();
        sbulletLImage = new ImageIcon("Media/Images/sciBulletl.png").getImage();
        bulletUImage = new ImageIcon("Media/Images/bulletU.png").getImage();
        bulletDImage = new ImageIcon("Media/Images/bulletd.png").getImage();
        bulletRImage = new ImageIcon("Media/Images/bulletr.png").getImage();
        bulletLImage = new ImageIcon("Media/Images/bulletl.png").getImage();

        heartImage = new ImageIcon("Media/Images/hp.png").getImage();
        pacmanImages.put("U", new ImageIcon("Media/Images/pacmanUP.png").getImage());
        pacmanImages.put("D", new ImageIcon("Media/Images/pacmanDown.png").getImage());
        pacmanImages.put("L", new ImageIcon("Media/Images/pacmanLeft.png").getImage());
        pacmanImages.put("R", new ImageIcon("Media/Images/pacmanRight.png").getImage());
        pacmanGunnerImages.put("U", new ImageIcon("Media/Images/pacmangunU.png").getImage());
        pacmanGunnerImages.put("D", new ImageIcon("Media/Images/pacmangund.png").getImage());
        pacmanGunnerImages.put("R", new ImageIcon("Media/Images/pacmangunr.png").getImage());
        pacmanGunnerImages.put("L", new ImageIcon("Media/Images/pacmangunl.png").getImage());
        pacmanswoardImages.put("U", new ImageIcon("Media/Images/PacmanSwordu.png").getImage());
        pacmanswoardImages.put("D", new ImageIcon("Media/Images/PacmanSwordd.png").getImage());
        pacmanswoardImages.put("R", new ImageIcon("Media/Images/PacmanSwordr.png").getImage());
        pacmanswoardImages.put("L", new ImageIcon("Media/Images/PacmanSwordl.png").getImage());

        deadpoolImages.put("U", new ImageIcon("Media/Images/deadpoolU.jpg").getImage());
        deadpoolImages.put("D", new ImageIcon("Media/Images/deadpoolu.jpg").getImage());
        deadpoolImages.put("L", new ImageIcon("Media/Images/deadpoolu.jpg").getImage());
        deadpoolImages.put("R", new ImageIcon("Media/Images/deadpoolu.jpg").getImage());
        deadpoolGunnerImages.put("U", new ImageIcon("Media/Images/deadpoolgunU.jpg").getImage());
        deadpoolGunnerImages.put("D", new ImageIcon("Media/Images/deadpoolgund.jpg").getImage());
        deadpoolGunnerImages.put("R", new ImageIcon("Media/Images/deadpoolgunr.jpg").getImage());
        deadpoolGunnerImages.put("L", new ImageIcon("Media/Images/deadpoolgunl.jpg").getImage());
        deadpoolswoardImages.put("U", new ImageIcon("Media/Images/deadpoolSwoardu.jpg").getImage());
        deadpoolswoardImages.put("D", new ImageIcon("Media/Images/deadpoolSwoardu.jpg").getImage());
        deadpoolswoardImages.put("R", new ImageIcon("Media/Images/deadpoolSwoardu.jpg").getImage());
        deadpoolswoardImages.put("L", new ImageIcon("Media/Images/deadpoolSwoardu.jpg").getImage());

        gholamImages.put("U", new ImageIcon("Media/Images/gholamU.jpg").getImage());
        gholamImages.put("D", new ImageIcon("Media/Images/gholamu.jpg").getImage());
        gholamImages.put("L", new ImageIcon("Media/Images/gholamu.jpg").getImage());
        gholamImages.put("R", new ImageIcon("Media/Images/gholamu.jpg").getImage());
        gholamGunnerImages.put("U", new ImageIcon("Media/Images/gholamgunU.jpg").getImage());
        gholamGunnerImages.put("D", new ImageIcon("Media/Images/gholamgunu.jpg").getImage());
        gholamGunnerImages.put("R", new ImageIcon("Media/Images/gholamgunu.jpg").getImage());
        gholamGunnerImages.put("L", new ImageIcon("Media/Images/gholamgunu.jpg").getImage());
        gholamswoardImages.put("U", new ImageIcon("Media/Images/gholamSwoardu.jpg").getImage());
        gholamswoardImages.put("D", new ImageIcon("Media/Images/gholamSwoardu.jpg").getImage());
        gholamswoardImages.put("R", new ImageIcon("Media/Images/gholamSwoardu.jpg").getImage());
        gholamswoardImages.put("L", new ImageIcon("Media/Images/gholamSwoardu.jpg").getImage());

        leonardoImages.put("U", new ImageIcon("Media/Images/leonardoU.jpg").getImage());
        leonardoImages.put("D", new ImageIcon("Media/Images/leonardoU.jpg").getImage());
        leonardoImages.put("L", new ImageIcon("Media/Images/leonardoU.jpg").getImage());
        leonardoImages.put("R", new ImageIcon("Media/Images/leonardoU.jpg").getImage());
        leonardoGunnerImages.put("U", new ImageIcon("Media/Images/leonardogunU.jpg").getImage());
        leonardoGunnerImages.put("D", new ImageIcon("Media/Images/leonardogund.jpg").getImage());
        leonardoGunnerImages.put("R", new ImageIcon("Media/Images/leonardogunr.jpg").getImage());
        leonardoGunnerImages.put("L", new ImageIcon("Media/Images/leonardogunl.jpg").getImage());
        leonardoswoardImages.put("U", new ImageIcon("Media/Images/leonardoswoardu.jpg").getImage());
        leonardoswoardImages.put("D", new ImageIcon("Media/Images/leonardoSworadu.jpg").getImage());
        leonardoswoardImages.put("R", new ImageIcon("Media/Images/leonardoSworadu.jpg").getImage());
        leonardoswoardImages.put("L", new ImageIcon("Media/Images/leonardoSworadu.jpg").getImage());


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

                synchronized (swoards) {
                    if(!swoards.isEmpty()){
                        for (Rectangle swoard : swoards) {
                            g.drawImage(swoardImage, swoard.x + 8, swoard.y + 8, 16, 20, this);
                        }
                    }
                }

                synchronized (guns) {
                    if(!guns.isEmpty()){
                        for (Rectangle gun : guns) {
                            g.drawImage(gunImage, gun.x + 8, gun.y + 8, 16, 20, this);
                        }
                    }

                }

                synchronized (hearts) {
                    if(!hearts.isEmpty()){
                        for (Rectangle heart : hearts) {
                            g.drawImage(heartImage, heart.x + 8, heart.y + 8, 16, 20, this);
                        }
                    }

                }

                synchronized (bullets) {
                    if(!bullets.isEmpty()){
                        for (Rectangle bullet : bullets) {
                            g.drawImage(bulletUImage, bullet.x + 8, bullet.y + 8, 16, 20, this);
                        }
                    }

                }

                synchronized (scifiBullets) {
                    if(!scifiBullets.isEmpty()){
                        for (Rectangle scifi : scifiBullets) {
                            g.drawImage(sbulletUImage, scifi.x + 8, scifi.y + 8, 16, 20, this);
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
                        Image pacmanImg = null;
                        /*if(p.character.equals("leonardo")){

                            if(p.haveGun){
                                pacmanImg = leonardoGunnerImages.getOrDefault(dir, leonardoImages.get("D"));
                            }
                            else if(p.haveSwoard){
                                pacmanImg = leonardoswoardImages.getOrDefault(dir, leonardoImages.get("R"));
                            }
                            else{
                                pacmanImg = leonardoImages.getOrDefault(dir, leonardoImages.get("L"));
                            }
                        }
                        else if(p.character.equals("pacman")){
                            if(p.haveGun){
                                pacmanImg = pacmanGunnerImages.getOrDefault(dir, pacmanImages.get("D"));
                            }
                            else if(p.haveSwoard){
                                pacmanImg = pacmanswoardImages.getOrDefault(dir, pacmanImages.get("R"));
                            }
                            else{
                                pacmanImg = pacmanImages.getOrDefault(dir, pacmanImages.get("L"));
                            }
                        }
                        else if(p.character.equals("deadpool")){
                            if(p.haveGun){
                                pacmanImg = deadpoolGunnerImages.getOrDefault(dir, pacmanImages.get("D"));
                            }
                            else if(p.haveSwoard){
                                pacmanImg = deadpoolswoardImages.getOrDefault(dir, deadpoolImages.get("R"));
                            }
                            else{
                                pacmanImg = deadpoolImages.getOrDefault(dir, deadpoolImages.get("L"));
                            }
                        }
                        else if(p.character.equals("gholam")){

                            if(p.haveGun){
                                pacmanImg = gholamGunnerImages.getOrDefault(dir, gholamImages.get("D"));
                            }
                            else if(p.haveSwoard){
                                pacmanImg = gholamswoardImages.getOrDefault(dir, gholamImages.get("R"));
                            }
                            else{
                                pacmanImg = gholamImages.getOrDefault(dir, gholamImages.get("L"));
                            }
                        }*/

                        if(p.haveGun){
                            pacmanImg = pacmanGunnerImages.getOrDefault(dir, pacmanImages.get("D"));
                        }
                        else if(p.haveSwoard){
                            pacmanImg = pacmanswoardImages.getOrDefault(dir, pacmanImages.get("R"));
                        }
                        else{
                            pacmanImg = pacmanImages.getOrDefault(dir, pacmanImages.get("L"));
                        }

                        g.drawImage(pacmanImg, p.x, p.y, 32, 32, this);


                    }

                }
                GameState.Player player = players.get(playerId);
                if(player.isDead){
                    g.drawImage(new ImageIcon("Media/Images/youdied.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 150));
                    g.drawString(Integer.toString(players.size()), 900, 470);
                    g.drawString(Integer.toString(player.kills), 760, 660);
                    out.println("remove:" + "," + playerId);
                }


            }
        };

        add(gamePanel);

        Socket socket = new Socket(serverAddress, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        playerId = Integer.parseInt(in.readLine());
        System.out.println("Connected as player " + playerId);

        out.println("character:" + character + "," + playerId);

        Thread receiveThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("STATE:")) {
                        UpdatePlayers(line.substring(6));
                        SwingUtilities.invokeLater(gamePanel::repaint);
                    } else if (line.startsWith("DIRS:")) {
                        UpdatePlayerDirections(line.substring(5));
                    }
                    else if (line.startsWith("ShootingBullets:")) {
                        UpdateShootingBullets(line.substring(16));
                    }
                    else if (line.startsWith("SwoardSpawner:")) {
                        UpdateSwoardSpawner(line.substring(14));
                    }
                    else if (line.startsWith("bulletSpawner:")) {
                        UpdateBulletSpawner(line.substring(14));
                    }
                    else if (line.startsWith("gunSpawner:")) {
                        UpdateGunSpawner(line.substring(11));
                    }
                    else if (line.startsWith("ScifiBulletSpawner:")) {
                        UpdateScifiSpawner(line.substring(19));
                    }
                    else if (line.startsWith("heartSpawner:")) {
                        UpdateHeartSpawner(line.substring(13));
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
            boolean dead = Boolean.parseBoolean(p[7]);
            int kills  = Integer.parseInt(p[8]);
            String character = p[9];
            GameState.Player player = gameState.new Player(x, y , tileSize, tileSize, "Player", 3);
            player.bulletCount = bulletCount;
            player.scifiBulletCount = scifiBulletCount;
            player.haveGun = haveGun;
            player.haveSwoard = haveSwoard;
            player.isDead = dead;
            player.kills = kills;
            player.character = character;
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
                switch (direction) {
                    case 'U':
                        image = sbulletUImage;
                        vy = -16;
                        break;
                    case 'D':
                        image = sbulletDImage;
                        vy = 16;
                        break;
                    case 'L':
                        vx = -16;
                        image = sbulletLImage;
                        break;
                    case 'R':
                        vx = 16;
                        image = sbulletRImage;
                        break;
                }
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

    private void UpdateBulletSpawner(String data) {
        List<Rectangle> newSwoards = new ArrayList<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int x = Integer.parseInt(p[0]) * 32;
            int y = Integer.parseInt(p[1]) * 32;


            newSwoards.add(new Rectangle(x, y, tileSize, tileSize));
        }

        synchronized (bullets) {
            bullets.clear();
            bullets.addAll(newSwoards);
        }
    }

    private void UpdateGunSpawner(String data) {
        List<Rectangle> newSwoards = new ArrayList<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int x = Integer.parseInt(p[0]) * 32;
            int y = Integer.parseInt(p[1]) * 32;


            newSwoards.add(new Rectangle(x, y, tileSize, tileSize));
        }

        synchronized (guns) {
            guns.clear();
            guns.addAll(newSwoards);
        }
    }

    private void UpdateHeartSpawner(String data) {
        List<Rectangle> newSwoards = new ArrayList<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int x = Integer.parseInt(p[0]) * 32;
            int y = Integer.parseInt(p[1]) * 32;


            newSwoards.add(new Rectangle(x, y, tileSize, tileSize));
        }

        synchronized (hearts) {
            hearts.clear();
            hearts.addAll(newSwoards);
        }
    }

    private void UpdateScifiSpawner(String data) {
        List<Rectangle> newSwoards = new ArrayList<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int x = Integer.parseInt(p[0]) * 32;
            int y = Integer.parseInt(p[1]) * 32;


            newSwoards.add(new Rectangle(x, y, tileSize, tileSize));
        }

        synchronized (scifiBullets) {
            scifiBullets.clear();
            scifiBullets.addAll(newSwoards);
        }
    }

    private void UpdateSwoardSpawner(String data) {
        List<Rectangle> newSwoards = new ArrayList<>();
        String[] parts = data.split(";");
        for (String part : parts) {
            if (part.isEmpty())
                continue;
            String[] p = part.split(",");
            int x = Integer.parseInt(p[0]) * 32;
            int y = Integer.parseInt(p[1]) * 32;


            newSwoards.add(new Rectangle(x, y, tileSize, tileSize));
        }

        synchronized (swoards) {
            swoards.clear();
            swoards.addAll(newSwoards);
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
                x = rand.nextInt(43);
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

    private void shootScifi() {
        GameState.Player player = players.get(playerId);
        if (player.haveGun && player.scifiBulletCount > 0) {
            out.println("ShootSBullet:" + playerId + "," + player.x + "," + player.y + "," + playerDirections.get(playerId));
        }
    }

    public static void main(String[] args) throws IOException {
        new Client("localhost", 12345, "pacman");

    }
}
