package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.util.Consumer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class FileChooserPanel extends JPanel {
    private final List<Consumer<File>> changeListeners = new ArrayList<>();
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
    private void callChangeListeners(File file) {
        for (Consumer<File> changeListener : changeListeners) {
            changeListener.accept(file);
        }
    }

    /**
     * Opens a file chooser. Returns the selected file. Returns null when no
     * file was selected.
     *
     * @return The file, or null if nothing was selected.
     */
    private File chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private JButton createBrowseButton() {
        JButton button = new JButton("Browse...");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                File opened = chooseFile();
                if (opened != null) {
                    FileChooserPanel.this.textField.setText(opened.getAbsolutePath());
                }
                callChangeListeners(opened);
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
                File file = new File(textField.getText());
                if (file.exists()) {
                    callChangeListeners(file);
                } else {
                    callChangeListeners(null);
                }
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
    public void subscribeToFileChanges(Consumer<File> consumer) {
        changeListeners.add(consumer);
    }
}
