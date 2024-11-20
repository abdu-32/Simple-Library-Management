import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryManagementSystem {
    private static Map<String, User> users = new HashMap<>();
    private static List<Book> books = new ArrayList<>();
    private static User currentUser;

    public static void main(String[] args) {
        // Launch Login Screen
        new LoginScreen();
    }

    // ----- Book Class -----
    static class Book {
        String title;
        String author;
        String id;
        boolean isIssued;

        public Book(String title, String author, String id) {
            this.title = title;
            this.author = author;
            this.id = id;
            this.isIssued = false;
        }

        public String toString() {
            return id + ": " + title + " by " + author + (isIssued ? " [Issued]" : " [Available]");
        }
    }

    // ----- User Class -----
    static class User {
        String username;
        String password;
        List<Book> issuedBooks;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
            this.issuedBooks = new ArrayList<>();
        }
    }

    // ----- Login Screen -----
    static class LoginScreen extends JFrame {
        public LoginScreen() {
            setTitle("Library Management System - Login");
            setSize(400, 250);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Set GridBagLayout for flexible layout
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel userLabel = new JLabel("Username:");
            userLabel.setFont(new Font("Arial", Font.BOLD, 14));
            JLabel passLabel = new JLabel("Password:");
            passLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);

            JButton loginButton = new JButton("Login");
            JButton registerButton = new JButton("Register");

            // Set colors and fonts for buttons
            loginButton.setBackground(Color.LIGHT_GRAY);
            loginButton.setFont(new Font("Arial", Font.BOLD, 14));
            registerButton.setBackground(Color.LIGHT_GRAY);
            registerButton.setFont(new Font("Arial", Font.BOLD, 14));

            // Positioning components
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(userLabel, gbc);
            gbc.gridx = 1;
            add(usernameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(passLabel, gbc);
            gbc.gridx = 1;
            add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            add(loginButton, gbc);
            gbc.gridx = 1;
            add(registerButton, gbc);

            setVisible(true);

            // Login Action
            loginButton.addActionListener(e -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if ("admin".equals(username) && "admin".equals(password)) {
                    dispose();
                    new AdminPanel();
                } else if (users.containsKey(username) && users.get(username).password.equals(password)) {
                    currentUser = users.get(username);
                    dispose();
                    new UserPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Register Action
            registerButton.addActionListener(e -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (!users.containsKey(username)) {
                    users.put(username, new User(username, password));
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    // ----- Admin Panel -----
    static class AdminPanel extends JFrame {
        public AdminPanel() {
            setTitle("Library Management System - Admin Panel");
            setSize(500, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Set layout and background color
            setLayout(new GridBagLayout());
            getContentPane().setBackground(new Color(230, 240, 255));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JButton addBookButton = new JButton("Add Book");
            JButton removeBookButton = new JButton("Remove Book");
            JButton viewBooksButton = new JButton("View All Books");
            JButton logoutButton = new JButton("Logout");

            // Set button colors and fonts
            customizeButton(addBookButton);
            customizeButton(removeBookButton);
            customizeButton(viewBooksButton);
            customizeButton(logoutButton);

            // Add components with GridBag positioning
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(addBookButton, gbc);

            gbc.gridy = 1;
            add(removeBookButton, gbc);

            gbc.gridy = 2;
            add(viewBooksButton, gbc);

            gbc.gridy = 3;
            add(logoutButton, gbc);

            setVisible(true);

            // Add Book Action
            addBookButton.addActionListener(e -> {
                String title = JOptionPane.showInputDialog("Enter Book Title:");
                String author = JOptionPane.showInputDialog("Enter Book Author:");
                String id = JOptionPane.showInputDialog("Enter Book ID:");

                books.add(new Book(title, author, id));
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            });

            // Remove Book Action
            removeBookButton.addActionListener(e -> {
                String id = JOptionPane.showInputDialog("Enter Book ID to Remove:");
                books.removeIf(book -> book.id.equals(id));
                JOptionPane.showMessageDialog(this, "Book removed successfully!");
            });

            // View Books Action
            viewBooksButton.addActionListener(e -> {
                StringBuilder bookList = new StringBuilder("Books in Library:\n");
                for (Book book : books) {
                    bookList.append(book).append("\n");
                }
                JOptionPane.showMessageDialog(this, bookList.toString());
            });

            // Logout Action
            logoutButton.addActionListener(e -> {
                dispose();
                new LoginScreen();
            });
        }
    }

    // ----- User Panel -----
    static class UserPanel extends JFrame {
        public UserPanel() {
            setTitle("Library Management System - User Panel");
            setSize(500, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            setLayout(new GridBagLayout());
            getContentPane().setBackground(new Color(245, 245, 220));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JButton viewBooksButton = new JButton("View Available Books");
            JButton issueBookButton = new JButton("Issue Book");
            JButton returnBookButton = new JButton("Return Book");
            JButton viewIssuedBooksButton = new JButton("View Issued Books");
            JButton logoutButton = new JButton("Logout");

            // Customize button appearance
            customizeButton(viewBooksButton);
            customizeButton(issueBookButton);
            customizeButton(returnBookButton);
            customizeButton(viewIssuedBooksButton);
            customizeButton(logoutButton);

            // Position components
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(viewBooksButton, gbc);

            gbc.gridy = 1;
            add(issueBookButton, gbc);

            gbc.gridy = 2;
            add(returnBookButton, gbc);

            gbc.gridy = 3;
            add(viewIssuedBooksButton, gbc);

            gbc.gridy = 4;
            add(logoutButton, gbc);

            setVisible(true);

            // View Available Books
            viewBooksButton.addActionListener(e -> {
                StringBuilder availableBooks = new StringBuilder("Available Books:\n");
                for (Book book : books) {
                    if (!book.isIssued) {
                        availableBooks.append(book).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(this, availableBooks.toString());
            });

            // Issue Book
            issueBookButton.addActionListener(e -> {
                String id = JOptionPane.showInputDialog("Enter Book ID to Issue:");
                for (Book book : books) {
                    if (book.id.equals(id) && !book.isIssued) {
                        book.isIssued = true;
                        currentUser.issuedBooks.add(book);
                        JOptionPane.showMessageDialog(this, "Book issued successfully!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Book not available or invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
            });

            // Return Book
            returnBookButton.addActionListener(e -> {
                String id = JOptionPane.showInputDialog("Enter Book ID to Return:");
                for (Book book : currentUser.issuedBooks) {
                    if (book.id.equals(id)) {
                        book.isIssued = false;
                        currentUser.issuedBooks.remove(book);
                        JOptionPane.showMessageDialog(this, "Book returned successfully!");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "You haven't issued this book", "Error", JOptionPane.ERROR_MESSAGE);
            });

            // View Issued Books
            viewIssuedBooksButton.addActionListener(e -> {
                StringBuilder issuedBooks = new StringBuilder("Your Issued Books:\n");
                for (Book book : currentUser.issuedBooks) {
                    issuedBooks.append(book).append("\n");
                }
                JOptionPane.showMessageDialog(this, issuedBooks.toString());
            });

            // Logout Action
            logoutButton.addActionListener(e -> {
                dispose();
                new LoginScreen();
            });
        }
    }

    // Customize button appearance
    static void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setPreferredSize(new Dimension(200, 40));
    }
}
