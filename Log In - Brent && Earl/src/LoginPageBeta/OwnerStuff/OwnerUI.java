package LoginPageBeta.OwnerStuff;

import javax.swing.*;

import LoginPageBeta.AccessManagerStuff.AccessManagerButton.LendBookAction;
import LoginPageBeta.AccessManagerStuff.AccessManagerButton.PenaltyButtonAction;
import LoginPageBeta.AccessManagerStuff.AccessManagerButton.RenewBookAction;
import LoginPageBeta.AccessManagerStuff.AccessManagerButton.ReturnBookAction;
import LoginPageBeta.CollectionManagerStuff.CollectionManagerButtons.addBookAction;
import LoginPageBeta.CollectionManagerStuff.CollectionManagerButtons.removeBookAction;
import LoginPageBeta.OwnerStuff.OwnerButtons.showBookCollection;
import LoginPageBeta.OwnerStuff.OwnerButtons.showBorrowedBooks;

import java.awt.*;

public class OwnerUI {

    private JButton lendBookButton;
    private JButton returnBookButton;
    private JButton renewBookButton;
    private JButton penaltyButton;
    private JButton showBorrowedBooksButton;

    private JButton addBookButton;
    private JButton removeBookButton;
    private JButton showBookCollectionButton;

    public OwnerUI() {
        // Initialize buttons for Access Manager Button Panel
        lendBookButton = createMultilineButton("Lend<br>Book");
        returnBookButton = createMultilineButton("Return<br>Book");
        renewBookButton = createMultilineButton("Renew<br>Book");
        penaltyButton = new JButton("Penalty");
        showBorrowedBooksButton = new JButton("Show Borrowed Books");

        // Initialize buttons for Collection Manager Panel
        addBookButton = createMultilineButton("Add<br>Book");
        removeBookButton = createMultilineButton("Remove<br>Book");
        showBookCollectionButton = new JButton("Show Book Collection");

        // Set background color for Access Manager buttons
        Color accessManagerColor = new Color(206, 195, 255);
        lendBookButton.setBackground(accessManagerColor);
        returnBookButton.setBackground(accessManagerColor);
        renewBookButton.setBackground(accessManagerColor);
        penaltyButton.setBackground(accessManagerColor);
        showBorrowedBooksButton.setBackground(accessManagerColor);

        // Set background color for Collection Manager buttons
        Color collectionManagerColor = new Color(159, 212, 226);
        addBookButton.setBackground(collectionManagerColor);
        removeBookButton.setBackground(collectionManagerColor);
        showBookCollectionButton.setBackground(collectionManagerColor);

        // Set button sizes to be square (adjust as needed)
        Dimension buttonSize = new Dimension(100, 100);
        lendBookButton.setPreferredSize(buttonSize);
        returnBookButton.setPreferredSize(buttonSize);
        renewBookButton.setPreferredSize(buttonSize);
        penaltyButton.setPreferredSize(buttonSize);
        showBorrowedBooksButton.setPreferredSize(new Dimension(205, 50));

        addBookButton.setPreferredSize(buttonSize);
        removeBookButton.setPreferredSize(buttonSize);
        showBookCollectionButton.setPreferredSize(new Dimension(205, 50));

        // Set bounds for buttons (assuming null layout approach)
        lendBookButton.setBounds(10, 10, buttonSize.width, buttonSize.height);
        returnBookButton.setBounds(120, 10, buttonSize.width, buttonSize.height);
        renewBookButton.setBounds(10, 120, buttonSize.width, buttonSize.height);
        penaltyButton.setBounds(120, 120, buttonSize.width, buttonSize.height);
        showBorrowedBooksButton.setBounds(10, 230, 200, 50);

        addBookButton.setBounds(10, 10, buttonSize.width, buttonSize.height);
        removeBookButton.setBounds(120, 10, buttonSize.width, buttonSize.height);

        showBookCollectionButton.setBounds(10, 120, 215, 50);

        lendBookButton.setFocusable(false);
        returnBookButton.setFocusable(false);
        renewBookButton.setFocusable(false);
        penaltyButton.setFocusable(false);
        showBorrowedBooksButton.setFocusable(false);
        addBookButton.setFocusable(false);
        removeBookButton.setFocusable(false);
        showBookCollectionButton.setFocusable(false);

        
        lendBookButton.addActionListener(e -> new LendBookAction());
        returnBookButton.addActionListener(e -> new ReturnBookAction());
        renewBookButton.addActionListener(e -> new RenewBookAction());
        penaltyButton.addActionListener(e -> new PenaltyButtonAction());

        addBookButton.addActionListener(e -> new addBookAction());
        removeBookButton.addActionListener(e -> new removeBookAction());

        showBookCollectionButton.addActionListener(e -> new showBookCollection());
        showBorrowedBooksButton.addActionListener(e -> new showBorrowedBooks());


    }

    private JButton createMultilineButton(String text) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }

    public JPanel getMainPanel() {
        JPanel mainPanel = new JPanel(null); // Use null layout for absolute positioning
        mainPanel.setPreferredSize(new Dimension(800, 400)); // Adjust panel size as needed

        // Left panel setup
        JPanel leftPanel = new JPanel(null); // Use null layout for absolute positioning
        leftPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        leftPanel.setBounds(450, 50, 300, 300); // Anchor to the east (adjust x position)

        // Subtitle for Access Manager Panel
        JLabel accessManagerLabel = new JLabel("Access Manager");
        accessManagerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        accessManagerLabel.setBounds(0, 0, 300, 30); // Position and size of the label within leftPanel
        accessManagerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(accessManagerLabel);

        // Add Access Manager Button Panel to left panel
        JPanel accessManagerButtonPanel = createAccessManagerButtonPanel();
        accessManagerButtonPanel.setBounds(0, 30, 300, 270); // Position and size of the button panel within leftPanel
        leftPanel.add(accessManagerButtonPanel);

        // Right panel setup
        JPanel rightPanel = new JPanel(null); // Use null layout for absolute positioning
        rightPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        rightPanel.setBounds(50, 50, 300, 300); // Anchor to the west (adjust x position)

        // Subtitle for Collection Manager Panel
        JLabel collectionManagerLabel = new JLabel("Collection Manager");
        collectionManagerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        collectionManagerLabel.setBounds(0, 0, 300, 30); // Position and size of the label within rightPanel
        collectionManagerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(collectionManagerLabel);

        // Add Collection Manager Button Panel to right panel
        JPanel collectionManagerButtonPanel = createCollectionManagerButtonPanel();
        collectionManagerButtonPanel.setBounds(0, 30, 300, 270); // Position and size of the button panel within rightPanel
        rightPanel.add(collectionManagerButtonPanel);

        // Add left and right panels to main panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        return mainPanel;
    }

    private JPanel createAccessManagerButtonPanel() {
        JPanel panel = new JPanel();

        panel.add(lendBookButton);
        panel.add(returnBookButton);
        panel.add(renewBookButton);
        panel.add(penaltyButton);
        panel.add(showBorrowedBooksButton);

        return panel;
    }

    private JPanel createCollectionManagerButtonPanel() {
        JPanel panel = new JPanel();

        panel.add(addBookButton);
        panel.add(removeBookButton);
        panel.add(showBookCollectionButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Owner UI");
            OwnerUI ui = new OwnerUI();
            frame.getContentPane().add(ui.getMainPanel());
            frame.setSize(800, 400);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
