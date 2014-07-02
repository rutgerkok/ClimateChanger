package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.util.Consumer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class FileChooserPanel extends JPanel {
    private final List<Consumer<Path>> changeListeners = new ArrayList<>();
    private String previousText = "";
    private final JTextField textField;

    public FileChooserPanel(String label, String defaultPath) {
        // Align right
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Label
        add(new JLabel(label));

        // Text field
        add(textField = createTextField(defaultPath));

        // Browse button
        add(createBrowseButton());
    }

    /**
     * Calls the change listeners.
     *
     * @param file
     *            The file, may be null.
     */
    private void callChangeListeners(Path file) {
        for (Consumer<Path> changeListener : changeListeners) {
            changeListener.accept(file);
        }
    }

    /**
     * Opens a file chooser. Returns the selected file. Returns null when no
     * file was selected.
     *
     * @return The file, or null if nothing was selected.
     */
    private Path chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        } else {
            return null;
        }
    }

    private JButton createBrowseButton() {
        JButton button = new JButton("Browse...");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Path opened = chooseFile();
                if (opened == null) {
                    // Cancelled
                    return;
                }

                textUpdated(opened.toString());
            }
        });
        return button;
    }

    private JTextField createTextField(String path) {
        final JTextField textField = new JTextField(path);
        textField.setPreferredSize(new Dimension(250, 22));
        textField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                textUpdated(textField.getText());
            }
        });
        return textField;
    }

    /**
     * Adds a listener that will be called when the selected file has changed.
     *
     * @param consumer
     *            The code to run. File parameter may be null if no file was
     *            selected.
     */
    public void subscribeToFileChanges(Consumer<Path> consumer) {
        changeListeners.add(consumer);
    }

    private void textUpdated(String newText) {
        newText = newText.trim();

        // Check if changed
        if (newText.equals(previousText)) {
            // Nothing changed, do nothing
            return;
        }

        // Update text field and previous text
        previousText = newText;
        textField.setText(newText);

        // Notify of disappeared file
        if (newText.isEmpty()) {
            callChangeListeners(null);
            return;
        }

        // Check for valid file, notify
        Path file = Paths.get(newText);
        if (Files.exists(file)) {
            callChangeListeners(file);
        } else {
            callChangeListeners(null);
        }
    }
}
