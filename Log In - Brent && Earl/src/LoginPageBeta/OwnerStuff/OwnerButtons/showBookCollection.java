package LoginPageBeta.OwnerStuff.OwnerButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

import LoginPageBeta.BookReader;

public class showBookCollection extends JFrame {

    private JPanel leftPanel; // New panel for the left side
    private JLabel removeBookTitleLabel;
    private JTextPane descriptionTextPane;
    private JLabel iconLabel; // Label for the icon
    private JTextArea bookListTextArea;

    private JLabel bookIsbnLabel;
    private JTextField bookIsbnTextField;
    private JLabel matchingBookLabel;
    private JButton searchButton; // New Generate button

    public showBookCollection() {
        setTitle("Book Collection");
        setSize(750, 500); // Increased width to accommodate left panel
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Use null layout for absolute positioning

        // Initialize components for the left panel
        leftPanel = new JPanel();
        leftPanel.setBackground(new Color(16, 19, 19));
        leftPanel.setBounds(0, 0, 300, getHeight()); // Set width and full height of the frame
        leftPanel.setLayout(null); // Null layout for precise positioning

        ImageIcon icon = new ImageIcon("lib\\BookCollectionIcon.png"); // Replace with your image path
        icon = new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Scale to 100x100
        iconLabel = new JLabel(icon);
        iconLabel.setBounds(50, 25, 200, 200);
        leftPanel.add(iconLabel);

        removeBookTitleLabel = new JLabel("Book Collection");
        removeBookTitleLabel.setForeground(Color.WHITE);
        removeBookTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        removeBookTitleLabel.setBounds(0, 200, 300, 30);
        removeBookTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(removeBookTitleLabel);

        descriptionTextPane = new JTextPane();
        descriptionTextPane.setText("This panel shows the collection of all books in the library.");
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setBackground(new Color(16, 19, 19));
        descriptionTextPane.setForeground(Color.WHITE);
        descriptionTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionTextPane.setBounds(25, 250, 250, 100);
        descriptionTextPane.setBorder(null);
        leftPanel.add(descriptionTextPane);

        add(leftPanel); // Add left panel to the frame

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(300, 50, 437, 410); // Adjust position and size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

                // Replace JScrollPane initialization
                bookListTextArea = new JTextArea();
                bookListTextArea.setEditable(false);
                scrollPane.setViewportView(bookListTextArea); // Add text area to scroll pane
        
                // Load and display books on startup
                displayBooks();

        // Components for the main content area
        bookIsbnLabel = new JLabel("Book ISBN:");
        bookIsbnTextField = new JTextField();
        matchingBookLabel = new JLabel("");
        searchButton = new JButton("Search");

        // Set bounds for each component
        bookIsbnLabel.setBounds(325, 20, 100, 30);
        bookIsbnTextField.setBounds(435, 20, 150, 30);
        searchButton.setBounds(595, 20, 100, 30);

        matchingBookLabel.setBounds(435, 75, 350, 30);

        // Customize Generate button
        searchButton.setBackground(new Color(65, 88, 108));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false); // Hide default border

        // Add hover effect to Generate button
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(122, 149, 171));
                searchButton.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBorder(null); // Remove border on mouse exit
                searchButton.setBackground(new Color(65, 88, 108));
                searchButton.setForeground(Color.WHITE);
            }
        });

        // Add components to the frame
        add(bookIsbnLabel);
        add(bookIsbnTextField);
        add(matchingBookLabel);
        add(searchButton);

        // Action listener for the Generate button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = bookIsbnTextField.getText().trim();
                String bookTitle = getBookTitleByISBN(isbn);
        
                if (bookTitle == null) {
                    JOptionPane.showMessageDialog(showBookCollection.this, "No matching book found for ISBN.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(showBookCollection.this,
                            "Title: " + bookTitle + "\nISBN: " + isbn,
                            "Book Searched", JOptionPane.INFORMATION_MESSAGE);
                }
        
                bookListTextArea.revalidate();
                bookListTextArea.repaint();
            }
        });
        

        setVisible(true);
    }

    private void displayBooks() {
        List<String[]> books = BookReader.readBooks("Library_Collection.txt");
        StringBuilder bookText = new StringBuilder();

        for (String[] book : books) {
            if (book.length >= 2) {  // Ensure both title and ISBN are present
                bookText.append("   " + book[0].trim())  // Title
                       .append("\n   ISBN: ")
                       .append(book[1].trim())  // ISBN
                       .append("\n\n"); 
            }
        }

        bookListTextArea.setText(bookText.toString());
    }

    private String getBookTitleByISBN(String isbn) {
        // Implement BookReader class or method readBooks to read from Library_Collection.txt
        // Example implementation:
        List<String[]> books = BookReader.readBooks("Library_Collection.txt");
        for (String[] book : books) {
            if (book[1].trim().equals(isbn)) {
                return book[0].trim();
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private boolean removeBookByISBN(String isbnToRemove) {
        File inputFile = new File("Library_Collection.txt");
        File tempFile = new File("Library_Collection_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            boolean bookFound = false;
            while ((currentLine = reader.readLine()) != null) {
                String[] book = currentLine.split(",");
                if (book.length >= 2 && book[1].trim().equals(isbnToRemove)) {
                    bookFound = true; // Mark that the book was found and skip the line
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }
        

            if (!bookFound) {
                // Book was not found, delete the temp file
                tempFile.delete();
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Delete the original file and rename the temp file
        if (!inputFile.delete()) {
            return false;
        }
        if (!tempFile.renameTo(inputFile)) {
            return false;
        }

        bookListTextArea.revalidate();
        bookListTextArea.repaint();

        return true;

        
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new showBookCollection());
    }
}
