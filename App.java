import Online.Database;
import Online.GameUser;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class App {
    static Database db = new Database();
    static GameUser user;
    public static JFrame frame;
    private static final int ROW_COUNT = 22;
    private static final int COLUMN_COUNT = 19;
    private static final int TILE_SIZE = 32;
    private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE + 14;
    private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
    private static PacMan pacmanGame;


    
    private static Image p1;
    private static Image p2;
    private static Image p3;
    private static Image p4;
    private static Image p5;
    private static Image p6;
    private static Image p7;
    private static Image p8;
    private static Image p9;
    private static Image p10;
    private static Image p11;
    private static Image p12;
    private static Image p13;
    private static Image p14;
    private static Image p15;
    private static Image p16;
    private static Image p17;
    private static Image p18;
    private static Image pI;

    private static Image m1;
    private static Image m2;
    private static Image m3;
    private static Image m4;
    private static Image mI;

    private static Image l1;
    private static Image l2;
    private static Image l3;
    private static Image lI;
    
    static float musicVolume = 50;
    static float sfxVolume = 50;
    private static int page = 1;
    private static int menuPage = 10001;
    private static boolean isInApp = true;


    public static void main(String[] args) {


        AudioManager.loadSound("menuMusic", "Media/Musics/menuMusic.wav", true);
        AudioManager.loadSound("gunShot", "Media/Musics/gunShot.wav", false);
        AudioManager.loadSound("foodEating1", "Media/Musics/foodEating1.wav", false);
        AudioManager.loadSound("foodEating2", "Media/Musics/foodEating2.wav", false);
        AudioManager.loadSound("fruitEaten", "Media/Musics/fruitEaten.wav", false);
        AudioManager.loadSound("ghostEaten", "Media/Musics/ghostEaten.wav", false);
        AudioManager.loadSound("loadGun", "Media/Musics/loadGun.wav", false);
        AudioManager.loadSound("normalMove", "Media/Musics/normalMove.wav", true);
        AudioManager.loadSound("scaryGhostTime", "Media/Musics/scaryGhostTime.wav", true);
        AudioManager.loadSound("spurtMove1", "Media/Musics/spurtMove1.wav", true);
        AudioManager.loadSound("spurtMove2", "Media/Musics/spurtMove2.wav", true);
        AudioManager.loadSound("spurtMove3", "Media/Musics/spurtMove3.wav", true);
        AudioManager.loadSound("spurtMove4", "Media/Musics/spurtMove4.wav", true);
        AudioManager.loadSound("deadSnake", "Media/Musics/snakeDead.wav", false);
        AudioManager.loadSound("earthquick", "Media/Musics/earthquick.wav", false);
        AudioManager.loadSound("armor", "Media/Musics/armor.wav", false);
        AudioManager.loadSound("explosion", "Media/Musics/explosion.wav", false);
        AudioManager.loadSound("portalspawn", "Media/Musics/portalspawn.wav", false);
        AudioManager.loadSound("ghostspawn", "Media/Musics/ghostspawn.wav", false);
        AudioManager.loadSound("winsong", "Media/Musics/winsong.wav", true);
        AudioManager.loadSound("deafeat", "Media/Musics/deafeat.wav", true);
        AudioManager.loadSound("deafeated", "Media/Musics/deafeated.wav", true);
        AudioManager.loadSound("ph3", "Media/Musics/ph3MUsic.wav", true);




        SwingUtilities.invokeLater(() -> {
            new LoginPage(() -> {

                frame = new JFrame("Pac Man");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                showMainMenu(frame);

                frame.setVisible(true);

            });
        });

    }

    public static void showMainMenu(JFrame frame) {

        m1 = new ImageIcon("Media/Images/menuPlay.png").getImage();
        m2 = new ImageIcon("Media/Images/menuOptions.png").getImage();
        m3 = new ImageIcon("Media/Images/menuExit.png").getImage();
        m4 = new ImageIcon("Media/Images/menuAbout.png").getImage();

        JPanel backgroundPanel;

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.setOpaque(false);

        JPanel bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 300));
        bottomLeftWrapper.setOpaque(false);
        bottomLeftWrapper.add(menuPanel);

        JButton playButton = createMenuButton("Play");
        playButton.setFont(new Font("Courier New",Font.BOLD,20));
        JButton playOnButton = createMenuButton("Play Multiplayer\n(Soon)");
        playOnButton.setFont(new Font("Courier New",Font.BOLD,15));
        JButton optionsButton = createMenuButton("Options");
        optionsButton.setFont(new Font("Courier New",Font.BOLD,25));
        JButton aboutButton = createMenuButton("About");
        aboutButton.setFont(new Font("Courier New",Font.BOLD,25));
        JButton exitButton = createMenuButton("Exit");
        exitButton.setFont(new Font("Courier New",Font.BOLD,25));
        JButton rightB = createMenuButton(">>>");
        rightB.setFont(new Font("Courier New",Font.BOLD,25));
        JButton leftB = createMenuButton("<<<");
        leftB.setFont(new Font("Courier New",Font.BOLD,25));
        
        
        rightB.addActionListener(e -> {
            menuPage++;
            showMainMenu(frame);
        });
        leftB.addActionListener(e -> {
            menuPage--;
            showMainMenu(frame);
        });
        playButton.addActionListener(e -> {
            startGame(frame);
            menuPage = 10001;
        });
        playOnButton.addActionListener(e -> {
            startOnGame(frame);
            menuPage = 10001;
        });
        aboutButton.addActionListener(e -> {
            showHelp(frame);
        });
        optionsButton.addActionListener(e -> {
            showOptions(frame);
        });
        exitButton.addActionListener(e -> {
            System.exit(0);
            menuPage = 1;
        });

        if(menuPage % 4 == 1){

            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (m1 != null) {
                        g.drawImage(m1, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
            
            backgroundPanel.setLayout(new BorderLayout());
            bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 365));
            bottomLeftWrapper.setOpaque(false);
            bottomLeftWrapper.add(menuPanel);


            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(leftB);
            menuPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            menuPanel.add(playOnButton);
            menuPanel.add(Box.createRigidArea(new Dimension(40, 0)));
            menuPanel.add(playButton);
            menuPanel.add(Box.createRigidArea(new Dimension(43, 0)));
            menuPanel.add(rightB);
            menuPanel.add(Box.createVerticalGlue());
        }
        else if(menuPage % 4 == 2){
            mI = m2;
            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (mI != null) {
                        g.drawImage(mI, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
        
            backgroundPanel.setLayout(new BorderLayout());
            bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 400));
            bottomLeftWrapper.setOpaque(false);
            bottomLeftWrapper.add(menuPanel);

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(leftB);
            menuPanel.add(Box.createRigidArea(new Dimension(130 , 0)));
            menuPanel.add(optionsButton);
            menuPanel.add(Box.createRigidArea(new Dimension(135 , 0)));
            menuPanel.add(rightB);
            menuPanel.add(Box.createVerticalGlue());
        }
        else if(menuPage %4 == 3){
            mI = m3;
            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (mI != null) {
                        g.drawImage(mI, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
        
            backgroundPanel.setLayout(new BorderLayout());
            bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 300));
            bottomLeftWrapper.setOpaque(false);
            bottomLeftWrapper.add(menuPanel);

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(leftB);
            menuPanel.add(Box.createRigidArea(new Dimension(150 , 0)));
            menuPanel.add(exitButton);
            menuPanel.add(Box.createRigidArea(new Dimension(160 , 0)));
            menuPanel.add(rightB);
            menuPanel.add(Box.createVerticalGlue());
        }
        else if(menuPage % 4 == 0){
            mI = m4;
            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (mI != null) {
                        g.drawImage(mI, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
        
            backgroundPanel.setLayout(new BorderLayout());
            bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 400));
            bottomLeftWrapper.setOpaque(false);
            bottomLeftWrapper.add(menuPanel);

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(leftB);
            menuPanel.add(Box.createRigidArea(new Dimension(142 , 0)));
            menuPanel.add(aboutButton);
            menuPanel.add(Box.createRigidArea(new Dimension(140 , 0)));
            menuPanel.add(rightB);
            menuPanel.add(Box.createVerticalGlue());
        }
        else{

            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (m1 != null) {
                        g.drawImage(m1, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
            
            backgroundPanel.setLayout(new BorderLayout());

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(leftB);
            menuPanel.add(Box.createRigidArea(new Dimension(150, 0)));
            menuPanel.add(playButton);
            menuPanel.add(Box.createRigidArea(new Dimension(50, 0)));
            menuPanel.add(playOnButton);
            menuPanel.add(Box.createRigidArea(new Dimension(150, 0)));
            menuPanel.add(rightB);
            menuPanel.add(Box.createVerticalGlue());
        }
        

        backgroundPanel.add(bottomLeftWrapper, BorderLayout.CENTER);
        frame.setContentPane(backgroundPanel);
        frame.revalidate();
        frame.repaint();
        if(isInApp){
            AudioManager.playLooping("menuMusic");
            isInApp = false;
        }
        AudioManager.stopLooping("ph3");
        AudioManager.stopLooping("winsong");
    }

    static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    private static void startGame(JFrame frame) {
        l1 = new ImageIcon("Media/Images/loading1.png").getImage();
        l2 = new ImageIcon("Media/Images/loading2.png").getImage();
        l3 = new ImageIcon("Media/Images/loading3.png").getImage();

        pacmanGame = new PacMan();
        JPanel backgroundPanel;
        Timer timer = new Timer();

        int i = 0;
        while( i < 3){
            switch(i){
                case 1:
                    lI = l1;
                    break;
                case 2:
                    lI = l2;
                    break;
                case 3:
                    lI = l3;
                    break;
                default:
                    lI = l1;
                    break;
            }
            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (lI != null) {
                        g.drawImage(lI, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
            frame.getContentPane().removeAll();
            frame.setContentPane(backgroundPanel);
            frame.revalidate();
            frame.repaint();

            
            i++;
        }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    frame.getContentPane().removeAll();
                    AudioManager.stopLooping("menuMusic");
                    frame.add(pacmanGame);
                    frame.revalidate();
                    pacmanGame.requestFocus();
                }
            }, 2000);

        isInApp = false;
    }

    private static void startOnGame(JFrame frame){

    }

    private static void showOptions(JFrame frame) {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.BLUE);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));


        JLabel musicLabel = new JLabel("Music Volume");
        musicLabel.setForeground(Color.WHITE);
        musicLabel.setFont(new Font("Arial", Font.BOLD, 18));
        musicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider musicSlider = new JSlider(0, 100, 50);
        musicSlider.setMajorTickSpacing(10);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicSlider.setBackground(Color.BLACK);
        musicSlider.setForeground(Color.WHITE);

        musicSlider.addChangeListener(e -> {
            float musicVolume = musicSlider.getValue() / 100f;
            AudioManager.setMusicVolume(musicVolume);
        });


        JLabel sfxLabel = new JLabel("SFX Volume");
        sfxLabel.setForeground(Color.WHITE);
        sfxLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sfxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider sfxSlider = new JSlider(0, 100, 50);
        sfxSlider.setMajorTickSpacing(10);
        sfxSlider.setMinorTickSpacing(5);
        sfxSlider.setPaintTicks(true);
        sfxSlider.setPaintLabels(true);
        sfxSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        sfxSlider.setBackground(Color.BLACK);
        sfxSlider.setForeground(Color.WHITE);

        sfxSlider.addChangeListener(e -> {
            float sfxVolume = sfxSlider.getValue() / 100f;
            AudioManager.setSfxVolume(sfxVolume);
        });

        JButton backButton = createMenuButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showMainMenu(frame));

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        optionsPanel.add(musicLabel);
        optionsPanel.add(musicSlider);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        optionsPanel.add(sfxLabel);
        optionsPanel.add(sfxSlider);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        optionsPanel.add(backButton);


        frame.getContentPane().removeAll();
        frame.getContentPane().add(optionsPanel);
        frame.revalidate();
        frame.repaint();
    }

    private static void showOptions2(JFrame frame) {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.BLUE);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));


        JLabel musicLabel = new JLabel("Music Volume");
        musicLabel.setForeground(Color.WHITE);
        musicLabel.setFont(new Font("Arial", Font.BOLD, 18));
        musicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int MV = (int) musicVolume;
        JSlider musicSlider = new JSlider(0, 100, MV);
        musicSlider.setMajorTickSpacing(10);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        musicSlider.setBackground(Color.BLACK);
        musicSlider.setForeground(Color.WHITE);

        musicSlider.addChangeListener(e -> {
            musicVolume = musicSlider.getValue() / 100f;
            AudioManager.setMusicVolume(musicVolume);
            musicVolume *= 100;
        });


        JLabel sfxLabel = new JLabel("SFX Volume");
        sfxLabel.setForeground(Color.WHITE);
        sfxLabel.setFont(new Font("Arial", Font.BOLD, 18));
        sfxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int SV = (int) sfxVolume;
        JSlider sfxSlider = new JSlider(0, 100, SV);
        sfxSlider.setMajorTickSpacing(10);
        sfxSlider.setMinorTickSpacing(5);
        sfxSlider.setPaintTicks(true);
        sfxSlider.setPaintLabels(true);
        sfxSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        sfxSlider.setBackground(Color.BLACK);
        sfxSlider.setForeground(Color.WHITE);

        sfxSlider.addChangeListener(e -> {
            sfxVolume = sfxSlider.getValue() / 100f;
            AudioManager.setSfxVolume(sfxVolume);
            sfxVolume *= 100;
        });

        JButton backButton = createMenuButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showPause(frame));

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        optionsPanel.add(musicLabel);
        optionsPanel.add(musicSlider);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        optionsPanel.add(sfxLabel);
        optionsPanel.add(sfxSlider);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        optionsPanel.add(backButton);


        frame.getContentPane().removeAll();
        frame.getContentPane().add(optionsPanel);
        frame.revalidate();
        frame.repaint();
    }

    private static void showHelp(JFrame frame){
        p1 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p2 = new ImageIcon("Media/Images/aboutP1.png").getImage();
        p3 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p4 = new ImageIcon("Media/Images/aboutP2.png").getImage();
        p5 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p6 = new ImageIcon("Media/Images/aboutP3.png").getImage();
        p7 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p8 = new ImageIcon("Media/Images/aboutP4.png").getImage();
        p9 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p10 = new ImageIcon("Media/Images/aboutP5.png").getImage();
        p11 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p12 = new ImageIcon("Media/Images/aboutP6.png").getImage();
        p13 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p14 = new ImageIcon("Media/Images/aboutP7.png").getImage();
        p15 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p16 = new ImageIcon("Media/Images/aboutP8.png").getImage();
        p17 = new ImageIcon("Media/Images/aboutPn.png").getImage();
        p18 = new ImageIcon("Media/Images/aboutP9.png").getImage();
        JPanel backgroundPanel;
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        menuPanel.setOpaque(false);

        JPanel bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        bottomLeftWrapper.setOpaque(false);
        bottomLeftWrapper.add(menuPanel);

        JButton nextButton = createMenuButton("Next");
        JButton previousButton = createMenuButton("Prevoius");
        JButton finishButton = createMenuButton("Finish");
        JButton backButton = createMenuButton("Back");

        nextButton.addActionListener(e -> {
            page++;
            showHelp(frame);
        });
        previousButton.addActionListener(e -> {
            page--;
            showHelp(frame);
        });
        finishButton.addActionListener(e -> {
            showMainMenu(frame);
            page = 1;
        });
        backButton.addActionListener(e -> {
            showMainMenu(frame);
            page = 1;
        });

        if(page >= 2 && page <= 18){
            switch(page){
                case 2:
                    pI = p2;
                    break;
                case 3:
                    pI = p3;
                    break;
                case 4:
                    pI = p4;
                    break;
                case 5:
                    pI = p5;
                    break;
                case 6:
                    pI = p6;
                    break;
                case 7:
                    pI = p7;
                    break;
                case 8:
                    pI = p8;
                    break;
                case 9:
                    pI = p9;
                    break;
                case 10:
                    pI = p10;
                    break;
                case 11:
                    pI = p11;
                    break;
                case 12:
                    pI = p12;
                    break;
                case 13:
                    pI = p13;
                    break;
                case 14:
                    pI = p14;
                    break;
                case 15:
                    pI = p15;
                    break;
                case 16:
                    pI = p16;
                    break;
                case 17:
                    pI = p17;
                    break;
                default:
                    pI = p18;
            }
            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (pI != null) {
                        g.drawImage(pI, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
        
            backgroundPanel.setLayout(new BorderLayout());

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(previousButton);
            menuPanel.add(Box.createRigidArea(new Dimension(345 , 0)));
            menuPanel.add(nextButton);
            menuPanel.add(Box.createVerticalGlue());
        }

        else if(page == 1){

            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (p1 != null) {
                        g.drawImage(p1, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
            
            backgroundPanel.setLayout(new BorderLayout());

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(backButton);
            menuPanel.add(Box.createRigidArea(new Dimension(380, 0)));
            menuPanel.add(nextButton);
            menuPanel.add(Box.createVerticalGlue());
        }
        else{

            backgroundPanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (p1 != null) {
                        g.drawImage(p1, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                }
            };
            
            backgroundPanel.setLayout(new BorderLayout());

            menuPanel.add(Box.createVerticalGlue());
            menuPanel.add(previousButton);
            menuPanel.add(Box.createRigidArea(new Dimension(330, 0)));
            menuPanel.add(finishButton);
            menuPanel.add(Box.createVerticalGlue());
        }

        

        backgroundPanel.add(bottomLeftWrapper, BorderLayout.SOUTH);
        frame.setContentPane(backgroundPanel);
        frame.revalidate();
        frame.repaint();
    }

    static void GameOver(JFrame frame, int score) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setBackground(Color.BLACK);
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        ImageIcon gameOverIcon = new ImageIcon("./Media/Images/GameOver.png");
        Image scaledImage = gameOverIcon.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = new JButton("Return to menu");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        button.addActionListener(e -> showMainMenu(frame));

        gameOverPanel.add(Box.createVerticalGlue());
        gameOverPanel.add(imageLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(scoreLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(button);
        gameOverPanel.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(gameOverPanel);

        frame.setContentPane(wrapper);
        frame.revalidate();
        frame.repaint();

        isInApp = true;
    }

    static void win(JFrame frame , int score , int phase) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);



        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setBackground(Color.BLACK);
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

        ImageIcon gameOverIcon;
        if (phase == 2)
            gameOverIcon = new ImageIcon("./Media/Images/Win.png");
        else if(phase == 3)
            gameOverIcon = new ImageIcon("./Media/Images/WinP2.png");
        else if (phase > 3)
            gameOverIcon = new ImageIcon("./Media/Images/WinP3.png");
        else
            gameOverIcon = new ImageIcon("./Media/Images/WinP3.png");


        Image scaledImage = gameOverIcon.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button = new JButton("Return to menu");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        button.addActionListener(e -> showMainMenu(frame));

        gameOverPanel.add(Box.createVerticalGlue());
        gameOverPanel.add(imageLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(scoreLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(button);
        gameOverPanel.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(gameOverPanel);

        frame.setContentPane(wrapper);
        frame.revalidate();
        frame.repaint();

        isInApp = true;

    }

    static void showPause(JFrame frame){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        

        JPanel pausePanel = new JPanel();
        pausePanel.setBackground(new Color(0,0,0,200));
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.Y_AXIS));

        JButton resumeButton = new JButton("Resume");
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resumeButton.setForeground(Color.WHITE);
        resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
        resumeButton.setContentAreaFilled(false);
        resumeButton.setBorderPainted(false);
        resumeButton.setFocusPainted(false);
        resumeButton.setOpaque(false);
        resumeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                resumeButton.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                resumeButton.setForeground(Color.WHITE);
            }
        });
        resumeButton.addActionListener(e -> {
            PacMan.running2 = true;
            frame.getContentPane().removeAll();
            frame.add(pacmanGame);
            frame.revalidate();
            frame.repaint();
            pacmanGame.requestFocus();
        });
        

        JButton optinsButton = new JButton("Options");
        optinsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optinsButton.setForeground(Color.WHITE);
        optinsButton.setFont(new Font("Arial", Font.BOLD, 20));
        optinsButton.setContentAreaFilled(false);
        optinsButton.setBorderPainted(false);
        optinsButton.setFocusPainted(false);
        optinsButton.setOpaque(false);
        optinsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                optinsButton.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                optinsButton.setForeground(Color.WHITE);
            }
        });
        optinsButton.addActionListener(e -> showOptions2(frame));

        JButton quitButton = new JButton("Quit");
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setForeground(Color.WHITE);
        quitButton.setFont(new Font("Arial", Font.BOLD, 20));
        quitButton.setContentAreaFilled(false);
        quitButton.setBorderPainted(false);
        quitButton.setFocusPainted(false);
        quitButton.setOpaque(false);
        quitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                quitButton.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                quitButton.setForeground(Color.WHITE);
            }
        });
        quitButton.addActionListener(e -> showMainMenu(frame));

        pausePanel.add(Box.createHorizontalBox());
        pausePanel.add(resumeButton);
        pausePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        pausePanel.add(optinsButton);
        pausePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        pausePanel.add(quitButton);
        pausePanel.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(pausePanel);

        wrapper.setPreferredSize(new Dimension(COLUMN_COUNT * TILE_SIZE, ROW_COUNT * (TILE_SIZE - 1) - 10));
        frame.pack();
        
        frame.setContentPane(wrapper);
        frame.revalidate();
        frame.repaint();
    }

    public static void resume2(){
        frame.getContentPane().removeAll();
        frame.add(pacmanGame);
        frame.revalidate();
        frame.repaint();
        pacmanGame.requestFocus();        
    }

}