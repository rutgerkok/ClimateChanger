package nl.rutgerkok.climatechanger.converter;

import nl.rutgerkok.climatechanger.ProgressUpdater;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.NbtIo;
import nl.rutgerkok.climatechanger.task.PlayerDataTask;
import nl.rutgerkok.climatechanger.util.DirectoryUtil;
import nl.rutgerkok.climatechanger.world.World;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Converts all player inventory files.
 *
 */
class PlayerDataConverter implements Converter {

    private final List<? extends PlayerDataTask> tasks;
    private final World world;

    PlayerDataConverter(World world, List<? extends PlayerDataTask> tasks) {
        this.world = Objects.requireNonNull(world);
        this.tasks = Objects.requireNonNull(tasks);
    }

    @Override
    public void convert(ProgressUpdater updater) throws IOException {
        // Convert player files
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(world.getPlayerDirectory())) {
            for (Path file : stream) {
                convertFile(file);
                updater.incrementProgress();
            }
        }

        // Convert level.dat
        if (convertLevelDatTag(world.getMinecraftTag())) {
            world.setTagNeedsSaving();
        }
        updater.incrementProgress();
    }

    /**
     * Converts a single player file.
     *
     * @param playerFile
     *            The file to convert.
     * @throws IOException
     *             If an IO error occurs.
     */
    private void convertFile(Path playerFile) throws IOException {
        try {
            CompoundTag tag = NbtIo.readCompressedFile(playerFile);
            if (convertTag(tag)) {
                NbtIo.writeCompressed(tag, playerFile);
            }
        } catch (Exception e) {
            IOException newE = new IOException("[File " + playerFile.getFileName() + "]" + e.getMessage());
            newE.setStackTrace(e.getStackTrace());
            throw newE;
        }
    }

    /**
     * Converts the "Player" tag in the level.dat file. If there is no "Player"
     * tag in the level.dat file (happens on servers), this method does nothing
     * and returns false.
     *
     * @param levelDatTag
     *            The Data tag in the level.dat file, with subtags like
     *            "GameRules".
     * @return True if the level.dat was changed, false otherwise.
     */
    private boolean convertLevelDatTag(CompoundTag levelDatTag) {
        if (levelDatTag.contains("Player")) {
            return convertTag(levelDatTag.getCompound("Player"));
        }
        return false;
    }

    /**
     * Converts the player tag.
     *
     * @param playerTag
     *            The player tag, with tags like "Inventory".
     * @return True if the tag was changed, false otherwise.
     */
    private boolean convertTag(CompoundTag playerTag) {
        boolean changed = false;
        for (PlayerDataTask task : tasks) {
            if (task.convertPlayerFile(playerTag)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public int getUnitsToConvert() throws IOException {
        // Add 1 for the level.dat conversion
        return DirectoryUtil.countFiles(world.getPlayerDirectory()) + 1;
    }

}
