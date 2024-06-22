package LoginPageBeta.AccessManagerStuff.AccessManagerButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReturnBookAction extends JFrame {

    private JPanel returnBooksPanel;
    private JPanel leftPanel;
    private JLabel returnTitleLabel;
    private JTextPane returnDescriptionTextPane;
    
    private JLabel iconLabel; // Label for the icon

    public ReturnBookAction() {
        setTitle("Return Book Action");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Null layout for precise positioning

        // Initialize components for the left panel
        leftPanel = new JPanel(null); // Null layout for precise positioning
        leftPanel.setBackground(new Color(16, 19, 19));
        leftPanel.setBounds(0, 0, 300, getHeight());

        // Placeholder for icon
        ImageIcon icon = new ImageIcon("lib\\ReturnIcon.png"); // Replace with your image path
        icon = new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Scale to 100x100
        iconLabel = new JLabel(icon);
        iconLabel.setBounds(50, 25, 200, 200);
        leftPanel.add(iconLabel);

        returnTitleLabel = new JLabel("Return Book Details");
        returnTitleLabel.setForeground(Color.WHITE);
        returnTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        returnTitleLabel.setBounds(0, 250, 300, 30);
        returnTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(returnTitleLabel);

        returnDescriptionTextPane = new JTextPane();
        returnDescriptionTextPane.setText("This panel displays books eligible for return. Click on each book to return or view borrower information.");
        returnDescriptionTextPane.setEditable(false);
        returnDescriptionTextPane.setBackground(new Color(16, 19, 19));
        returnDescriptionTextPane.setForeground(Color.WHITE);
        returnDescriptionTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
        returnDescriptionTextPane.setBounds(25, 300, 250, 100);
        returnDescriptionTextPane.setBorder(null);
        leftPanel.add(returnDescriptionTextPane);

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

        searchButton.addActionListener(e -> {
            String searchQuery = searchTextField.getText();
            triggerMatchingButton(searchQuery);
        });

        getContentPane().add(searchButton);

        // Create the returnBooksPanel with GridLayout (2 columns, 3 hgap, 3 vgap)
        returnBooksPanel = new JPanel(new GridLayout(0, 2, 3, 3)); // 2 columns, 3 hgap, 3 vgap

        // Create the JScrollPane and add the returnBooksPanel to it
        JScrollPane scrollPane = new JScrollPane(returnBooksPanel);
        scrollPane.setBounds(300, 50, 487, 420); // Adjust position and size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Set a custom unit increment
        verticalScrollBar.setUnitIncrement(10); // Scrollwheel sens

        getContentPane().add(scrollPane); // Add the scrollPane to the content pane

        loadReturnableBooks(); // Load books eligible for return

        setVisible(true);
    }

        private void loadReturnableBooks() {
        List<String> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed_books.txt"))) {
            StringBuilder bookEntry = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                bookEntry.append(line).append("\n");
                if (line.equals("----------------------------------------")) {
                    books.add(bookEntry.toString());
                    bookEntry.setLength(0);
                }
            }
            if (bookEntry.length() > 0) {
                books.add(bookEntry.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        for (String bookEntry : books) {
            String deadline = extractValue(bookEntry, "Deadline:");
            if (deadline != null && !deadline.isEmpty()) {
                try {
                    LocalDate deadlineDate = LocalDate.parse(deadline, formatter);
                    if (deadlineDate.isEqual(currentDate)) {
                        returnButtonFactory(bookEntry, deadline, "\n", new Color(255, 197, 0));
                    } else if (deadlineDate.isAfter(currentDate)) {
                        returnButtonFactory(bookEntry, deadline, "\n", new Color(54, 116, 156));
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format for deadline: " + deadline);
                }
            }
        }

        returnBooksPanel.revalidate();
        returnBooksPanel.repaint();
    }

    private void returnButtonFactory(String bookEntry, String deadline, String returnStatus, Color borderColor) {
        String borrowID = extractValue(bookEntry, "Borrow ID:");
        String borrower = extractValue(bookEntry, "Borrower:");
        String mobileNumber = extractValue(bookEntry, "Mobile Number:");
        String emailAddress = extractValue(bookEntry, "Email Address:");
        String bookTitle = extractValue(bookEntry, "Book:");
    
        JButton button = new JButton("<html><div style='text-align: left; padding-left: 10px;'>" +
                "Borrow ID: " + borrowID + "<br><br>" +
                "Title: " + bookTitle + "<br>" +
                "Borrower: " + borrower + "<br>" +
                "Deadline: " + deadline + "<br>" +
                returnStatus + "<br>" +
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
    
            // Add labels and text fields for additional input (e.g., confirmation)
            panel.add(new JLabel("Borrow ID: " + borrowID));
            panel.add(new JLabel("Title: " + bookTitle));
            panel.add(new JLabel("Borrower: " + borrower));
    
            panel.add(new JLabel("----------------------------------------"));
            panel.add(new JLabel("Contact Information"));
            panel.add(new JLabel("Mobile Number: " + mobileNumber));
            panel.add(new JLabel("Email Address: " + emailAddress));
            panel.add(new JLabel("\n"));
    
            int option = JOptionPane.showOptionDialog(null, panel, "Return Book",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    new String[]{"Cancel", "Return"}, "Cancel");
    
            if (option == JOptionPane.NO_OPTION) {
                returnBook(borrowID);
            }
        });
    
        // Add mouse listener for hover effect
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
    
        returnBooksPanel.add(button);
    }
    
    private void returnBook(String borrowID) {
        try {
            File inputFile = new File("borrowed_books.txt");
            File tempFile = new File("temp_borrowed_books.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
    
            String currentLine;
            boolean skipEntry = false;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("Borrow ID: " + borrowID)) {
                    skipEntry = true; // Skip this book entry
                    continue;
                } else if (currentLine.equals("----------------------------------------")) {
                    if (skipEntry) {
                        skipEntry = false;
                        continue; // Skip the line separator after the entry
                    }
                }
    
                if (!skipEntry) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
    
            writer.close();
            reader.close();
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
            }
    
            // Refresh the displayed list of books after returning
            returnBooksPanel.removeAll(); // Remove all existing buttons
            loadReturnableBooks(); // Reload from the updated file
    
            JOptionPane.showMessageDialog(this, "Book returned successfully.",
                    "Return Successful", JOptionPane.INFORMATION_MESSAGE);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Revalidate and repaint the panel after removing all buttons
        returnBooksPanel.revalidate();
        returnBooksPanel.repaint();
    }
    

    private String extractValue(String entry, String key) {
        int index = entry.indexOf(key);
        if (index != -1) {
            int startIndex = index + key.length();
            int endIndex = entry.indexOf("\n", startIndex);
            if (endIndex != -1) {
                return entry.substring(startIndex, endIndex).trim();
            } else {
                return entry.substring(startIndex).trim();
            }
        }
        return "";
    }

    private void triggerMatchingButton(String borrowID) {
        for (Component component : returnBooksPanel.getComponents()) {
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
        SwingUtilities.invokeLater(ReturnBookAction::new);
    }
}
