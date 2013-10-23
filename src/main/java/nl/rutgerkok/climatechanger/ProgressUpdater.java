package nl.rutgerkok.climatechanger;

/**
 * Used to monitor progress. Any method may be called from any thread.
 * 
 */
public interface ProgressUpdater {
    /**
     * Called whenever the progress is updated.
     * 
     * @param progress
     *            The current progress.
     */
    void setProgressPercentage(float progress);

    /**
     * Called when the process is complete.
     * 
     * @param chunksConverted
     *            The number of chunks that were converted.
     */
    void complete(int chunksConverted);
}
