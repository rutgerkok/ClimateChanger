package nl.rutgerkok.climatechanger;

public class ConsoleProgressUpdater implements ProgressUpdater {
    private int maxProgress;

    public void setProgress(int progress) {
        System.out.println(progress + "/" + maxProgress + " files done.");
    }

    public void complete(int chunksConverted) {
        System.out.println("Done! Converted " + chunksConverted + " chunks.");
    }

    public void init(int maxProgress) {
        this.maxProgress = maxProgress;
    }

}
