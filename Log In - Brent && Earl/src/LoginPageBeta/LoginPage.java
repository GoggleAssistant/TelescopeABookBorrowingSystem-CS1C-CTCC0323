package LoginPageBeta;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

public class LoginPage implements ActionListener {

    JFrame frame = new JFrame();
    JButton loginButton = new JButton("LOGIN");
    JButton clearButton = new JButton("CLEAR");

    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();

    JLabel userIDLabel = new JLabel("USERNAME:");
    JLabel userPasswordLabel = new JLabel("PASSWORD:");

    JLabel messageLabel = new JLabel();
    JLabel logoLabel = new JLabel();
    HashMap<String, User> logininfo = new HashMap<String, User>();

    LoginPage(HashMap<String, User> loginInfoOriginal) {

        logininfo = loginInfoOriginal;

        ImageIcon logoIcon = new ImageIcon("lib\\Book Borrowing System Elements\\Telescope_Logo.png"); // Change the path to your logo image
        Image logoImage = logoIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(logoImage);
        logoLabel.setIcon(logoIcon);
        logoLabel.setBounds(135, 20, 150, 150);

        userIDLabel.setBounds(50, 200, 75, 25);
        userPasswordLabel.setBounds(50, 235, 75, 25);

        messageLabel.setBounds(75, 320, 250, 35);
        messageLabel.setFont(new Font(null, Font.BOLD, 20));

        userIDField.setBounds(145, 200, 200, 25);
        userPasswordField.setBounds(145, 235, 200, 25);

        loginButton.setBounds(145, 280, 90, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        loginButton.setBackground(new Color(52, 70, 85));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);

        clearButton.setBounds(255, 280, 90, 25);
        clearButton.setFocusable(false);
        clearButton.addActionListener(this);
        clearButton.setBackground(Color.red);
        clearButton.setForeground(Color.WHITE);
        clearButton.setBorderPainted(false);

        frame.setTitle("Telescope | The Book Borrowing System");
        frame.setIconImage(new ImageIcon("lib\\Icons\\LoginIcon.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setResizable(false);
        frame.setLayout(null);
        
        // Load background image
        ImageIcon backgroundImage = new ImageIcon("lib\\LoginBackground.jpg");
        Image scaledImage = backgroundImage.getImage().getScaledInstance(600, frame.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 600, frame.getHeight());
        frame.setContentPane(backgroundLabel); 
        
        // Add components to the frame
        frame.add(logoLabel);
        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(clearButton);
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            userIDField.setText("");
            userPasswordField.setText("");
        }

        if (e.getSource() == loginButton) {
            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

            if (logininfo.containsKey(userID)) {
                User user = logininfo.get(userID);
                if (user.getPassword().equals(password)) {
                    messageLabel.setForeground(Color.green);
                    messageLabel.setText("LOGIN SUCCESSFUL");
                    frame.dispose();
                    new WelcomePage(user, logininfo, messageLabel);
                } else {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("WRONG PASSWORD");
                }
            } else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("USERNAME NOT FOUND");
            }
        }
    }
}
