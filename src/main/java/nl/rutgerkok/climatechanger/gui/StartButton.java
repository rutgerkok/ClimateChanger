package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.Converter;
import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.World;
import nl.rutgerkok.climatechanger.task.ChunkTask;

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
        final World world = information.getWorld();
        if (world == null) {
            showMessage("Please select the level.dat first.");
            return;
        }
        
        final List<ChunkTask> tasks = information.getTasks();
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
                new Converter(StartButton.this, world, tasks).convert();
            }
        }).start();
    }

    @Override
    public void complete(final int chunksConverted) {
        progressBar.complete(chunksConverted);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showMessage("Done! Converted " + chunksConverted + " chunks.");
                setEnabled(true);
            }
        });
    }

    @Override
    public void failed(final Exception reason) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showMessage("Failed to convert chunks: " + reason.getMessage());
                setEnabled(true);
            }
        });
        progressBar.failed(reason);
    }

    @Override
    public void init(int maxProgress) {
        // Forward
        progressBar.init(maxProgress);
    }

    @Override
    public void setProgress(int progress) {
        // Forward
        progressBar.setProgress(progress);
    }

    private void showMessage(String error) {
        JOptionPane.showMessageDialog(null, error);
    }
}
