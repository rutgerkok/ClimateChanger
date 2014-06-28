package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.Converter;
import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.task.ChunkTask;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
        final File regionDirectory = information.getRegionDirectory();
        if (regionDirectory == null || !regionDirectory.exists() || !regionDirectory.isDirectory()) {
            showMessage("Please select the region directory.");
            return;
        }

        if (!regionDirectory.getName().equals("region")) {
            showMessage("Please select the region directory in the world folder.");
            return;
        }

        // Disable button
        setEnabled(false);

        // Start converter
        final List<ChunkTask> tasks = information.getTasks();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Converter(StartButton.this, regionDirectory, tasks).convert();
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
