import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;


public class App {
    private static final int ROW_COUNT = 22;
    private static final int COLUMN_COUNT = 19;
    private static final int TILE_SIZE = 32;
    private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE + 14;
    private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
    private static Image BG;
    
    private static AdvancedPlayer player;
    private static Thread musicThread;
    private static boolean isPlaying = false;

    private static int currentVolume = 50;
    
    private static PacMan pacmanGame;
    public static void playRepeatMusic(String filePath) {
        if (isPlaying) return;
        
        isPlaying = true;
        musicThread = new Thread(() -> {
            while (isPlaying) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    player = new AdvancedPlayer(fileInputStream);
                    player.play();

                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        musicThread.start();
    }
    
    public static void stopMusic() {
        if (player != null) {
            player.close();
            isPlaying = false;
        }
    }
    
    
    public static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    
        showMainMenu(frame);
    
        frame.setVisible(true);
        playRepeatMusic("Media/Musics/menuMusic.mp3");
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
        JButton exitButton = createMenuButton("Exit");
    
        playButton.addActionListener(e -> startGame(frame));
        playOnButton.addActionListener(e -> startOnGame(frame));
        optionsButton.addActionListener(e -> showOptions(frame));
        exitButton.addActionListener(e -> System.exit(0));
    
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(playButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(playOnButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(optionsButton);
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

        pacmanGame = new PacMan();
        frame.add(pacmanGame);

        frame.revalidate();
        pacmanGame.requestFocus();

        stopMusic();
    }

    private static void startOnGame(JFrame frame){

    }

    private static void showOptions(JFrame frame) {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.BLACK);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider volumeSlider = new JSlider(0, 100, currentVolume);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        volumeSlider.setBackground(Color.BLACK);
        volumeSlider.setForeground(Color.WHITE);

        volumeSlider.addChangeListener(e -> {
            currentVolume = volumeSlider.getValue();
            float volumeNormalized = currentVolume / 100f;
            AudioManager.setMusicVolume(volumeNormalized);
            AudioManager.setSfxVolume(volumeNormalized);
        });

        
        JButton backButton = createMenuButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showMainMenu(frame));

        optionsPanel.add(Box.createVerticalGlue());
        optionsPanel.add(volumeLabel);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        optionsPanel.add(volumeSlider);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        optionsPanel.add(backButton);
        optionsPanel.add(Box.createVerticalGlue());

        frame.setContentPane(optionsPanel);
        frame.revalidate();
        frame.repaint();
    }

    

    static void GameOver(JFrame frame , int score) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel gameOverPanel = new JPanel();
            gameOverPanel.setBackground(Color.BLACK);
            gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        
            ImageIcon gameOverIcon = new ImageIcon("./Media/Images/GameOver.png");
            Image scaledImage = gameOverIcon.getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

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
        
            JLabel scoreLabel = new JLabel("Score: " + score);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
            button.addActionListener(e -> showMainMenu(frame));

            gameOverPanel.add(Box.createVerticalGlue());
            gameOverPanel.add(imageLabel);
            gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameOverPanel.add(scoreLabel);
            gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameOverPanel.add(button);
            gameOverPanel.add(Box.createVerticalGlue());


            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            JPanel bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
            bottomLeftWrapper.setOpaque(false);
            bottomLeftWrapper.add(panel);

            gameOverPanel.add(bottomLeftWrapper, BorderLayout.SOUTH);
            frame.setContentPane(gameOverPanel);
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
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

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
        
            JLabel scoreLabel = new JLabel("Score: " + score);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
            button.addActionListener(e -> showMainMenu(frame));

            gameOverPanel.add(Box.createVerticalGlue());
            gameOverPanel.add(imageLabel);
            gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameOverPanel.add(scoreLabel);
            gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameOverPanel.add(button);
            gameOverPanel.add(Box.createVerticalGlue());


            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            JPanel bottomLeftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
            bottomLeftWrapper.setOpaque(false);
            bottomLeftWrapper.add(panel);

            gameOverPanel.add(bottomLeftWrapper, BorderLayout.SOUTH);
            frame.setContentPane(gameOverPanel);
            frame.revalidate();
            frame.repaint();
        }
    
}