package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.material.ForgeMaterialMap;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.material.VanillaMaterialMap;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.NbtIo;
import nl.rutgerkok.climatechanger.nbt.TagType;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private static DirectoryStream.Filter<Path> worldDirectories(final String prefix) {
        return new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path path) throws IOException {
                return Files.isDirectory(path)
                        && path.getFileName().toString().startsWith(prefix)
                        && Files.exists(path.resolve(REGION_FOLDER_NAME));
            }
        };
    }

    private final Path levelDat;
    private final MaterialMap materialMap;

    private final CompoundTag tag;

    public World(Path levelDat) throws IOException {
        if (!levelDat.getFileName().toString().equals(LEVEL_DAT_NAME)) {
            throw new IOException("Expected a " + LEVEL_DAT_NAME + " file, got \"" + levelDat.getName(levelDat.getNameCount() - 1)  +"\"");
        }
        this.levelDat = levelDat.toAbsolutePath();
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
    private Path getDirectory(String name) {
        Path file = levelDat.getParent().resolveSibling(name);
        if (Files.isDirectory(file)) {
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
    public Path getPlayerDirectory() {
        // Try modern file
        Path dir = getDirectory(PLAYER_DIRECTORY);
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
     * @throws IOException
     *             If the folders cannot be read.
     */
    public Collection<Path> getRegionFolders() throws IOException {
        Collection<Path> regionFolders = new HashSet<>();
        Path levelDirectory = levelDat.getParent();

        // Search for region folder next to level.dat
        Path normalRegionFolder = getDirectory(REGION_FOLDER_NAME);
        if (Files.isDirectory(normalRegionFolder)) {
            regionFolders.add(normalRegionFolder);
        }

        // Search for other dimensions next to level.dat
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(levelDirectory, worldDirectories("DIM"))) {
            for (Path file : stream) {
                regionFolders.add(file);
            }
        }

        // Search step above (CraftBukkit and Spigot store dimensions there)
        String worldDirectoryName = levelDirectory.getFileName().toString();
        Path aboveLevelDirectory = levelDirectory.getParent();

        if (aboveLevelDirectory == null) {
            // World is stored in server root, abort
            return regionFolders;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(levelDirectory, worldDirectories(worldDirectoryName + "_"))) {
            for (Path file : stream) {
                regionFolders.add(file.resolve(REGION_FOLDER_NAME));
            }
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
