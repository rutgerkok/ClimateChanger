package nl.rutgerkok.climatechanger;

import nl.rutgerkok.hammer.util.Progress;

public class ConsoleProgressUpdater implements ProgressUpdater {

    private volatile double previousUpdate = -1;

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
    public void init() {
    }

    private double roundToSingleDigit(double number) {
        int intNumber = (int) (number * 10);
        return intNumber / 10.0;
    }

    @Override
    public void update(Progress progress) {
        double percentage = progress.getPercentage();
        if (percentage == this.previousUpdate) {
            return;
        }
        System.out.println(roundToSingleDigit(percentage) + "%");
        this.previousUpdate = percentage;
    }

}
