package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.util.Result;

public class BiomeIdChanger implements ChunkTask {
    private final int from;
    private final int to;

    /**
     * Changes the biome id in all files in the given directory.
     *
     * @param from
     *            The original id, use -1 to convert all ids.
     * @param to
     *            The id to convert to, use -1 to let Minecraft recalculate the
     *            biomes when it loads the world.
     */
    public BiomeIdChanger(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Result convertChunk(AnvilChunk chunk) {
        int[] biomeArray = chunk.getBiomeArray();
        if (biomeArray.length == 0) {
            return handleByteArray(chunk.getOldBiomeArray()) ? Result.CHANGED : Result.NO_CHANGES;
        }
        return handleIntArray(biomeArray) ? Result.CHANGED : Result.NO_CHANGES;
    }

    @Override
    public String getDescription() {
        return "Change biome id " + from + " into " + to;
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
        if (((byte) to) != to) {
            return false; // Cannot store this biome
        }
        boolean hasChanges = false;
        for (int i = 0; i < biomeIds.length; i++) {
            if (from == -1 || biomeIds[i] == from) {
                biomeIds[i] = (byte) to;
                hasChanges = true;
            }
        }
        return hasChanges;
    }

    /**
     * Converts all ids in the byte array. The original byte array will be
     * modified.
     *
     * @param biomeIds
     *            The bytes to convert.
     * @return Whether changes were made to the array.
     */
    private boolean handleIntArray(int[] biomeIds) {
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
