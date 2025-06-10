import javax.swing.*;
import java.awt.*;
import Online.GameUser;
import Online.Database;

public class SignUp extends JFrame {

    Database db = new Database();
    GameUser user;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;

    private static Image BG;

    public String Username;
    public String Password;

    private Runnable onLoginSuccess;

    public SignUp(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;

        setTitle("Sign Up Page");
        setSize(32 * 19, 22 * 32);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BG = new ImageIcon("Media/Images/firstScene2.png").getImage();

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

        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Sign Up");
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

        signUpButton = new JButton("Sign Up");
        signUpButton.setForeground(Color.BLACK);
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
        loginButton.addActionListener(e -> Auth());
        panel.revalidate();
        panel.repaint();
        add(panel);
        setVisible(true);
    }

    public void signUp() {
        if(usernameField.getText().equals("") || passwordField.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill all the fields");
        }
        else {
            Username = usernameField.getText();
            Password = new String(passwordField.getPassword());
            db.createUser(Username, Password);
            JOptionPane.showMessageDialog(null, "Sign Up Successful");
            dispose();

            SwingUtilities.invokeLater(() -> {
                new LoginPage(onLoginSuccess);
            });
        }
    }


    public void Auth() {
        LoginPage loginPage = new LoginPage(onLoginSuccess);
        dispose();

    }

}
