package LoginPageBeta.AccessManagerStuff.AccessManagerButton;

import javax.swing.*;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import LoginPageBeta.BookReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class LendBookAction extends JFrame {

    private JPanel leftPanel; // New panel for the left side
    private JLabel lendBookTitleLabel;
    private JTextPane descriptionTextPane;
    private JLabel iconLabel; // Label for the icon

    private JLabel bookIsbnLabel;
    private JTextField bookIsbnTextField;
    private JButton searchButton;
    private JLabel matchingBookLabel;
    private JLabel borrowerLabel;
    private JTextField borrowerTextField;
    private JTextField mobileNumberTextField;
    private JTextField emailAddressTextField;
    private JLabel mobileNumberLabel;
    private JLabel emailAddressLabel;
    private JTextField daysTextField;
    private JLabel daysLabel;
    private JButton generateButton; // New Generate button

    public LendBookAction() {
        setTitle("Lend Book Action");
        setSize(750, 500); // Increased width to accommodate left panel
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Use null layout for absolute positioning

        // Initialize components for the left panel
        leftPanel = new JPanel();
        leftPanel.setBackground(new Color(16, 19, 19));
        leftPanel.setBounds(0, 0, 300, getHeight()); // Set width and full height of the frame
        leftPanel.setLayout(null); // Null layout for precise positioning

        // Placeholder for icon
        ImageIcon icon = new ImageIcon("lib\\LendBookIcon.png"); // Replace with your image path
        icon = new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Scale to 100x100
        iconLabel = new JLabel(icon);
        iconLabel.setBounds(50, 25, 200, 200);
        leftPanel.add(iconLabel);

        lendBookTitleLabel = new JLabel("Lend Book");
        lendBookTitleLabel.setForeground(Color.WHITE);
        lendBookTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        lendBookTitleLabel.setBounds(0, 200, 300, 30);
        lendBookTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(lendBookTitleLabel);

        descriptionTextPane = new JTextPane();
        descriptionTextPane.setText("This panel allows you to lend books to borrowers. Enter the book ISBN, borrower details, and days borrowed to generate a lending record.");
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setBackground(new Color(16, 19, 19));
        descriptionTextPane.setForeground(Color.WHITE);
        descriptionTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionTextPane.setBounds(25, 250, 250, 100);
        descriptionTextPane.setBorder(null);
        leftPanel.add(descriptionTextPane);

        add(leftPanel); // Add left panel to the frame

        // Components for the main content area
        bookIsbnLabel = new JLabel("Book ISBN:");
        bookIsbnTextField = new JTextField();
        searchButton = new JButton("Search");
        matchingBookLabel = new JLabel("");

        borrowerLabel = new JLabel("Borrower:");
        borrowerTextField = new JTextField();

        mobileNumberLabel = new JLabel("Mobile Number:");
        mobileNumberTextField = new JTextField();

        emailAddressLabel = new JLabel("Email Address:");
        emailAddressTextField = new JTextField();

        daysLabel = new JLabel("Days Borrowed:");
        daysTextField = new JTextField();

        generateButton = new JButton("Generate");

        

        // Set bounds for each component
        bookIsbnLabel.setBounds(325, 50, 100, 30);  
        bookIsbnTextField.setBounds(435, 50, 150, 30);

        borrowerLabel.setBounds(325, 120, 100, 30);  
        borrowerTextField.setBounds(435, 120, 260, 30); 

        mobileNumberLabel.setBounds(325, 170, 100, 30); 
        mobileNumberTextField.setBounds(435, 170, 260, 30); 

        emailAddressLabel.setBounds(325, 220, 100, 30);
        emailAddressTextField.setBounds(435, 220, 260, 30); 

        daysLabel.setBounds(325, 270, 250, 30);   
        daysTextField.setBounds(435, 270, 260, 30);  

        generateButton.setBounds(325, 320, 100, 30); 


        searchButton.setBounds(595, 50, 100, 30);    
        matchingBookLabel.setBounds(435, 75, 350, 30);
        searchButton.setBackground(new Color(65, 88, 108));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false); // Hide default border

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
        

        // Customize Generate button
        generateButton.setBackground(new Color(65, 88, 108));
        generateButton.setForeground(Color.WHITE);
        generateButton.setBorderPainted(false); // Hide default border

        // Add hover effect to Generate button
        generateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                generateButton.setBackground(new Color(122, 149, 171));
                generateButton.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                generateButton.setBorder(null); // Remove border on mouse exit
                generateButton.setBackground(new Color(65, 88, 108));
                generateButton.setForeground(Color.WHITE);
            }
        });

        


        // Add components to the frame
        add(bookIsbnLabel);
        add(bookIsbnTextField);
        add(searchButton);
        add(matchingBookLabel);
        add(borrowerLabel);
        add(borrowerTextField);
        add(mobileNumberLabel);
        add(mobileNumberTextField);
        add(emailAddressLabel);
        add(emailAddressTextField);
        add(daysLabel);
        add(daysTextField);
        add(generateButton);

        // Action listener for the Generate button
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateAction();
            }
        });

        // Action listener for the Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        setVisible(true);
    }

    private void performSearch() {
        String isbn = bookIsbnTextField.getText().trim();
        String bookTitle = getBookTitleByISBN(isbn);
        if (bookTitle != null) {
            matchingBookLabel.setText(bookTitle);
        } else {
            matchingBookLabel.setText("No matching book found for ISBN");
        }
    }

    private String getBookTitleByISBN(String isbn) {
        List<String[]> books = BookReader.readBooks("Library_Collection.txt");
        for (String[] book : books) {
            if (book[1].trim().equals(isbn)) {
                return book[0].trim();
            }
        }
        return null;
    }

    private void generateAction() {
        String isbn = bookIsbnTextField.getText().trim();
        String borrower = borrowerTextField.getText().trim();
        String mobileNumber = mobileNumberTextField.getText().trim();
        String emailAddress = emailAddressTextField.getText().trim();
        String daysBorrowedStr = daysTextField.getText().trim();

        // Validate daysBorrowed input
        int daysBorrowed = 0;
        try {
            daysBorrowed = Integer.parseInt(daysBorrowedStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of days borrowed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get book title based on ISBN
        String bookTitle = getBookTitleByISBN(isbn);
        if (bookTitle == null) {
            JOptionPane.showMessageDialog(this, "No matching book found for ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate deadline
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysBorrowed);
        Date deadline = calendar.getTime();

        // Format the deadline date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String deadlineStr = dateFormat.format(deadline);

        // Generate a random 6-digit borrow ID
        Random random = new Random();
        int borrowID = 100000 + random.nextInt(900000);

        // Construct message to display
        String message = "Borrow ID: " + borrowID + "\n" +
                "Book: " + bookTitle + "\n" +
                "ISBN: " + isbn + "\n" +
                "Deadline: " + deadlineStr + "\n" +
                "Borrower: " + borrower + "\n" +
                "Mobile Number: " + mobileNumber + "\n" +
                "Email Address: " + emailAddress + "\n";


        // Show dialog with options to save or cancel
        int option = JOptionPane.showOptionDialog(this, message, "Generated Information",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Save", "Cancel"}, "Save");

        if (option == JOptionPane.YES_OPTION) {
            // Save the information
            saveToFile(bookTitle, isbn, borrower, mobileNumber, emailAddress, deadlineStr, borrowID);
            JOptionPane.showMessageDialog(this, "Information saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Cancel the operation
            JOptionPane.showMessageDialog(this, "Operation cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveToFile(String bookTitle, String isbn, String borrower, String mobileNumber, String emailAddress, String deadlineStr, int borrowID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowed_books.txt", true))) {
            writer.write("Borrow ID: " + borrowID);
            writer.newLine();
            writer.write("Book: " + bookTitle);
            writer.newLine();
            writer.write("ISBN: " + isbn);
            writer.newLine();
            writer.write("Borrower: " + borrower);
            writer.newLine();
            writer.write("Deadline: " + deadlineStr);
            writer.newLine();
            writer.write("Mobile Number: " + mobileNumber);
            writer.newLine();
            writer.write("Email Address: " + emailAddress);
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving information to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LendBookAction());
    }
}
