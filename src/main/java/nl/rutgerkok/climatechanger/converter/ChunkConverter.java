package nl.rutgerkok.climatechanger.converter;

import nl.rutgerkok.climatechanger.Chunk;
import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.RegionFile;
import nl.rutgerkok.climatechanger.World;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.NbtIo;
import nl.rutgerkok.climatechanger.task.ChunkTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * Converter for running all tasks that implement {@link ChunkTask}.
 *
 */
class ChunkConverter implements Converter {
    private final List<? extends ChunkTask> tasks;
    private final World world;

    /**
     * Changes the biome id in all files in the given directory.
     *
     * @param progressUpdater
     *            Used to monitor progress.
     * @param world
     *            The world
     * @param tasks
     *            The tasks to execute for each chunk.
     */
    ChunkConverter(World world, List<? extends ChunkTask> tasks) {
        this.world = world;
        this.tasks = tasks;
    }

    @Override
    public void convert(ProgressUpdater updater) throws IOException {
        Collection<Path> regionDirectories = world.getRegionFolders();
        for (Path regionDirectory : regionDirectories) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(regionDirectory)) {
                for (Path regionFile : stream) {
                    if (regionFile.getFileName().toString().endsWith(".mca")) {
                        convertFile(regionFile);
                    }
                    updater.incrementProgress();
                }
            }
        }
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
    private int convertFile(Path file) throws IOException {
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
                } catch (IOException e) {
                    // Rethrow with location information and same stacktrace
                    IOException newE = new IOException("[Chunk " + chunkX + ","
                            + chunkZ + " in region file " + file.getFileName() + "] " + e.getMessage());
                    newE.setStackTrace(e.getStackTrace());
                    throw newE;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }
        }
        regionFile.close();
        return changedChunks;
    }

    /**
     * Gets the sum of the file count in each directory. Only counts files
     * directly in each directory, not in subdirectories.
     *
     * @return The total amount of files.
     * @throws IOException
     *             If counting fails.
     */
    @Override
    public int getUnitsToConvert() throws IOException {
        int size = 0;
        for (Path directory : world.getRegionFolders()) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (@SuppressWarnings("unused")
                Path file : stream) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * Runs all the tasks of this chunk.
     *
     * @param chunk
     *            The chunk to modify.
     * @return True if one of the tasks changed the chunk data, false otherwise.
     */
    private boolean runTasks(Chunk chunk) {
        try {
            boolean changed = false;
            for (ChunkTask task : tasks) {
                if (task.convertChunk(chunk)) {
                    changed = true;
                }
            }
            return changed;
        } catch (RuntimeException e) {
            throw new RuntimeException("Exception in chunk " + chunk.getChunkX() + "," + chunk.getChunkZ(), e);
        }
    }

}
