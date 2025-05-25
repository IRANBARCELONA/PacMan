import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class
App {
    private static final int ROW_COUNT = 22;
    private static final int COLUMN_COUNT = 19;
    private static final int TILE_SIZE = 32;
    private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE + 14;
    private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
    private static Image BG;

    private static AdvancedPlayer player;
    private static Thread musicThread;
    private static boolean isPlaying = false;

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


    public static void main(String[] args) {
        BG = new ImageIcon("Media/Images/background.png").getImage();

        JFrame frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

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

        JButton playOnlineButton = createMenuButton("Play Online");
        JButton playButton = createMenuButton("Play Offline");
        JButton optionsButton = createMenuButton("Options");
        JButton exitButton = createMenuButton("Exit");

        playOnlineButton.addActionListener(e -> startOnlineGame(frame));
        playButton.addActionListener(e -> startGame(frame));
        optionsButton.addActionListener(e -> showOptions());
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(playOnlineButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(playButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(optionsButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(bottomLeftWrapper, BorderLayout.SOUTH);
        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);

        playRepeatMusic("Media/Musics/menuMusic.mp3");
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
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);

        frame.revalidate();
        pacmanGame.requestFocus();

        stopMusic();
    }

    private static void startOnlineGame(JFrame frame) {
        frame.getContentPane().removeAll();
        PacmanOnline pacmanOnline = new PacmanOnline();
        frame.add(pacmanOnline);

        frame.revalidate();
        pacmanOnline.requestFocus();

        stopMusic();
    }

    private static void showOptions() {

    }
}