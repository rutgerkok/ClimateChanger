package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Panel with a save button in the bottom right corner.
 *
 */
public class TaskSavePanel extends JPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    private final TaskChooserWindow window;

    public TaskSavePanel(TaskChooserWindow window) {
        this.window = window;

        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTask();
            }
        });

        add(saveButton);
    }

    private void saveTask() {
        Component selected = window.getSelectedTab();
        if (!(selected instanceof TaskPanel)) {
            JOptionPane.showMessageDialog(this, "No tab selected", "Cannot add task", JOptionPane.ERROR_MESSAGE);
        }
        TaskPanel taskPanel = (TaskPanel) selected;
        try {
            Task task = taskPanel.getTask();
            window.closeOnSuccess(task);
        } catch (InvalidTaskException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Cannot add task", JOptionPane.ERROR_MESSAGE);
        }
    }
}
