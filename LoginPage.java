import javax.swing.*;
import java.awt.*;
import Online.GameUser;
import Online.Database;

public class LoginPage extends JFrame {

    Database db = new Database();

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    public String Username;
    public String Password;
    public Boolean UserName = false;
    public Boolean PassWord = false;

    private Runnable onLoginSuccess;

    public LoginPage(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;

        setTitle("Login Page");
        setSize(32 * 19, 22 * 32);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Login");
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

        loginButton = new JButton("Login");
        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(loginButton, gbc);

        signUpButton = new JButton("Sign Up");
        gbc.gridy++;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(signUpButton, gbc);

        signUpButton.addActionListener(e -> signUp());
        loginButton.addActionListener(e -> Auth());
        add(panel);
        setVisible(true);
    }

    public void signUp() {
        new SignUp(onLoginSuccess).setVisible(true);
        dispose();
    }

    public void Auth() {
        this.Username = usernameField.getText();
        this.Password = new String(passwordField.getPassword());

        if(this.Username.equals("") || this.Password.equals("")) {
            JOptionPane.showMessageDialog(this, "Error: Empty Fields");
        }
        else{

            App.user = db.getGameUserByUsername(this.Username);

            if(App.user != null) {
                this.UserName = true;
                String currectPass = App.user.getPassword();
                if(currectPass.equals(this.Password)) {
                    this.PassWord = true;
                }
            }

            if (UserName && PassWord) {
                dispose();
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            }
            else if (UserName && !PassWord) {
                JOptionPane.showMessageDialog(this, "Incorrect Password");
            }
            else {
                JOptionPane.showMessageDialog(this, "Error: User Not Found");
            }

        }


    }

}
