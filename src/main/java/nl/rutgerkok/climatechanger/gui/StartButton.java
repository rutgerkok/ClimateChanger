package nl.rutgerkok.climatechanger.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import nl.rutgerkok.climatechanger.IdChanger;
import nl.rutgerkok.climatechanger.ProgressUpdater;

public class StartButton extends JButton implements ActionListener, ProgressUpdater {
    private final FileChooserPanel directoryPanel;
    private final IdChooserPanel idChooserPanel;
    private final ProgressBar progressBar;

    public StartButton(String text, FileChooserPanel directory, IdChooserPanel idChooserPanel, ProgressBar progressBar) {
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
                new IdChanger(StartButton.this, regionDirectory, idFrom, idTo).convert();
            }
        }).start();

    }

    public void setProgressPercentage(float progress) {
        // Forward
        progressBar.setProgressPercentage(progress);
    }

    public void complete(final int chunksConverted) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                showMessage("Done! Converted " + chunksConverted + " chunks.");
                setEnabled(true);
            }
        });
    }
}
