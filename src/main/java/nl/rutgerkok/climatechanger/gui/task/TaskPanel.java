package nl.rutgerkok.climatechanger.gui.task;

import nl.rutgerkok.climatechanger.gui.FileChooserPanel;
import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.util.Supplier;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
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
        final FileChooserPanel fileChooserPanel = new FileChooserPanel("Region directory:", "");
        add(fileChooserPanel, BorderLayout.NORTH);
        information.setRegionDirectory(new Supplier<File>() {
            @Override
            public File get() {
                return new File(fileChooserPanel.getText());
            }
        });

        // Field for current task
        add(new TaskListPanel(information), BorderLayout.CENTER);

        // Button for new tasks
        add(new TaskListButtonsPanel(information), BorderLayout.SOUTH);
    }
}
