package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.nbt.CompoundTag;

/**
 * Represents a single chunk, as used by Minecraft.
 *
 */
public class Chunk {
    public static final int CHUNK_X_SIZE = 16;
    public static final int CHUNK_Y_SIZE = 256;
    public static final int CHUNK_Z_SIZE = 16;

    private final CompoundTag chunkTag;

    /**
     * Creates a new chunk from the given data tag.
     * 
     * @param chunkTag
     *            The data tag, with child tags like Biomes, Sections, etc.
     */
    public Chunk(CompoundTag chunkTag) {
        this.chunkTag = chunkTag;
    }

    /**
     * Gets direct access to the chunk data tag. Modifying the returned chunk
     * data tag will modify the internal tag in this class.
     * 
     * @return The chunk data tag.
     */
    public CompoundTag getTag() {
        return chunkTag;
    }
    
    /**
     * Gets direct access to the biome array of this chunk. Modifying the byte
     * array will modify the data of this chunk.
     * @return The biome array.
     */
    public byte[] getBiomeArray() {
        return chunkTag.getByteArray("Biomes");
    }
}
