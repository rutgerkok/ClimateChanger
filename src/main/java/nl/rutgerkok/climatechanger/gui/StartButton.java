package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.Converter;
import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.task.BiomeIdChanger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class StartButton extends JButton implements ActionListener, ProgressUpdater {
    private final FileChooserPanel directoryPanel;
    private final IdChooserPanel idChooserPanel;
    private final ProgressPanel progressBar;

    public StartButton(String text, FileChooserPanel directory, IdChooserPanel idChooserPanel, ProgressPanel progressBar) {
        super(text);
        this.directoryPanel = directory;
        this.idChooserPanel = idChooserPanel;
        this.progressBar = progressBar;
        addActionListener(this);
    }

    private void showMessage(String error) {
        JOptionPane.showMessageDialog(null, error);
    }

    public void actionPerformed(ActionEvent event) {
        final File regionDirectory = new File(directoryPanel.getText());
        if (!regionDirectory.exists() || !regionDirectory.isDirectory()) {
            showMessage("Please select the region directory.");
            return;
        }

        if (!regionDirectory.getName().equals("region")) {
            showMessage("Please select the region directory in the world folder.");
            return;
        }

        final byte idFrom;
        final byte idTo;
        try {
            idFrom = (byte) Short.parseShort(idChooserPanel.getFrom());
            idTo = (byte) Short.parseShort(idChooserPanel.getTo());
        } catch (NumberFormatException e) {
            showMessage("Invalid biome id - use numeric ids");
            return;
        }

        // Disable button
        setEnabled(false);

        // Start converter
        new Thread(new Runnable() {
            public void run() {
                List<? extends ChunkTask> tasks = Arrays.asList(new BiomeIdChanger(idFrom, idTo));
                new Converter(StartButton.this, regionDirectory, tasks).convert();
            }
        }).start();
    }

    public void setProgress(int progress) {
        // Forward
        progressBar.setProgress(progress);
    }

    public void complete(final int chunksConverted) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                showMessage("Done! Converted " + chunksConverted + " chunks.");
                setEnabled(true);
            }
        });
    }

    public void init(int maxProgress) {
        // Forward
        progressBar.init(maxProgress);
    }
}
