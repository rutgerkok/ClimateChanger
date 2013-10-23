package nl.rutgerkok.climatechanger;

public class ConsoleProgressUpdater implements ProgressUpdater {
    private volatile long lastUpdateTime;

    public void setProgressPercentage(float progress) {
        /*
         * The overhead of synchronization isn't necessary here. Worst that can
         * happen is that the progress message is displayed twice, and even that
         * is highly unlikely.
         */
        long currentTime = System.currentTimeMillis();
        if (currentTime - 1000L > lastUpdateTime) {
            // It's one second since the last progress update, print a message
            lastUpdateTime = currentTime;
            System.out.println(((int) progress) + "% done");
        }
    }

    public void complete(int chunksConverted) {
        System.out.println("Converted " + chunksConverted + " chunks.");
    }

}
