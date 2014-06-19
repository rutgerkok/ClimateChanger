package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.Chunk;

public class IdChanger implements ChunkTask {
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
    public IdChanger(byte from, byte to) {
        this.from = from;
        this.to = to;
    }

    public boolean execute(Chunk chunk) {
        return handleByteArray(chunk.getBiomeArray());
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
