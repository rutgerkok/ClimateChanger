package nl.rutgerkok.climatechanger;

import java.util.concurrent.atomic.AtomicInteger;

public class ConsoleProgressUpdater implements ProgressUpdater {
    private final AtomicInteger currentProgress = new AtomicInteger();
    private int maxProgress;

    @Override
    public void complete() {
        System.out.println("Done! Converted everything without fatal errors.");
    }

    @Override
    public void failed(Exception reason) {
        System.err.println("***Error during conversion");
        reason.printStackTrace();
    }

    @Override
    public void incrementProgress() {
        System.out.println(currentProgress.incrementAndGet() + "/" + maxProgress + " files done.");
    }

    @Override
    public void init(int maxProgress) {
        currentProgress.set(0);
        this.maxProgress = maxProgress;
    }

}
