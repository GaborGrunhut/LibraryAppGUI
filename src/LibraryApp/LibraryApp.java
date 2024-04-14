import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibraryApp extends JFrame implements ActionListener {
    private DefaultListModel<String> namesModel;
    private JList<String> namesList;
    private DefaultListModel<String> booksModel;
    private JList<String> booksList;
    private JButton returnButton;

    public LibraryApp() {
        setTitle("Könyvtár alkalmazás");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        namesModel = new DefaultListModel<>();
        readNamesFromFile("/..Kolcsonzok.csv");

        namesList = new JList<>(namesModel);
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateBooksList();
            }
        });

        JScrollPane namesScrollPane = new JScrollPane(namesList);

        booksModel = new DefaultListModel<>();
        booksList = new JList<>(booksModel);

        JScrollPane booksScrollPane = new JScrollPane(booksList);

        returnButton = new JButton("Visszahozva");
        returnButton.addActionListener(this);
        returnButton.setEnabled(false);

        JButton closeButton = new JButton("Bezár");
        closeButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnButton);
        buttonPanel.add(closeButton);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(namesScrollPane);
        mainPanel.add(booksScrollPane);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void readNamesFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                namesModel.addElement(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readBooksFromFile(String fileName, String selectedName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].trim().equals(selectedName)) {
                    booksModel.addElement(parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBooksList() {
        String selectedName = namesList.getSelectedValue();
        if (selectedName != null) {
            booksModel.clear();
            readBooksFromFile("/..Kolcsonzesek.csv", selectedName);
            returnButton.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton) {
            String selectedBook = booksList.getSelectedValue();
            if (selectedBook != null) {
                booksModel.removeElement(selectedBook);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            app.setVisible(true);
        });
    }
}

