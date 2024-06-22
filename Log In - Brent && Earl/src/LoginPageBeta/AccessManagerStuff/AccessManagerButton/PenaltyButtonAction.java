package LoginPageBeta.AccessManagerStuff.AccessManagerButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PenaltyButtonAction extends JFrame {

    private JPanel deadlineBooksPanel;
    private JPanel leftPanel; // New panel for the left side
    private JLabel penaltyTitleLabel;
    private JTextPane penaltyDescriptionTextPane;
    private JLabel iconLabel; // Label for the icon
    

    public PenaltyButtonAction() {
        setTitle("Penalty Button Action");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Use null layout for precise positioning

        // Initialize components for the left panel
        leftPanel = new JPanel(null); // Use null layout for precise positioning
        leftPanel.setBackground(new Color(16, 19, 19));
        leftPanel.setBounds(0, 0, 300, getHeight()); // Set width and full height of the frame
        
        // Placeholder for icon
        ImageIcon icon = new ImageIcon("lib\\PenaltyIcon.png"); // Replace with your image path
        icon = new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Scale to 100x100
        iconLabel = new JLabel(icon);
        iconLabel.setBounds(50, 25, 200, 200);
        leftPanel.add(iconLabel);

        penaltyTitleLabel = new JLabel("Penalty");
        penaltyTitleLabel.setForeground(Color.WHITE);
        penaltyTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        penaltyTitleLabel.setBounds(0, 250, 300, 30);
        penaltyTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(penaltyTitleLabel);

        penaltyDescriptionTextPane = new JTextPane();
        penaltyDescriptionTextPane.setText("This panel displays penalty details for overdue books. Click on each book to view borrower information.");
        penaltyDescriptionTextPane.setEditable(false);
        penaltyDescriptionTextPane.setBackground(new Color(16, 19, 19));
        penaltyDescriptionTextPane.setForeground(Color.WHITE);
        penaltyDescriptionTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
        penaltyDescriptionTextPane.setBounds(25, 300, 250, 100);
        penaltyDescriptionTextPane.setBorder(null);
        leftPanel.add(penaltyDescriptionTextPane);

        getContentPane().add(leftPanel); // Add left panel to the frame

        // JTextField for search
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(310, 10, 300, 30); // Adjust position and size as needed
        getContentPane().add(searchTextField);

        // JButton for search
        JButton searchButton = new JButton("Search Borrow ID");
        searchButton.setBounds(620, 10, 150, 30); // Adjust position and size as needed

        //search button hover shit
        searchButton.setBackground(new Color(65, 88, 108));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false); // Hide default border
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
        getContentPane().add(searchButton);

        searchButton.addActionListener(e -> {
            String searchQuery = searchTextField.getText();
            triggerMatchingButton(searchQuery);
        });

        // Create the deadlineBooksPanel with GridLayout (2 columns, 3 hgap, 3 vgap)
        deadlineBooksPanel = new JPanel(new GridLayout(0, 2, 3, 3)); // 2 columns, 3 hgap, 3 vgap

        // Create the JScrollPane and add the deadlineBooksPanel to it
        JScrollPane scrollPane = new JScrollPane(deadlineBooksPanel);
        scrollPane.setBounds(300, 50, 487, 420); // Adjusted position and size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Set a custom unit increment
        verticalScrollBar.setUnitIncrement(10); // Scrollwheel sens

        getContentPane().add(scrollPane); // Add the scrollPane directly to the content pane

        loadBorrowedBooks(); // Load borrowed books details from file

        setVisible(true);
    }

    private void triggerMatchingButton(String borrowID) {
        for (Component component : deadlineBooksPanel.getComponents()) {
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

    private void loadBorrowedBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed_books.txt"))) {
            StringBuilder bookEntry = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Borrow ID: ")) {
                    if (bookEntry.length() > 0) {
                        borrowedBookButtonFactory(bookEntry.toString());
                        bookEntry.setLength(0); // Clear StringBuilder for new entry
                    }
                }
                bookEntry.append(line).append("\n"); // Append line to StringBuilder
            }
            // Process the last book entry
            if (bookEntry.length() > 0) {
                borrowedBookButtonFactory(bookEntry.toString());
            }

            // After adding all buttons, revalidate and repaint the panel
            deadlineBooksPanel.revalidate();
            deadlineBooksPanel.repaint();
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading error
        }
    }

    private void borrowedBookButtonFactory(String entry) {
    // Extract details from the entry
    String borrower = extractValue(entry, "Borrower:");
    String mobileNumber = extractValue(entry, "Mobile Number:");
    String emailAddress = extractValue(entry, "Email Address:");
    String deadline = extractValue(entry, "Deadline:");
    String borrowID = extractValue(entry, "Borrow ID:");

    // Remove "Book: " from the book title
    String bookTitle = extractBookTitle(entry);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate deadlineDate = LocalDate.parse(deadline, formatter);
    LocalDate currentDate = LocalDate.now();

    // Calculate penalty days and amount
    long penaltyDays = ChronoUnit.DAYS.between(deadlineDate, currentDate);
    int penaltyAmount = 5; // Penalty amount per day in pesos
    int totalPenalty = (int) (penaltyDays * penaltyAmount);

    // Only add buttons for books that are past the deadline
    if (deadlineDate.isBefore(currentDate)) {
        JButton button = new JButton("<html><div style='text-align: left; padding-left: 10px;'>" +
                "Borrow ID: " + borrowID + "<br><br>" +
                "Title: " + bookTitle + "<br>" +
                "Borrower: " + borrower + "<br>" +
                "Deadline: " + deadline + "<br>" +
                "<font color='red'>Penalty: PHP " + totalPenalty + "</font><br>" +
                "</div></html>");
        button.setPreferredSize(new Dimension(200, 120)); // Adjusted button size for penalty text
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setVerticalAlignment(SwingConstants.CENTER); // Adjusted to show all text properly
        button.setFocusable(false);
        button.setBackground(Color.WHITE);

        // Create matte border with the determined color
        Color borderColor = new Color(252, 57, 0); // Red color for past deadline
        Border originalBorder = BorderFactory.createMatteBorder(0, 7, 0, 0, borderColor); // 7 pixels wide, left side only
        button.setBorder(originalBorder);

        // Add ActionListener to the button
        button.addActionListener(e -> {
            // Display the Borrow ID, Mobile Number, and Email Address in the option dialog
            int choice = JOptionPane.showOptionDialog(null, "Borrow ID: " + borrowID + "\n" +
                            "Title: " + bookTitle + "\n" +
                            "Borrower: " + borrower + "\n" +
                            "Deadline: " + deadline + "\n" +
                            "Penalty: PHP " + totalPenalty + "\n\n" +
                            "Contact Information\n" +
                            "Mobile Number: " + mobileNumber + "\n" +
                            "Email Address: " + emailAddress + "\n\n" +
                            "Would you like to lift the penalty?",
                    "Borrow Details", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Cancel", "Lift Penalty"}, "Cancel");

            // Handle the user choice
            if (choice == JOptionPane.YES_OPTION) {
                // User chose Cancel, do nothing
            } else if (choice == JOptionPane.NO_OPTION) {
                // User chose Lift Penalty, remove the corresponding log from borrowed_books.txt
                removeBorrowedBookEntry(borrowID);
            }
        });

        // Add hover animation to the button
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

        // Add button to deadlineBooksPanel
        deadlineBooksPanel.add(button);

        // Revalidate and repaint the panel after adding each button
        deadlineBooksPanel.revalidate();
        deadlineBooksPanel.repaint();
    }
}


    private void removeBorrowedBookEntry(String borrowID) {
        try {
            File inputFile = new File("borrowed_books.txt");
            File tempFile = new File("temp_borrowed_books.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
    
            String currentLine;
            boolean entryFound = false;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("Borrow ID: " + borrowID)) {
                    entryFound = true;
                    // Skip the lines until the separator is reached
                    while (!currentLine.equals("----------------------------------------")) {
                        currentLine = reader.readLine();
                    }
                } else {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
    
            writer.close();
            reader.close();
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            }
    
            // Refresh the displayed list of books after removing an entry
            deadlineBooksPanel.removeAll(); // Remove all existing buttons
            loadBorrowedBooks(); // Reload from the updated file
    
            if (!entryFound) {
                JOptionPane.showMessageDialog(this, "Borrow ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Penalty lifted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private String extractBookTitle(String entry) {
        int index = entry.indexOf("Book: ");
        if (index != -1) {
            int startIndex = index + "Book: ".length();
            int endIndex = entry.indexOf("\n", startIndex);
            if (endIndex != -1) {
                return entry.substring(startIndex, endIndex).trim();
            }
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PenaltyButtonAction::new);
    }
}
