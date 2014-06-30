package nl.rutgerkok.climatechanger;

/**
 * Used to monitor progress. Any method may be called from any thread.
 *
 */
public interface ProgressUpdater {
    /**
     * Called when the process is complete.
     *
     * @param chunksConverted
     *            The number of chunks that were converted.
     */
    void complete(int chunksConverted);

    /**
     * Called when something goes wrong.
     * 
     * @param reason
     *            The exception that caused this.
     */
    void failed(Exception reason);

    /**
     * Called when the task is started.
     *
     * @param maxProgress
     *            Defines the scale of the task. When the progress is
     *            maxProgress, the task is completed.
     */
    void init(int maxProgress);

    /**
     * Called whenever the progress is updated.
     *
     * @param progress
     *            The current progress. When this is 1
     */
    void setProgress(int progress);
}
