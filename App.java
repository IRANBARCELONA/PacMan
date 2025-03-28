// چیزایی که مبشه تغییر داد رو نوشتم برات و چیزایی که از هوش مصنوعی هم گرفتم نوشتم

import javax.swing.*;
import java.awt.*;

public class App {
    private static final int ROW_COUNT = 22;
    private static final int COLUMN_COUNT = 19;
    private static final int TILE_SIZE = 32;
    private static final int BOARD_WIDTH = COLUMN_COUNT * TILE_SIZE + 14;
    private static final int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;
    private static Image BG;

    public static void main(String[] args) {
        // عکس بک گراند
        BG = new ImageIcon("Media/Images/background.png").getImage();

        JFrame frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // AI Generated(برای بک گراند)
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

        JButton playButton = createMenuButton("Play");
        JButton optionsButton = createMenuButton("Options");
        JButton exitButton = createMenuButton("Exit");

        playButton.addActionListener(e -> startGame(frame));
        optionsButton.addActionListener(e -> showOptions());
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(playButton);
        // فاصله دکمه ها از هم
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(optionsButton);
        // فاصله دکمه ها از هم
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(menuPanel, BorderLayout.CENTER); 
        frame.setContentPane(backgroundPanel); 
        frame.setVisible(true);
    }

    // تنظیمات دیگه مثل رنگ و بک گراند و سایز و غیره برای دکمه ها
    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(255, 0, 0, 200));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setPreferredSize(new Dimension(200, 60));
        button.setMaximumSize(new Dimension(200, 60));
        button.setFocusPainted(false);
        button.setOpaque(true);
        return button;
    }

    private static void startGame(JFrame frame) {
        frame.getContentPane().removeAll();
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        
        // AI Generated
        frame.revalidate();
        pacmanGame.requestFocus();
    }

    private static void showOptions() {
        
    }
}
