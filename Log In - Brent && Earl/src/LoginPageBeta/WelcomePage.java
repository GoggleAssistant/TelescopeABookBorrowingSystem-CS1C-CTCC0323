package LoginPageBeta;

import javax.swing.*;

import LoginPageBeta.AccessManagerStuff.AccessManagerUI;
import LoginPageBeta.CollectionManagerStuff.CollectionManagerUI;
import LoginPageBeta.OwnerStuff.OwnerUI;

import java.awt.*;
import java.util.HashMap;

public class WelcomePage {

    JFrame frame = new JFrame();

    JLabel welcomeLabel = new JLabel();
    JLabel userInfoLabel = new JLabel();

    CustomPanel welcomePanel = new CustomPanel();
    JPanel bodyPanel = new JPanel();

    JButton editAccountDetailsButton = new JButton("Edit Account Details");
    JButton logoutButton = new JButton("Logout");

    User user;
    HashMap<String, User> logininfo;

    WelcomePage(User user, HashMap<String, User> logininfo, JLabel messageLabel) {
        this.user = user;
        this.logininfo = logininfo;

        welcomeLabel.setFont(new Font(null, Font.BOLD, 30));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setText("Hello, " + user.getUsername());
        welcomeLabel.setBounds(10, 10, 400, 35);

        userInfoLabel.setFont(new Font(null, Font.PLAIN, 18));
        userInfoLabel.setForeground(Color.DARK_GRAY);
        userInfoLabel.setText("  " + user.getPosition());
        userInfoLabel.setBounds(15, 50, 400, 15);

        editAccountDetailsButton.setFocusable(false);
        editAccountDetailsButton.setBorderPainted(false);
        editAccountDetailsButton.addActionListener(e -> {
            new EditAccountDetailsPage(user, logininfo);
        });

        logoutButton.setFocusable(false);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        welcomePanel.setPreferredSize(new Dimension(1000, 115));
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.add(welcomeLabel, BorderLayout.WEST);
        welcomePanel.add(userInfoLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(logoutButton, BorderLayout.EAST);
        buttonPanel.add(editAccountDetailsButton, BorderLayout.WEST);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(welcomePanel, BorderLayout.NORTH);
        headerPanel.add(buttonPanel, BorderLayout.CENTER);

        bodyPanel.setBackground(Color.white);
        bodyPanel.setPreferredSize(new Dimension(1000, 585)); // Adjusted to fit the remaining space

        // Switch statement to set the background image based on the user's position
        switch (user.getPosition()) {
            case "Owner":
                setupOwnerUI();
                break;

            case "Collection Manager":
                setupCollectionManagerUI();
                break;

            case "Access Manager":
                setupAccessManagerUI();
                break;

            default:
                break;
        }

        frame.setLayout(new BorderLayout());
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void setupOwnerUI() {
        editAccountDetailsButton.setBackground(new Color(212, 202, 133)); // d4ca85
        logoutButton.setBackground(new Color(212, 202, 133)); // d4ca85
        welcomePanel.setBackgroundImage("lib/exotic.png");
        frame.setIconImage(new ImageIcon("lib\\Icons\\ExoticEngram.png").getImage());
        frame.setTitle("Owner Profile");
        frame.setLocationRelativeTo(null);

        OwnerUI ownerUI = new OwnerUI();
        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.add(ownerUI.getMainPanel(), BorderLayout.CENTER);
        bodyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
    }

    private void setupCollectionManagerUI() {
        editAccountDetailsButton.setBackground(new Color(206, 195, 255)); // cec3ff
        logoutButton.setBackground(new Color(206, 195, 255)); // cec3ff
        welcomePanel.setBackgroundImage("lib/legendary.png");
        frame.setIconImage(new ImageIcon("lib\\Icons\\LegendaryEngram.png").getImage());
        frame.setTitle("Manager Profile");
        frame.setLocationRelativeTo(null);
    
        CollectionManagerUI collectionManagerUI = new CollectionManagerUI();
        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.add(collectionManagerUI.getMainPanel(), BorderLayout.CENTER);
    }
    

    private void setupAccessManagerUI() {
        editAccountDetailsButton.setBackground(new Color(159, 212, 226)); // 9fd4e2
        logoutButton.setBackground(new Color(159, 212, 226)); // 9fd4e2
        welcomePanel.setBackgroundImage("lib/rare.png");
        frame.setIconImage(new ImageIcon("lib\\Icons\\RareEngram.png").getImage());
        frame.setTitle("Access Manager Profile");
        frame.setLocationRelativeTo(null);

        AccessManagerUI accessManagerUI = new AccessManagerUI();
        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.add(accessManagerUI.getMainPanel(), BorderLayout.CENTER);
    }

    class CustomPanel extends JPanel {
        private Image backgroundImage;

        public void setBackgroundImage(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                int width = getWidth();
                int height = getHeight();
                g.drawImage(backgroundImage, 0, 0, width, height, this);
            }
        }
    }
}
