package nl.rutgerkok.climatechanger.gui.task;

import nl.rutgerkok.climatechanger.World;
import nl.rutgerkok.climatechanger.gui.FileChooserPanel;
import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.util.Consumer;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Panel that allows people to edit the task list and region folder.
 *
 */
public class TaskPanel extends JPanel {
    public TaskPanel(GuiInformation information) {

        // Divide into areas
        setLayout(new BorderLayout());

        // Add margin
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Field for directory name
        add(createFileChooser(information), BorderLayout.NORTH);

        // Field for current task
        add(new TaskListPanel(information), BorderLayout.CENTER);

        // Button for new tasks
        add(new TaskListButtonsPanel(information), BorderLayout.SOUTH);
    }

    private FileChooserPanel createFileChooser(final GuiInformation information) {
        final FileChooserPanel fileChooserPanel = new FileChooserPanel("Select the " + World.LEVEL_DAT_NAME + ":", "");

        fileChooserPanel.subscribeToFileChanges(new Consumer<File>() {

            @Override
            public void accept(File file) {
                if (file == null) {
                    // No file selected
                    information.setWorld(null);
                    return;
                }

                try {
                    if (!file.getName().equals(World.LEVEL_DAT_NAME)) {
                        throw new IOException("Please select a " + World.LEVEL_DAT_NAME + "-file.");
                    }

                    information.setWorld(new World(file));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(TaskPanel.this, "Cannot read file:\n\n" + e.getMessage(), "Invalid file", JOptionPane.ERROR_MESSAGE);
                    information.setWorld(null);
                }
            }
        });
        return fileChooserPanel;
    }
}
