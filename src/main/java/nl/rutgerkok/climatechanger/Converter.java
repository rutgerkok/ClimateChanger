package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.task.ChunkTask;

import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.RegionFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.NbtIo;

public class Converter {
    private final ProgressUpdater progressUpdater;
    private final File regionFolder;
    private final List<? extends ChunkTask> tasks;

    /**
     * Changes the biome id in all files in the given directory.
     * 
     * @param progressUpdater
     *            Used to monitor progress.
     * @param regionFolder
     *            The directory containing the region files.
     * @param tasks The tasks to execute for each chunk.
     */
    public Converter(ProgressUpdater progressUpdater, File regionFolder, List<? extends ChunkTask> tasks) {
        this.progressUpdater = progressUpdater;
        this.regionFolder = regionFolder;
        this.tasks = tasks;
    }

    /**
     * Converts the files.
     * 
     * @return How many chunks were converted.
     */
    public void convert() {
        int changedChunks = 0;
        File[] filesToConvert = regionFolder.listFiles();
        progressUpdater.init(filesToConvert.length);
        for (int i = 0; i < filesToConvert.length; i++) {
            File file = filesToConvert[i];

            if (file.getName().endsWith(".mca")) {
                try {
                    changedChunks += convertFile(file);
                    try {
                        // Give processor some rest
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                } catch (IOException e) {
                    System.err.println("Failed to convert file " + file.getAbsolutePath());
                    e.printStackTrace();
                }
            }

            progressUpdater.setProgress(i);
        }

        progressUpdater.complete(changedChunks);
    }

    /**
     * Converts a single region file.
     * 
     * @param file
     *            The file to convert.
     * @return The number of chunks changed in the file.
     * @throws IOException
     *             If something went wrong.
     */
    private int convertFile(File file) throws IOException {
        int changedChunks = 0;
        RegionFile regionFile = new RegionFile(file);
        for (int chunkX = 0; chunkX < 32; chunkX++) {
            for (int chunkZ = 0; chunkZ < 32; chunkZ++) {
                DataInputStream inputStream = null;
                DataOutputStream outputStream = null;
                try {
                    inputStream = regionFile.getChunkDataInputStream(chunkX, chunkZ);
                    if (inputStream == null) {
                        // RegionFile does not contain this chunk
                        continue;
                    }
                    CompoundTag parentTag = NbtIo.read(inputStream);
                    CompoundTag chunkTag = parentTag.getCompound("Level");
                    if (chunkTag == null) {
                        // No chunk tag (!)
                        continue;
                    }
                    Chunk chunk = new Chunk(chunkTag);
                    if (runTasks(chunk)) {
                        // Save when changed
                        outputStream = regionFile.getChunkDataOutputStream(chunkX, chunkZ);
                        NbtIo.write(parentTag, outputStream);
                        changedChunks++;
                    }
                } finally {
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();
                }
            }
        }
        regionFile.close();
        return changedChunks;
    }
    
    /**
     * Runs all the tasks of this chunk.
     * @param chunk The chunk to modify.
     * @return True if one of the tasks changed the chunk data, false otherwise.
     */
    private boolean runTasks(Chunk chunk) {
        boolean changed = false;
        for (ChunkTask task : tasks) {
            if (task.execute(chunk)) {
                changed = true;
            }
        }
        return changed;
    }
    
}
