package nl.rutgerkok.climatechanger.gui.task;

import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.gui.GuiInformation.UpdateType;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.util.Consumer;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays a list of tasks. Automatically updates the list.
 *
 */
public class TaskListPanel extends JPanel {
    private final GuiInformation information;

    public TaskListPanel(GuiInformation information) {
        this.information = information;

        information.subscribeToTaskChanges(new Consumer<UpdateType>() {

            @Override
            public void accept(UpdateType type) {
                if (type == UpdateType.REMOVE_ELEMENT || type == UpdateType.ADD_ELEMENT) {
                    rebuildTasks();
                } else {
                    updateTasks();
                }
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(new Color(172, 172, 172)));
        setBackground(Color.WHITE);
        addTasks();
    }

    private void addTasks() {
        List<ChunkTask> tasks = information.getTasks();
        for (ChunkTask task : tasks) {
            add(new TaskLabel(task, information));
        }
        if (tasks.isEmpty()) {
            add(new JLabel("<html><i>No tasks provided</i></html>"));
        }
    }

    /**
     * Removes all tasks and adds them again.
     */
    private void rebuildTasks() {
        removeAll();

        addTasks();

        revalidate();
        repaint();
    }

    private void updateTasks() {
        for (Component component : getComponents()) {
            if (component instanceof TaskLabel) {
                ((TaskLabel) component).updateText();
            }
        }
    }

}
