package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.Consumer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Pop-up window that allows the user to add a task.
 *
 */
public class TaskChooserWindow extends JDialog {
    private final Consumer<Task> onSuccess;
    private final JTabbedPane tabs;

    public TaskChooserWindow(JPanel alignTo, MaterialMap materialMap, Consumer<Task> onSuccess, final Runnable onAbort) {
        this.onSuccess = onSuccess;

        setSize(300, 465);
        setTitle("Add a task");

        // Set above original component
        Point windowLocation = alignTo.getLocationOnScreen();
        windowLocation.x += 30;
        windowLocation.y -= 100;
        setLocation(windowLocation);

        // Notify on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAbort.run();
            }
        });

        // Add tabs
        tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(7, 7, 0, 7));

        tabs.addTab("Change biome id", new BiomeIdChangerPanel(materialMap));
        tabs.addTab("Change block id", new BlockIdChangerPanel(materialMap));
        tabs.addTab("Spawn ores", new OreSpawnerPanel(materialMap));
        tabs.addTab("Fix signs", new SignFixerPanel());

        add(tabs, BorderLayout.CENTER);

        // Add save button
        add(new TaskSavePanel(this), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Closes this window after the task has been created.
     *
     * @param task
     *            The created task.
     */
    void closeOnSuccess(Task task) {
        setVisible(false);
        dispose();

        onSuccess.accept(task);
    }

    /**
     * Gets the currently selected component.
     *
     * @return The currently selected component.
     */
    Component getSelectedTab() {
        return tabs.getSelectedComponent();
    }
}
