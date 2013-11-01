package nl.rutgerkok.climatechanger.gui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import nl.rutgerkok.climatechanger.ProgressUpdater;

public class ProgressPanel extends JPanel implements ProgressUpdater {
    private final JProgressBar progressBar;
    private final JLabel progressField;

    public ProgressPanel() {
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 20));
        add(progressBar);

        progressField = new JLabel();
        progressField.setPreferredSize(new Dimension(45, 25));
        add(progressField);
    }

    public void setProgress(final int progress) {
        // It's one tenth second since the last progress update, update bar
        // Make sure bar is updated on correct thread
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setValue(progress);
                progressField.setText(progress + "/" + progressBar.getMaximum());
            }
        });
    }

    public void complete(int chunksConverted) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Make loading bar full
                progressBar.setValue(progressBar.getMaximum());
            }
        });
    }

    public void init(int maxProgress) {
        progressBar.setMaximum(maxProgress);
        progressBar.setIndeterminate(false);
    }

}
