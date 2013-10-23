package nl.rutgerkok.climatechanger.gui;

import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import nl.rutgerkok.climatechanger.ProgressUpdater;

public class ProgressBar extends JProgressBar implements ProgressUpdater {
    private volatile long lastUpdateTime;
    private static final int PRECISION = 1000;

    public ProgressBar() {
        this.setMaximum(PRECISION);
        setPreferredSize(new Dimension(300, 20));
    }

    public void setProgressPercentage(final float progress) {
        /*
         * This method avoids updating the progress bar too often.
         * 
         * The overhead of synchronization isn't necessary here. Worst that can
         * happen is that the progress message is displayed twice, and even that
         * is highly unlikely.
         */
        long currentTime = System.currentTimeMillis();
        if (currentTime - 100L > lastUpdateTime) {
            // It's one tenth second since the last progress update, update bar
            lastUpdateTime = currentTime;
            // Make sure bar is updated on correct thread
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    ProgressBar.this.setValue((int) (progress * PRECISION));
                }
            });
        }
    }

    public void complete(int chunksConverted) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Make loading bar full
                ProgressBar.this.setValue(PRECISION);
            }
        });
    }

}
