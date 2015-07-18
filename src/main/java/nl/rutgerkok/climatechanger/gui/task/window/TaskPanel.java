package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Task panels extend this class. Has a white background.
 *
 */
public abstract class TaskPanel extends JPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    public TaskPanel() {
        setBackground(Color.WHITE);
    }

    /**
     * When the user clicks "Add", this method will be called.
     *
     * @return The task to add.
     * @throws InvalidTaskException
     *             If no valid task was added yet.
     */
    public abstract Task getTask() throws InvalidTaskException;
}
