package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.converter.ConverterExecutor;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.hammer.anvil.AnvilWorld;
import nl.rutgerkok.hammer.util.Progress;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class StartButton extends JButton implements ActionListener, ProgressUpdater {
    private final GuiInformation information;
    private final ProgressPanel progressBar;

    public StartButton(String text, GuiInformation information, ProgressPanel progressBar) {
        super(text);
        this.information = information;
        this.progressBar = progressBar;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final AnvilWorld world = information.getWorld();
        if (world == null) {
            showMessage("Please select the level.dat first.");
            return;
        }

        final List<Task> tasks = information.getTasks();
        if (tasks.isEmpty()) {
            showMessage("There are no tasks selected. Please add at least one task.");
            return;
        }

        // Disable button
        setEnabled(false);

        // Start converter
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ConverterExecutor(StartButton.this, world, tasks).convertAll();
            }
        }).start();
    }

    @Override
    public void complete() {
        progressBar.complete();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showMessage("Done! Converted everything.");
                setEnabled(true);
            }
        });
    }

    @Override
    public void failed(final Exception reason) {
        // Print to console
        reason.printStackTrace();

        // Show popup box
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showMessage("Failed to convert everything: " + reason.getMessage());
                setEnabled(true);
            }
        });

        // Stop progress bar
        progressBar.failed(reason);
    }

    @Override
    public void update(Progress progress) {
        // Forward
        progressBar.update(progress);
    }

    @Override
    public void init() {
        // Forward
        progressBar.init();
    }

    private void showMessage(String error) {
        JOptionPane.showMessageDialog(null, error);
    }
}
