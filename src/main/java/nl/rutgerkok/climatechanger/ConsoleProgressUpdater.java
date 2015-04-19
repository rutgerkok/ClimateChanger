package nl.rutgerkok.climatechanger;

import nl.rutgerkok.hammer.util.Progress;

public class ConsoleProgressUpdater implements ProgressUpdater {

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
    public void update(Progress progress) {
        System.out.println(progress.getIntPercentage() + "%");
    }

    @Override
    public void init() {
    }

}
