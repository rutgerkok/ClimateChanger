package nl.rutgerkok.climatechanger.gui.filechooser;

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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class FileChooserPanel extends JPanel {

    private final List<Consumer<Path>> changeListeners = new ArrayList<>();
    private String previousText = "";
    private final JTextField textField;

    public FileChooserPanel(String label) {
        // Align right
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Label
        add(new JLabel(label));

        // Text field
        add(textField = createTextField());

        // Add button
        JButton button = new JButton("Browse...");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseClick();
            }
        });
        add(button);
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

    private JTextField createTextField() {
        final JTextField textField = new JTextField("");
        textField.setPreferredSize(new Dimension(250, 22));
        textField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                textUpdated(textField.getText());
            }
        });
        return textField;
    }

    protected abstract void onBrowseClick();

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

    protected void textUpdated(String newText) {
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
