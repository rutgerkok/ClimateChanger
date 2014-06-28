package nl.rutgerkok.climatechanger.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

public class FileChooserPanel extends JPanel {
    private final JTextField textField;

    public FileChooserPanel(String label, String defaultPath) {
        // Align right
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Label
        add(new JLabel(label));

        // Text field
        this.textField = new JTextField(defaultPath);
        this.textField.setPreferredSize(new Dimension(250, 22));
        add(this.textField);

        // Browse button
        JButton button = new JButton("Browse...");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                File opened = chooseDirectory();
                if (opened != null) {
                    FileChooserPanel.this.textField.setText(opened.getAbsolutePath());
                }
            }
        });
        add(button);
    }

    /**
     * Opens a file chooser. Returns the selected file. Returns null when no
     * file was selected.
     *
     * @return The file, or null if nothing was selected.
     */
    private File chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public String getText() {
        return this.textField.getText();
    }
}
