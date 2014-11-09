package nl.rutgerkok.climatechanger.world;

import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.*;

import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.material.MaterialData;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.ListTag;
import nl.rutgerkok.climatechanger.nbt.TagType;

import java.util.List;

/**
 * Represents a single chunk, as used by Minecraft.
 *
 */
public final class Chunk {

    public static final int CHUNK_X_SIZE = 16;
    public static final int CHUNK_Y_SIZE = 256;
    public static final int CHUNK_Z_SIZE = 16;

    /**
     * The highest possible biome id in a Minecraft map.
     */
    public static final int MAX_BIOME_ID = 254;

    /**
     * The highest possible block data in a Minecraft map.
     */
    public static final int MAX_BLOCK_DATA = 15;

    /**
     * The highest possible block id in a Minecraft map.
     */
    public static final int MAX_BLOCK_ID = 4095;

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
     * Gets direct access to the biome array of this chunk. Modifying the byte
     * array will modify the data of this chunk.
     *
     * @return The biome array.
     */
    public byte[] getBiomeArray() {
        return chunkTag.getByteArray(CHUNK_BIOMES_TAG);
    }

    /**
     * Gets the tags of the chunk sections.
     *
     * @return The tags.
     */
    public ListTag<CompoundTag> getChunkSections() {
        return chunkTag.getList(CHUNK_SECTIONS_TAG, TagType.COMPOUND);
    }

    /**
     * Gets the chunk x in the world.
     *
     * @return The chunk x.
     */
    public int getChunkX() {
        return chunkTag.getInt(CHUNK_X_POS_TAG);
    }

    /**
     * Gets the chunk z in the world.
     *
     * @return The chunk z.
     */
    public int getChunkZ() {
        return chunkTag.getInt(CHUNK_Z_POS_TAG);
    }

    /**
     * Gets all entities in this chunk.
     *
     * @return The entities.
     */
    public List<CompoundTag> getEntities() {
        return chunkTag.getList(CHUNK_ENTITIES_TAG, TagType.COMPOUND);
    }

    /**
     * Gets the material id at the given position.
     *
     * @param x
     *            X position of the block,
     *            <code>0 <= x < {@value #CHUNK_X_SIZE}</code>.
     * @param y
     *            Y position of the block,
     *            <code>0 <= y < {@value #CHUNK_Y_SIZE}</code>.
     * @param z
     *            Z position of the block,
     *            <code>0 <= z < {@value #CHUNK_Z_SIZE}</code>.
     * @return The id, or {@value Material#AIR_ID} if the coordinates are out of
     *         bounds.
     */
    public short getMaterialId(int x, int y, int z) {
        if (isOutOfBounds(x, y, z)) {
            return Material.AIR_ID;
        }
        return ChunkSection.getMaterialId(chunkTag, x, y, z);
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
     * Gets access to the tile entities in this chunk.
     *
     * @return The tile entities.
     */
    public List<CompoundTag> getTileEntities() {
        return chunkTag.getList(CHUNK_TILE_ENTITIES_TAG, TagType.COMPOUND);
    }

    private boolean isOutOfBounds(int x, int y, int z) {
        return x < 0 || x >= CHUNK_X_SIZE || y < 0 || y >= CHUNK_Y_SIZE || z < 0 || z >= CHUNK_Z_SIZE;
    }

    /**
     * Sets the block at the given position. Silently fails if the position is
     * out of bounds.
     *
     * @param x
     *            X position of the block,
     *            <code>0 <= x < {@value #CHUNK_X_SIZE}</code>.
     * @param y
     *            Y position of the block,
     *            <code>0 <= y < {@value #CHUNK_Y_SIZE}</code>.
     * @param z
     *            Z position of the block,
     *            <code>0 <= z < {@value #CHUNK_Z_SIZE}</code>.
     * @param materialData
     *            Material to set.
     */
    public void setBlock(int x, int y, int z, MaterialData materialData) {
        if (isOutOfBounds(x, y, z)) {
            return;
        }

        ChunkSection.setMaterialId(chunkTag, x, y, z, materialData.getMaterial().getId());
        ChunkSection.setMaterialData(chunkTag, x, y, z, materialData.getData());
    }
}
