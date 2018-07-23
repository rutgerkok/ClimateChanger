package nl.rutgerkok.climatechanger.gui.task.window;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import nl.rutgerkok.climatechanger.task.LightPopulatedSetter;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;

public class LightPopulatedSetterPanel extends TaskPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    public LightPopulatedSetterPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        add(new JLabel("<html>Minecraft 1.13 deletes all chunks with LightPopulated set to false."
                + " This task sets LightPopulated to true in all chunks, so that your complete"
                + " world will be carried over to Minecraft 1.13.'."), BorderLayout.NORTH);
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        return new LightPopulatedSetter();
    }
}
