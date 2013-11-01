package nl.rutgerkok.climatechanger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.NbtIo;

public class IdChanger {
    private final ProgressUpdater progressUpdater;
    private final File regionFolder;
    private final byte from;
    private final byte to;

    /**
     * Changes the biome id in all files in the given directory.
     * 
     * @param progressUpdater
     *            Used to monitor progress.
     * @param regionFolder
     *            The directory containing the region files.
     * @param from
     *            The original id, use -1 to convert all ids.
     * @param to
     *            The id to convert to, use -1 to let Minecraft recalculate the
     *            biomes when it loads the world.
     */
    public IdChanger(ProgressUpdater progressUpdater, File regionFolder, byte from, byte to) {
        this.progressUpdater = progressUpdater;
        this.regionFolder = regionFolder;
        this.from = from;
        this.to = to;
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
                    byte[] biomeIds = chunkTag.getByteArray("Biomes");
                    if (handleByteArray(biomeIds)) {
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
     * Converts all ids in the byte array. The original byte array will be
     * modified.
     * 
     * @param biomeIds
     *            The bytes to convert.
     * @return Whether changes were made to the array.
     */
    protected boolean handleByteArray(byte[] biomeIds) {
        boolean hasChanges = false;
        for (int i = 0; i < biomeIds.length; i++) {
            if (from == -1 || biomeIds[i] == from) {
                biomeIds[i] = to;
                hasChanges = true;
            }
        }
        return hasChanges;
    }
}
