import Online.Database;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.*;

public class LoginPage extends JFrame {

    Database db = new Database();

    private static Image BG;
    private static int xi = 0;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public String Username;
    public String Password;
    public Boolean UserName = false;
    public Boolean PassWord = false;

    private Runnable onLoginSuccess;

    ScheduledExecutorService imageScheduler;

    public LoginPage(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;

        setTitle("Login Page");
        setSize(32 * 19, 22 * 32);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BG = new ImageIcon("Media/Images/firstScene.png").getImage();

        JPanel panel;
        panel = new JPanel() {
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

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                BG = new ImageIcon("Media/Images/firstScene2.png").getImage();

                panel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                JLabel titleLabel = new JLabel("Login");
                titleLabel.setFont(new Font("Calibri", Font.BOLD, 35));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(-20, 0, 15, 0);
                panel.add(titleLabel, gbc);


                JLabel usernameLabel = new JLabel("Username:");
                usernameLabel.setFont(new Font("Calibri", Font.BOLD, 15));
                usernameField = new JTextField(20);
                usernameField.setFont(new Font("Calibri", Font.PLAIN, 14));
                usernameField.setBackground(new Color(240, 240, 240));
                usernameField.setForeground(Color.BLACK);
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                gbc.gridy++;
                gbc.insets = new Insets(35, 0, 10, 0);
                panel.add(usernameLabel, gbc);
                gbc.gridy++;
                gbc.insets = new Insets(10, 0, 15, 0);
                panel.add(usernameField, gbc);

                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setFont(new Font("Calibri", Font.BOLD, 15));
                passwordField = new JPasswordField(20);
                passwordField.setFont(new Font("Calibri", Font.PLAIN, 14));
                passwordField.setBackground(new Color(240, 240, 240));
                passwordField.setForeground(Color.BLACK);
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
                gbc.gridy++;
                gbc.insets = new Insets(15, 0, 10, 0);
                panel.add(passwordLabel, gbc);
                gbc.gridy++;
                gbc.insets = new Insets(10, 0, 15, 0);
                panel.add(passwordField, gbc);

                JButton signUpButton = new JButton("Sign Up");

                signUpButton.setBackground(new Color(173, 216, 230));

                signUpButton.setForeground(Color.BLACK); // متن سیاه
                signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
                signUpButton.setFocusPainted(false);
                signUpButton.setContentAreaFilled(true);
                signUpButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                signUpButton.setOpaque(true);

                signUpButton.setFont(new Font("Calibri", Font.BOLD, 16));
                gbc.gridy++;
                gbc.insets = new Insets(30, 0, 30, 0);
                panel.add(signUpButton, gbc);

                JButton loginButton = App.createMenuButton("Login");
                loginButton.setFont(new Font("Calibri", Font.BOLD, 16));
                gbc.gridy++;
                gbc.insets = new Insets(20, 14, -110, 0);
                panel.add(loginButton, gbc);

                signUpButton.addActionListener(e -> signUp());
                loginButton.addActionListener(e -> {
                    xi = 1;
                    if (xi == 1) {
                        BG = new ImageIcon("Media/Images/firstScene3.png").getImage();
                        xi = 0;
                    }

                    panel.revalidate();
                    panel.repaint();
                    Auth();
                });

                panel.revalidate();
                panel.repaint();

            }
        }, 2000);
        add(panel);
        setVisible(true);
    }

    public void signUp() {
        new SignUp(onLoginSuccess).setVisible(true);
        dispose();
    }

    public void Auth() {
        /*this.Username = usernameField.getText();
        this.Password = new String(passwordField.getPassword());

        if (this.Username.equals("") || this.Password.equals("")) {
            JOptionPane.showMessageDialog(this, "Error: Empty Fields");
        } else {

 */
            App.user = db.getGameUserByUsername(this.Username);

            /*if (App.user != null) {
                this.UserName = true;
                String currectPass = App.user.getPassword();
                if (currectPass.equals(this.Password)) {
                    this.PassWord = true;
                }
            }
*/
            PassWord = true;
            UserName = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (UserName && PassWord) {
                        dispose();
                        if (onLoginSuccess != null) {
                            onLoginSuccess.run();
                        }
                    }
                }
            }, 2000);

            /*if (UserName && !PassWord) {
                JOptionPane.showMessageDialog(this, "Incorrect Password");
            }
            if (!UserName) {
                JOptionPane.showMessageDialog(this, "Error: User Not Found");
            }

        }*/


    }
}


