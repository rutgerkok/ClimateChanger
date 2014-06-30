package nl.rutgerkok.climatechanger;

public class ConsoleProgressUpdater implements ProgressUpdater {
    private int maxProgress;

    @Override
    public void complete(int chunksConverted) {
        System.out.println("Done! Converted " + chunksConverted + " chunks.");
    }

    @Override
    public void failed(Exception reason) {
        System.err.println("***Failed converting the chunks");
        reason.printStackTrace();
    }

    @Override
    public void init(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public void setProgress(int progress) {
        System.out.println(progress + "/" + maxProgress + " files done.");
    }

}
