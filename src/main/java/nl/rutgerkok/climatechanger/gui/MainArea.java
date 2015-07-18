package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.gui.task.TaskPanel;
import nl.rutgerkok.hammer.anvil.AnvilWorld;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JPanel;

/**
 * The main area of the GUI. Most of the time, this is just a wrapper around
 * {@link TaskPanel}, but it holds an empty JPanel when there is no world set.
 *
 */
final class MainArea extends JPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    MainArea(final GuiInformation information) {
        setLayout(new BorderLayout());
        information.subscribeToWorldChanges(new Runnable() {

            @Override
            public void run() {
                AnvilWorld newWorld = information.getWorld();
                if (newWorld == null) {
                    clearMainPanel();
                } else {
                    setMainPanel(new TaskPanel(information));
                }
            }
        });
    }

    /**
     * Sets a new main panel.
     * 
     * @param panel
     *            The panel.
     */
    private void setMainPanel(JPanel panel) {
        Objects.requireNonNull(panel);

        // Replace panel
        removeAll();
        add(panel, BorderLayout.CENTER);
        updateScreen();
    }

    /**
     * Revalidates and repaints the layout.
     */
    private void updateScreen() {
        revalidate();
        repaint();
    }

    /**
     * Sets the main panel to an empty panel.
     */
    private void clearMainPanel() {
        setMainPanel(new JPanel());
    }
}
