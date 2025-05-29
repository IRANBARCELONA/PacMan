import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;


public class App {
    public static JFrame frame;
    private static final int ROW_COUNT = 22;
    private static final int COLUMN_COUNT = 19;
    private static final int TILE_SIZE = 32;
    private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE + 14;
    private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
    private static Image BG;
    private static PacMan pacmanGame;




    public static void main(String[] args) {
        frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

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


        showMainMenu(frame);

        frame.setVisible(true);
        AudioManager.playLooping("menuMusic");
    }

    public static void showMainMenu(JFrame frame) {
        BG = new ImageIcon("Media/Images/background.png").getImage();

        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (BG != null) {
                    g.drawImage(BG, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        backgroundPanel.setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);

        JPanel bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        bottomLeftWrapper.setOpaque(false);
        bottomLeftWrapper.add(menuPanel);

        JButton playButton = createMenuButton("Play");
        JButton playOnButton = createMenuButton("Play Co-op");
        JButton optionsButton = createMenuButton("Options");
        JButton helpButton = createMenuButton("Help");
        JButton exitButton = createMenuButton("Exit");

        playButton.addActionListener(e -> startGame(frame));
        playOnButton.addActionListener(e -> startOnGame(frame));
        optionsButton.addActionListener(e -> showOptions(frame));
        helpButton.addActionListener(e -> showHelp(frame));
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(playButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(playOnButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(optionsButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(helpButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(bottomLeftWrapper, BorderLayout.SOUTH);
        frame.setContentPane(backgroundPanel);
        frame.revalidate();
        frame.repaint();
    }

    private static JButton createMenuButton(String text) {
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
        frame.getContentPane().removeAll();
        AudioManager.stopLooping("menuMusic");
        pacmanGame = new PacMan();
        frame.add(pacmanGame);

        frame.revalidate();
        pacmanGame.requestFocus();
    }

    private static void startOnGame(JFrame frame){

    }

    private static void showOptions(JFrame frame) {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.BLACK);
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

    private static void showHelp(JFrame frame){
        return;
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

        // چیدن آیتم‌ها
        gameOverPanel.add(Box.createVerticalGlue());
        gameOverPanel.add(imageLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(scoreLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(button);
        gameOverPanel.add(Box.createVerticalGlue());

        // پنل wrapper برای وسط‌چینی کلی با GridBagLayout
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(gameOverPanel);

        frame.setContentPane(wrapper);
        frame.revalidate();
        frame.repaint();
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
            gameOverIcon = new ImageIcon("./Media/Images/Win.png");

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
    }


}