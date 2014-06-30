package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.material.ForgeMaterialMap;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.material.VanillaMaterialMap;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.NbtIo;
import nl.rutgerkok.climatechanger.nbt.TagType;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a Minecraft world.
 *
 */
public class World {
    private static final String FML_ITEM_DATA_TAG = "ItemData";
    private static final String FML_TAG = "FML";
    public static final String LEVEL_DAT_NAME = "level.dat";
    private static final String PLAYER_DIRECTORY = "playerdata";
    private static final String PLAYER_DIRECTORY_OLD = "players";
    private static final String REGION_FOLDER_NAME = "region";

    private static FileFilter worldDirectories(final String prefix) {
        return new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory()
                        && file.getName().startsWith(prefix)
                        && new File(file, REGION_FOLDER_NAME).exists();
            }
        };
    }

    private final File levelDat;
    private final MaterialMap materialMap;

    private final CompoundTag tag;

    public World(File levelDat) throws IOException {
        if (!levelDat.getName().equals(LEVEL_DAT_NAME)) {
            throw new IOException("Expected a " + LEVEL_DAT_NAME + " file, got " + levelDat.getName());
        }
        this.levelDat = levelDat.getAbsoluteFile();
        tag = NbtIo.readCompressedFile(levelDat);
        materialMap = initMaterialMap();
    }

    /**
     * Gets the directory next to the level.dat with the given name.
     *
     * @param name
     *            Name of the directory.
     * @return The directory, or null if not found.
     */
    private File getDirectory(String name) {
        File file = new File(levelDat.getParentFile(), name);
        if (file.isDirectory()) {
            return file;
        }
        return null;
    }

    /**
     * Gets the material map of this world.
     *
     * @return The material map.
     */
    public MaterialMap getMaterialMap() {
        return materialMap;
    }

    /**
     * Gets the player directory. May be null if no player directory exists.
     *
     * @return The player directory.
     */
    public File getPlayerDirectory() {
        // Try modern file
        File dir = getDirectory(PLAYER_DIRECTORY);
        if (dir != null) {
            return dir;
        }

        // Try again with old file
        return getDirectory(PLAYER_DIRECTORY_OLD);
    }

    /**
     * Gets all region folders of all dimensions of this world.
     *
     * @return All region folders.
     */
    public Collection<File> getRegionFolders() {
        Collection<File> regionFolders = new HashSet<>();
        File levelDirectory = levelDat.getParentFile();

        // Search for region folder next to level.dat
        File normalRegionFolder = new File(levelDirectory, REGION_FOLDER_NAME);
        if (normalRegionFolder.exists() && normalRegionFolder.isDirectory()) {
            regionFolders.add(normalRegionFolder);
        }

        // Search for other dimensions next to level.dat
        for (File file : levelDirectory.listFiles(worldDirectories("DIM"))) {
            regionFolders.add(file);
        }

        // Search step above (CraftBukkit and Spigot store dimensions there)
        String worldDirectoryName = levelDirectory.getName();
        File aboveLevelDirectory = levelDirectory.getParentFile();

        if (aboveLevelDirectory == null) {
            // World is stored in server root, abort
            return regionFolders;
        }

        for (File file : aboveLevelDirectory.listFiles(worldDirectories(worldDirectoryName + "_"))) {
            regionFolders.add(new File(file, REGION_FOLDER_NAME));
        }
        return regionFolders;
    }

    /**
     * Gets the NBT root tag of the level.dat file.
     *
     * @return The NBT root tag.
     */
    public CompoundTag getTag() {
        return tag;
    }

    /**
     * Scans the level.dat for a Forge name->id map, if found it used that,
     * otherwise it uses the vanilla ids.
     *
     * @return The id map.
     */
    private MaterialMap initMaterialMap() {
        if (tag.contains(FML_TAG)) {
            CompoundTag fmlTag = tag.getCompound(FML_TAG);
            if (fmlTag.contains(FML_ITEM_DATA_TAG)) {
                return new ForgeMaterialMap(fmlTag.getList(FML_ITEM_DATA_TAG, TagType.COMPOUND));
            }
        }
        return new VanillaMaterialMap();
    }
}
