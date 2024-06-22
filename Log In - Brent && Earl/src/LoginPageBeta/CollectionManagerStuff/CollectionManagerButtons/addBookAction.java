package LoginPageBeta.CollectionManagerStuff.CollectionManagerButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import LoginPageBeta.BookReader;

public class addBookAction extends JFrame {

    private JPanel leftPanel;
    private JLabel iconLabel;

    private JLabel addBookTitleLabel;
    private JTextPane descriptionTextPane;

    private JTextArea bookListTextArea;

    private JLabel bookIsbnLabel;
    private JTextField bookIsbnTextField;

    private JLabel bookTitleLabel;
    private JTextField bookTitleTextField;

    private JLabel matchingBookLabel;
    private JButton addBookButton;

    public addBookAction() {
        setTitle("Add Book Action");
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
        ImageIcon icon = new ImageIcon("lib\\AddBookIcon.png"); // Replace with your image path
        icon = new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)); // Scale to 100x100
        iconLabel = new JLabel(icon);
        iconLabel.setBounds(50, 25, 200, 200);
        leftPanel.add(iconLabel);

        addBookTitleLabel = new JLabel("Add Book");
        addBookTitleLabel.setForeground(Color.WHITE);
        addBookTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        addBookTitleLabel.setBounds(0, 200, 300, 30);
        addBookTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(addBookTitleLabel);

        descriptionTextPane = new JTextPane();
        descriptionTextPane.setText("This panel allows you to add books to the library collection. Enter the book ISBN and title below, then click 'Add Book' to save it to the collection. The list of existing books is displayed on the right. You can also search for a book by its ISBN to remove it from the collection.");
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setBackground(new Color(16, 19, 19));
        descriptionTextPane.setForeground(Color.WHITE);
        descriptionTextPane.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionTextPane.setBounds(25, 250, 250, 100);
        descriptionTextPane.setBorder(null);
        leftPanel.add(descriptionTextPane);

        add(leftPanel); // Add left panel to the frame

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(300, 95, 437, 375); // Adjust position and size
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
        bookTitleLabel = new JLabel("Book Title:"); // Corrected label initialization
        bookTitleTextField = new JTextField();
        matchingBookLabel = new JLabel("");
        addBookButton = new JButton("Add Book");

        // Set bounds for each component
        bookIsbnLabel.setBounds(325, 55, 100, 30);
        bookIsbnTextField.setBounds(435, 55, 150, 30);

        bookTitleLabel.setBounds(325, 20, 100, 30); // Corrected bounds for bookTitleLabel
        bookTitleTextField.setBounds(435, 20, 150, 30);

        addBookButton.setBounds(595, 35, 100, 30);

        matchingBookLabel.setBounds(435, 75, 350, 30);

        // Customize Search button
        addBookButton.setBackground(new Color(65, 88, 108));
        addBookButton.setForeground(Color.WHITE);
        addBookButton.setFocusable(false);

        // Add hover effect to Search button
        addBookButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addBookButton.setBackground(new Color(122, 149, 171));
                addBookButton.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                addBookButton.setBackground(new Color(65, 88, 108));
                addBookButton.setForeground(Color.WHITE);
            }
        });

        // Add components to the frame
        add(bookIsbnLabel);
        add(bookIsbnTextField);
        add(bookTitleLabel);
        add(bookTitleTextField);
        add(matchingBookLabel);
        add(addBookButton);

        // Action listener for the Search button
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = bookTitleTextField.getText().trim();
                String isbn = bookIsbnTextField.getText().trim();

                if (!title.isEmpty() && !isbn.isEmpty()) {
                    String bookEntry = "\n" + title + "," + isbn;

                    // Append to Library_Collection.txt
                    try (FileWriter fw = new FileWriter("Library_Collection.txt", true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        out.println(bookEntry);
                        out.close();
                        JOptionPane.showMessageDialog(addBookAction.this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Clear fields and update displayed books
                        bookTitleTextField.setText("");
                        bookIsbnTextField.setText("");
                        displayBooks();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(addBookAction.this, "Failed to add book", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(addBookAction.this, "Please enter both book title and ISBN", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new addBookAction());
    }
}
