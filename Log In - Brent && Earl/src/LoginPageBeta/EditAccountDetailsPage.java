package LoginPageBeta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class EditAccountDetailsPage extends JFrame implements ActionListener {

    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton saveButton = new JButton("Save");

    User user;
    HashMap<String, User> logininfo;

    EditAccountDetailsPage(User user, HashMap<String, User> logininfo) {
        this.user = user;
        this.logininfo = logininfo;

        setTitle("Edit Account Details");
        setSize(300, 200);
        setLayout(null);

        ImageIcon icon = new ImageIcon("lib\\Icons\\Edit.jpg");
        setIconImage(icon.getImage());

        // Placeholder to set a background image
        ImageIcon backgroundImage = new ImageIcon("lib\\LoginBackground.jpg");
        Image scaledImage = backgroundImage.getImage().getScaledInstance(800, getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledBackgroundImage = new ImageIcon(scaledImage);
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 800, getHeight());

        JLabel usernameLabel = new JLabel("New Username:");
        usernameLabel.setBounds(10, 30, 100, 25);

        usernameField.setBounds(120, 30, 150, 25);
        usernameField.setText(user.getUsername());

        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setBounds(10, 70, 100, 25);

        passwordField.setBounds(120, 70, 150, 25);
        passwordField.setText(user.getPassword());

        saveButton.setBounds(90, 110, 120, 25);
        saveButton.addActionListener(this);
        saveButton.setBackground(new Color(52, 70, 85));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorderPainted(false);

        add(backgroundLabel);
        backgroundLabel.add(saveButton);
        backgroundLabel.add(passwordField);
        backgroundLabel.add(passwordLabel);
        backgroundLabel.add(usernameField);
        backgroundLabel.add(usernameLabel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String newUsername = usernameField.getText();
            String newPassword = new String(passwordField.getPassword());

            if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
                // Update the user's details
                if (updateUserDetailsInFile(user.getUsername(), newUsername, newPassword)) {
                    // Update successful
                    user.setUsername(newUsername);
                    user.setPassword(newPassword);
                    JOptionPane.showMessageDialog(this, "Details updated successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update details in file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        }
    }

    private boolean updateUserDetailsInFile(String oldUsername, String newUsername, String newPassword) {
        // Update user details in users.txt file
        String filename = "users.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User userInfo : logininfo.values()) {
                String username = userInfo.getUsername();
                String password = userInfo.getPassword();
                String position = userInfo.getPosition();

                if (username.equals(oldUsername)) {
                    // Update user with new details
                    writer.write(newUsername + "," + newPassword + "," + position + "\n");
                } else {
                    writer.write(username + "," + password + "," + position + "\n");
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
