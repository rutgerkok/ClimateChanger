package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.hammer.util.Progress;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressPanel extends JPanel implements ProgressUpdater {
    private final Color originalBarForeground;
    private final JProgressBar progressBar;
    private final JLabel progressField;

    public ProgressPanel() {
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 20));
        add(progressBar);

        progressField = new JLabel();
        progressField.setPreferredSize(new Dimension(45, 25));
        add(progressField);

        originalBarForeground = progressBar.getForeground();
    }

    @Override
    public void complete() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Make loading bar full
                progressBar.setValue(progressBar.getMaximum());
            }
        });
    }

    @Override
    public void failed(Exception reason) {
        progressBar.setForeground(Color.RED);
        progressBar.setStringPainted(true);
        progressBar.setString("Failed: " + reason.getMessage());
    }

    @Override
    public void update(final Progress progress) {
        // It's one tenth second since the last progress update, update bar
        // Make sure bar is updated on correct thread
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                progressBar.setValue(progress.getIntPercentage());
                progressField.setText(progress.getPercentage() + "%");
            }
        });
    }

    @Override
    public void init() {
        progressBar.setValue(0);
        progressBar.setMaximum(100);
        progressBar.setIndeterminate(false);

        // Reset foreground, in case previous attempt failed
        progressBar.setForeground(originalBarForeground);
        progressBar.setStringPainted(false);
    }

}
