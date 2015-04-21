package nl.rutgerkok.climatechanger.gui.task;

import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.gui.GuiInformation.UpdateType;
import nl.rutgerkok.climatechanger.gui.task.window.TaskChooserWindow;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.hammer.material.MaterialMap;
import nl.rutgerkok.hammer.util.Consumer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TaskListButtonsPanel extends JPanel {

    private final JButton addTaskButton;
    private final GuiInformation information;
    private boolean popupWindowOpen;
    private JButton removeSelectedTasksButton;

    public TaskListButtonsPanel(final GuiInformation information) {
        this.information = information;

        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        removeSelectedTasksButton = new JButton("Remove selected");
        removeSelectedTasksButton.setEnabled(false);
        removeSelectedTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedActions();
            }
        });
        add(removeSelectedTasksButton);

        addTaskButton = new JButton("Add a task...");
        addTaskButton.setEnabled(false);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTaskWindow();
            }
        });
        updateAddTaskButton();
        add(addTaskButton);

        // Update "Add task" button when needed
        information.subscribeToWorldChanges(new Runnable() {
            @Override
            public void run() {
                updateAddTaskButton();
            }
        });

        // Update "Remove selected" button when needed
        information.subscribeToTaskChanges(new Consumer<UpdateType>() {
            @Override
            public void accept(UpdateType type) {
                if (type == UpdateType.ADD_SELECTED || type == UpdateType.REMOVE_SELECTED) {
                    updateRemoveSelectedTasksButton();
                }
            }
        });


    }

    private void openTaskWindow() {
        addTaskButton.setEnabled(false);
        MaterialMap materialMap = information.getWorld().getGameFactory().getMaterialMap();
        new TaskChooserWindow(this, materialMap, new Consumer<Task>() {
            @Override
            public void accept(Task task) {
                information.addTask(task);
                popupWindowOpen = false;
                updateAddTaskButton();
            }
        }, new Runnable() {
            @Override
            public void run() {
                popupWindowOpen = false;
                updateAddTaskButton();
            }
        });
    }

    private void removeSelectedActions() {
        for (Task task : information.getSelectedTasks()) {
            information.removeTask(task);
        }
    }

    /**
     * Updates the enabled state of the "Add task" button.
     */
    private void updateAddTaskButton() {
        if (information.getWorld() == null) {
            addTaskButton.setEnabled(false);
            return;
        }
        if (popupWindowOpen) {
            addTaskButton.setEnabled(false);
            return;
        }
        addTaskButton.setEnabled(true);
    }

    /**
     * Updates the enabled state of the "Remove selected tasks" button.
     */
    private void updateRemoveSelectedTasksButton() {
        if (information.getSelectedTasks().isEmpty()) {
            removeSelectedTasksButton.setEnabled(false);
        } else {
            removeSelectedTasksButton.setEnabled(true);
        }
    }
}
