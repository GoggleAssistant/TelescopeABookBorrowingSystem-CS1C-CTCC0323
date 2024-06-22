package LoginPageBeta.OwnerStuff.OwnerButtons;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeParseException;

public class showBorrowedBooks extends JFrame {

    private JPanel renewBooksPanel;
    
    private JLabel iconLabel;
    private JPanel leftPanel;
    private JLabel renewTitleLabel;
    private JTextPane renewDescriptionTextPane;

    public showBorrowedBooks() {
        setTitle("Borrowed Books");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Null layout for precise positioning

        // Initialize components for the left panel
        leftPanel = new JPanel(null); // Null layout for precise positioning
        leftPanel.setBackground(new Color(16, 19, 19));
        leftPanel.setBounds(0, 0, 300, getHeight());

        // Placeholder for icon
        ImageIcon icon = new ImageIcon("lib\\BorrowedBooksIcon.png"); // Replace with your image path
        icon = new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Scale to 100x100
        iconLabel = new JLabel(icon);
        iconLabel.setBounds(50, 25, 200, 200);
        leftPanel.add(iconLabel);


        renewTitleLabel = new JLabel("Borrowed Books");
        renewTitleLabel.setForeground(Color.WHITE);
        renewTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        renewTitleLabel.setBounds(0, 250, 300, 30);
        renewTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(renewTitleLabel);

        renewDescriptionTextPane = new JTextPane();
        renewDescriptionTextPane.setText("This panel shows all the currently borrowed books. Click on a borrowed book to see more details.");
        renewDescriptionTextPane.setEditable(false);
        renewDescriptionTextPane.setBackground(new Color(16, 19, 19));
        renewDescriptionTextPane.setForeground(Color.WHITE);
        renewDescriptionTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
        renewDescriptionTextPane.setBounds(25, 300, 250, 100);
        renewDescriptionTextPane.setBorder(null);
        leftPanel.add(renewDescriptionTextPane);

        getContentPane().add(leftPanel);

        // JTextField for search (if needed)
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(310, 10, 300, 30); // Adjust position and size
        getContentPane().add(searchTextField);

        // JButton for search (if needed)
        JButton searchButton = new JButton("Search Borrow ID");
        searchButton.setBounds(620, 10, 150, 30); // Adjust position and size
        searchButton.setBackground(new Color(65, 88, 108));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false); 
        searchButton.setFocusable(false);

        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(122, 149, 171));
                searchButton.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBorderPainted(false); // Hide border
                searchButton.setBackground(new Color(65, 88, 108));
                searchButton.setForeground(Color.WHITE);
            }
        });

        searchButton.addActionListener(e -> {
            String searchQuery = searchTextField.getText();
            triggerMatchingButton(searchQuery);
        });

        getContentPane().add(searchButton);

        // Create the renewBooksPanel with GridLayout (2 columns, 3 hgap, 3 vgap)
        renewBooksPanel = new JPanel(new GridLayout(0, 2, 3, 3)); // 2 columns, 3 hgap, 3 vgap

        // Create the JScrollPane and add the renewBooksPanel to it
        JScrollPane scrollPane = new JScrollPane(renewBooksPanel);
        scrollPane.setBounds(300, 50, 487, 420); // Adjust position and size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Set a custom unit increment
        verticalScrollBar.setUnitIncrement(10); // Scrollwheel sens

        getContentPane().add(scrollPane); // Add the scrollPane to the content pane

        loadRenewableBooks(); // Load books eligible for renewal

        setVisible(true);
    }

    private void loadRenewableBooks() {
        List<String> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed_books.txt"))) {
            StringBuilder bookEntry = new StringBuilder(); // Declare bookEntry here
            String line;
            while ((line = reader.readLine()) != null) {
                bookEntry.append(line).append("\n");
                if (line.equals("----------------------------------------")) {
                    books.add(bookEntry.toString());
                    bookEntry.setLength(0); // Clear StringBuilder for new entry
                }
            }
            // Process the last book entry
            if (bookEntry.length() > 0) {
                books.add(bookEntry.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display all books from the file
        for (String bookEntry : books) {
            buttonFactory(bookEntry, "\n", new Color(255, 197, 0)); // Always display the button
        }

        // Revalidate and repaint the panel after adding all buttons
        renewBooksPanel.revalidate();
        renewBooksPanel.repaint();
    }

    private void buttonFactory(String bookEntry, String renewalStatus, Color borderColor) {
        String borrowID = extractValue(bookEntry, "Borrow ID:");
        String borrower = extractValue(bookEntry, "Borrower:");
        String mobileNumber = extractValue(bookEntry, "Mobile Number:");
        String emailAddress = extractValue(bookEntry, "Email Address:");
        String bookTitle = extractValue(bookEntry, "Book:");
        String deadline = extractValue(bookEntry, "Deadline:");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate deadlineDate;
        try {
            deadlineDate = LocalDate.parse(deadline, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return; // Exit the method if the date cannot be parsed
        }
        // Determine the border color based on the deadline
        
        LocalDate currentDate = LocalDate.now();
        if (deadlineDate.isBefore(currentDate)) {
            borderColor = new Color(252, 57, 0); // RGB for deadline after current date
        } else if (deadlineDate.isEqual(currentDate)) {
            borderColor = new Color(255, 197, 0); // RGB for deadline today
        } else {
            borderColor = new Color(54, 116, 156); // RGB for deadline before current date
        }



        JButton button = new JButton("<html><div style='text-align: left; padding-left: 10px;'>" +
                "Borrow ID: " + borrowID + "<br><br>" +
                "Title: " + bookTitle + "<br>" +
                "Borrower: " + borrower + "<br>" +
                "Deadline: " + deadline + "<br>" +
                renewalStatus + "<br>" +
                "</div></html>");
        button.setPreferredSize(new Dimension(200, 120));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setFocusable(false);
        button.setBackground(Color.WHITE);

        Border border = BorderFactory.createMatteBorder(0, 7, 0, 0, borderColor);
        button.setBorder(border);

        // Add ActionListener to the button
        button.addActionListener(e -> {
            // Display the Borrow ID, Mobile Number, and Email Address in the option dialog
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1));
        
            // Add labels for Borrow ID, Title, Borrower, Contact Information
            panel.add(new JLabel("Borrow ID: " + borrowID));
            panel.add(new JLabel("Title: " + bookTitle));
            panel.add(new JLabel("Borrower: " + borrower));
            panel.add(new JLabel("Deadline: " + deadline));
        
            panel.add(new JLabel("----------------------------------------"));
            panel.add(new JLabel("Contact Information"));
            panel.add(new JLabel("Mobile Number: " + mobileNumber));
            panel.add(new JLabel("Email Address: " + emailAddress));
        
            JOptionPane.showOptionDialog(null, panel, "Details",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"OK"}, "OK");
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Border originalBorder = button.getBorder(); // Store the original button border
            private final Color borderColor = ((MatteBorder) originalBorder).getMatteColor();

            private final int targetWidth = 7; // Target width for the border on hover for non-left sides
            private final int delay = 20; // Delay between steps in milliseconds

            private Timer timer;
            private int currentWidthTop = 0; // Current width for top side
            private int currentWidthRight = 0; // Current width for right side
            private int currentWidthBottom = 0; // Current width for bottom side

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Stop any existing animation timer
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }

                // Initialize a new animation timer
                timer = new Timer(delay, e -> {
                    currentWidthTop = Math.min(currentWidthTop + 1, targetWidth); // Increase width gradually
                    currentWidthRight = Math.min(currentWidthRight + 1, targetWidth); // Increase width gradually
                    currentWidthBottom = Math.min(currentWidthBottom + 1, targetWidth); // Increase width gradually
                    updateBorder();
                    if (currentWidthTop >= targetWidth && currentWidthRight >= targetWidth && currentWidthBottom >= targetWidth) {
                        timer.stop();
                    }
                });
                timer.start();

                button.setBackground(new Color(240, 240, 240)); // Change background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Stop any existing animation timer
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }

                // Initialize a new animation timer
                timer = new Timer(delay, e -> {
                    currentWidthTop = Math.max(currentWidthTop - 1, 0); // Decrease width gradually
                    currentWidthRight = Math.max(currentWidthRight - 1, 0); // Decrease width gradually
                    currentWidthBottom = Math.max(currentWidthBottom - 1, 0); // Decrease width gradually
                    updateBorder();
                    if (currentWidthTop <= 0 && currentWidthRight <= 0 && currentWidthBottom <= 0) {
                        timer.stop();
                    }
                });
                timer.start();
            }

            private void updateBorder() {
                Border hoverBorder = BorderFactory.createMatteBorder(currentWidthTop, 7, currentWidthBottom, currentWidthRight, borderColor);
                button.setBorder(hoverBorder);
            }
        });
    
        renewBooksPanel.add(button);
    }

    private String extractValue(String entry, String key) {
        int index = entry.indexOf(key);
        if (index != -1) {
            int startIndex = index + key.length();
            int endIndex = entry.indexOf("\n", startIndex);
            if (endIndex != -1) {
                return entry.substring(startIndex, endIndex).trim();
            }
        }
        return "";
    }

    private void triggerMatchingButton(String borrowID) {
        for (Component component : renewBooksPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String buttonText = button.getText();
                if (buttonText.contains("Borrow ID: " + borrowID)) {
                    button.doClick(); // Trigger the action listener of the matching button
                    return; // Exit the loop once a match is found
                }
            }
        }
        // Handle the case where no matching borrow ID is found
        JOptionPane.showMessageDialog(this, "Borrow ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(showBorrowedBooks::new);
    }
}
