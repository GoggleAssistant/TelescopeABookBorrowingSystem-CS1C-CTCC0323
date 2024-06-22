package LoginPageBeta.CollectionManagerStuff;

import javax.swing.*;

import LoginPageBeta.CollectionManagerStuff.CollectionManagerButtons.addBookAction;
import LoginPageBeta.CollectionManagerStuff.CollectionManagerButtons.removeBookAction;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CollectionManagerUI {

    private JPanel buttonPanel;
    private JButton addBookButton;
    private JButton removeBookButton;
    private JPanel libraryCollectionPanel;
    private JScrollPane scrollPane;
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;

    public CollectionManagerUI() {
        // Initialize panels and components
        buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(800, 30));

        addBookButton = new JButton("Add Book");
        removeBookButton = new JButton("Remove Book");

        addBookButton.setPreferredSize(new Dimension(200, 50));
        removeBookButton.setPreferredSize(new Dimension(200, 50));

        addBookButton.setFocusable(false);
        removeBookButton.setFocusable(false);

        // Add action listeners to buttons


        addBookButton.addActionListener(e -> new addBookAction());
        removeBookButton.addActionListener(e -> new removeBookAction());

        buttonPanel.add(addBookButton);
        buttonPanel.add(removeBookButton);

        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField();
        searchButton = new JButton("Search Book ISBN");

        // Set preferred sizes for the search field and button
        searchField.setPreferredSize(new Dimension(660, 30));
        searchButton.setPreferredSize(new Dimension(200, 30));

        searchButton.setFocusable(false);

        // Add ActionListener to searchButton
        searchButton.addActionListener(e -> {
            String isbnToSearch = searchField.getText().trim();
            if (!isbnToSearch.isEmpty()) {
                searchBookByISBN(isbnToSearch);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter an ISBN to search.", "Search Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Initialize libraryCollectionPanel
        libraryCollectionPanel = new JPanel();
        libraryCollectionPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns layout

        // Create scroll pane for libraryCollectionPanel
        scrollPane = new JScrollPane(libraryCollectionPanel);
        scrollPane.setPreferredSize(new Dimension(850, 450));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Set a custom unit increment
        verticalScrollBar.setUnitIncrement(10); // Scrollwheel sensitivity

        // Load books from file
        loadBooksFromFile("Library_Collection.txt");
    }

    // Method to load books from file and populate the library collection panel
    private void loadBooksFromFile(String filename) {
        int libraryCollectionPanelHeight = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String bookInfo = parts[0];
                    String isbn = parts[1];

                    // Create panel for each book
                    JPanel bookPanel = new JPanel(new BorderLayout());
                    JLabel bookLabel = new JLabel("    " + bookInfo);
                    JLabel isbnLabel = new JLabel("    ISBN: " + isbn);
                    bookPanel.add(bookLabel, BorderLayout.NORTH);
                    bookPanel.add(isbnLabel, BorderLayout.CENTER);

                    // Add bookPanel to libraryCollectionPanel
                    libraryCollectionPanel.add(bookPanel);

                    // Adjust height for every odd number of books
                    if (libraryCollectionPanel.getComponentCount() % 2 == 1) {
                        libraryCollectionPanelHeight += 50;
                    }
                }
            }
            // Update the preferred size of libraryCollectionPanel
            libraryCollectionPanel.setPreferredSize(new Dimension(850, libraryCollectionPanelHeight));

            // Refresh the scroll pane to reflect changes
            scrollPane.revalidate();
            scrollPane.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to search for a book by ISBN and display information in a dialog
    private void searchBookByISBN(String isbnToSearch) {
        try (BufferedReader br = new BufferedReader(new FileReader("Library_Collection.txt"))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String bookInfo = parts[0];
                    String isbn = parts[1];
                    if (isbn.equals(isbnToSearch)) {
                        // Found the book, show message dialog with book details
                        JOptionPane.showMessageDialog(null, "Book Found:\n" + bookInfo + "\nISBN: " + isbn,
                                "Book Found", JOptionPane.INFORMATION_MESSAGE);
                        found = true;
                        break; // No need to continue searching
                    }
                }
            }
            if (!found) {
                // If ISBN is not found, show error dialog
                JOptionPane.showMessageDialog(null, "Book with ISBN " + isbnToSearch + " not found.",
                        "Book Not Found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            JFrame frame = new JFrame("Collection Manager UI");
            CollectionManagerUI ui = new CollectionManagerUI();
            frame.getContentPane().add(ui.getMainPanel());
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
