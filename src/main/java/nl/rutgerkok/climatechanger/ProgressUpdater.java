package nl.rutgerkok.climatechanger;

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
     */
    void incrementProgress();

    /**
     * Called when the task is started.
     *
     * @param maxProgress
     *            Defines the scale of the task. When the progress is
     *            maxProgress, the task is completed.
     */
    void init(int maxProgress);
}
