import javax.swing.*;
import java.awt.*;
import Online.GameUser;
import Online.Database;

public class SignUp extends JFrame {

    Database db = new Database();
    GameUser user;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    public String Username;
    public String Password;

    private Runnable onLoginSuccess;

    public SignUp(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;

        setTitle("Login Page");
        setSize(32 * 19, 22 * 32);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        panel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        gbc.gridy++;
        panel.add(usernameLabel, gbc);
        gbc.gridy++;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridy++;
        panel.add(passwordField, gbc);

        signUpButton = new JButton("Sign Up");
        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(signUpButton, gbc);


        loginButton = new JButton("Login");
        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(loginButton, gbc);

        signUpButton.addActionListener(e -> signUp());
        loginButton.addActionListener(e -> Auth());
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
