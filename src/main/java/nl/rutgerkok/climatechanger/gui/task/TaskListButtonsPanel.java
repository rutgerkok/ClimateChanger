package nl.rutgerkok.climatechanger.gui.task;

import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.gui.task.window.TaskChooserWindow;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.util.Consumer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TaskListButtonsPanel extends JPanel {

    private final JButton addTaskButton;
    private final GuiInformation information;

    public TaskListButtonsPanel(GuiInformation information) {
        this.information = information;

        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JButton removeTaskButton = new JButton("Remove selected");
        removeTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedActions();
            }
        });
        add(removeTaskButton);

        addTaskButton = new JButton("Add a task...");
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTaskWindow();
            }
        });
        add(addTaskButton);
    }

    private void openTaskWindow() {
        addTaskButton.setEnabled(false);
        new TaskChooserWindow(this, new Consumer<ChunkTask>() {
            @Override
            public void accept(ChunkTask task) {
                information.addTask(task);
                addTaskButton.setEnabled(true);
            }
        }, new Runnable() {
            @Override
            public void run() {
                addTaskButton.setEnabled(true);
            }
        });
    }

    private void removeSelectedActions() {
        for (ChunkTask task : information.getSelectedTasks()) {
            information.removeTask(task);
        }
    }
}
