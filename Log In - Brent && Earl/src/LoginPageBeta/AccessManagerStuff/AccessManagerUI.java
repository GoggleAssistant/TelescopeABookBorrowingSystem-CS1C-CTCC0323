package LoginPageBeta.AccessManagerStuff;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import LoginPageBeta.AccessManagerStuff.AccessManagerButton.LendBookAction;
import LoginPageBeta.AccessManagerStuff.AccessManagerButton.ReturnBookAction;
import LoginPageBeta.AccessManagerStuff.AccessManagerButton.RenewBookAction;
import LoginPageBeta.AccessManagerStuff.AccessManagerButton.PenaltyButtonAction;

public class AccessManagerUI {

    private JPanel buttonPanel;
    private JButton lendBookButton;
    private JButton returnBookButton;
    private JButton renewBookButton;
    private JButton penaltyButton;
    private JPanel booksBorrowedPanel;
    private JScrollPane scrollPane;
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;

    public AccessManagerUI() {
        // Initialize panels and components
        buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(800, 60));

        lendBookButton = new JButton("Lend Book");
        returnBookButton = new JButton("Return Book");
        renewBookButton = new JButton("Renew Book");
        penaltyButton = new JButton("Penalty");

        lendBookButton.setPreferredSize(new Dimension(200, 50));
        returnBookButton.setPreferredSize(new Dimension(200, 50));
        renewBookButton.setPreferredSize(new Dimension(200, 50));
        penaltyButton.setPreferredSize(new Dimension(200, 50));

        lendBookButton.setFocusable(false);
        returnBookButton.setFocusable(false);
        renewBookButton.setFocusable(false);
        penaltyButton.setFocusable(false);

        lendBookButton.addActionListener(e -> new LendBookAction());
        returnBookButton.addActionListener(e -> new ReturnBookAction());
        renewBookButton.addActionListener(e -> new RenewBookAction());
        penaltyButton.addActionListener(e -> new PenaltyButtonAction());

        buttonPanel.add(lendBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(renewBookButton);
        buttonPanel.add(penaltyButton);

        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField();
        searchButton = new JButton("Search Borrow ID");

        // Set preferred sizes for the search field and button
        searchField.setPreferredSize(new Dimension(660, 30));
        searchButton.setPreferredSize(new Dimension(200, 30));

        searchButton.setFocusable(false);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add the action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BorrowIdSearch();
            }
        });

        // Create the booksBorrowedPanel and make it scrollable
        booksBorrowedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        booksBorrowedPanel.setPreferredSize(new Dimension(850, 450));

        scrollPane = new JScrollPane(booksBorrowedPanel);
        scrollPane.setPreferredSize(new Dimension(850, 450));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Set a custom unit increment
        verticalScrollBar.setUnitIncrement(10); // Scrollwheel sens

        loadBorrowedBooks(); // Load borrowed books details from file
    }

    //searching using the borrow id
    private void BorrowIdSearch() {
        String inputBorrowId = searchField.getText().trim();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("borrowed_books.txt"))) {
            String line;
            StringBuilder message = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Borrow ID: ")) {
                    String borrowID = line.substring(10).trim();
                    if (borrowID.equals(inputBorrowId)) {
                        found = true;
                        message.append(line).append("\n"); // Borrow ID
                        message.append(reader.readLine()).append("\n"); // Book title
                        message.append(reader.readLine()).append("\n"); // ISBN
                        message.append(reader.readLine()).append("\n"); // Borrower
                        message.append(reader.readLine()).append("\n"); // Deadline
                        message.append("------------------------").append("\n");
                        message.append("Contact Information").append("\n");
                        message.append(reader.readLine()).append("\n"); // Mobile Number
                        message.append(reader.readLine()).append("\n"); // Email Address
                        
                        JOptionPane.showMessageDialog(null, message.toString(), "Borrow Details", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "Borrow ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            booksBorrowedPanel.revalidate();
            booksBorrowedPanel.repaint();
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
    
        // Create JButton for the book with left-aligned text
        JButton button = new JButton("<html><div style='text-align: left; padding-left: 10px;'>" +
                "Borrow ID: " + borrowID + "<br>" + "<br>" +
                "Title: " + bookTitle + "<br>" +
                "Borrower: " + borrower + "<br>" +
                "Deadline: " + deadline + "<br>" +
                "</div></html>");

        button.setPreferredSize(new Dimension(280, 200)); // Set button size
        button.setHorizontalAlignment(SwingConstants.LEFT); // Ensure horizontal alignment is left
        button.setVerticalAlignment(SwingConstants.CENTER); // Ensure vertical alignment is top
        button.setFocusable(false);
        button.setBackground(Color.WHITE);

        if (booksBorrowedPanel.getComponentCount() % 3 == 0) {
            Dimension panelSize = booksBorrowedPanel.getPreferredSize();
            panelSize.height += 170; // Increase height by 200 for every set of 3 buttons
            booksBorrowedPanel.setPreferredSize(panelSize);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate deadlineDate = LocalDate.parse(deadline, formatter);
        LocalDate currentDate = LocalDate.now();
        
        // Determine color based on deadline comparison
        Color borderColor;
        if (deadlineDate.isBefore(currentDate)) {
            borderColor = new Color(252, 57, 0); // RGB for deadline after current date
        } else if (deadlineDate.isEqual(currentDate)) {
            borderColor = new Color(255, 197, 0); // RGB for deadline today
        } else {
            borderColor = new Color(54, 116, 156); // RGB for deadline before current date
        }
        
            // ActionListener for button click
        button.addActionListener(e -> {
            StringBuilder message = new StringBuilder();

            message.append("Borrow ID: ").append(borrowID).append("\n");
            message.append("Title: ").append(bookTitle).append("\n");
            message.append("Borrower: ").append(borrower).append("\n");
            message.append("Deadline: ").append(deadline).append("\n");

            message.append("------------------------\n");

            message.append("Contact Information").append("\n");
            message.append("Mobile Number: ").append(mobileNumber).append("\n");
            message.append("Email Address: ").append(emailAddress).append("\n");

            JOptionPane.showMessageDialog(null, message.toString(), "Borrow Details", JOptionPane.INFORMATION_MESSAGE);
        });

        // Create matte border with the determined color
        Border border = BorderFactory.createMatteBorder(0, 7, 0, 0, borderColor); // 7 pixels wide, left side only
        button.setBorder(border);
        
        // Add ActionListener to the button
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
        
                // Change background color on hover (optional)
                button.setBackground(new Color(240, 240, 240)); // Example: Change background color on hover
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
        
                // Reset background color on exit (optional)
                button.setBackground(Color.WHITE); // Example: Reset background color on hover exit
            }
        
            private void updateBorder() {
                // Create matte border with the updated widths
                Border hoverBorder = BorderFactory.createMatteBorder(currentWidthTop, 7, currentWidthBottom, currentWidthRight, borderColor);
                button.setBorder(hoverBorder);
            }
        });
        
        

    
        booksBorrowedPanel.add(button); // Add button to the panel
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
  
    public JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create a panel to hold the buttonPanel and the searchPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Create a panel to hold the topPanel and the scrollPane
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Access Manager UI");
            AccessManagerUI ui = new AccessManagerUI();
            frame.getContentPane().add(ui.getMainPanel());
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
