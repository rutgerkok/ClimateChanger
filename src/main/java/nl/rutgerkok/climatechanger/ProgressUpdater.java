package nl.rutgerkok.climatechanger;

import nl.rutgerkok.hammer.util.Progress;

/**
 * Used to monitor progress. Any method may be called from any thread.
 *
 */
public interface ProgressUpdater {
    /**
     * Called when the process is complete.
     */
    void complete();

    /**
     * Called when something goes wrong.
     *
     * @param reason
     *            The exception that caused this.
     */
    void failed(Exception reason);

    /**
     * Called whenever the progress is updated one unit.
     * 
     * @param progress
     *            The new progress.
     *
     */
    void update(Progress progress);

    /**
     * Called when the task is started.
     */
    void init();
}
