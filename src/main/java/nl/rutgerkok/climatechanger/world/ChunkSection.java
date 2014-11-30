package nl.rutgerkok.climatechanger.world;

import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.CHUNK_SECTIONS_TAG;
import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.SECTION_BLOCK_DATA_TAG;
import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.SECTION_BLOCK_IDS_TAG;
import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.SECTION_EXT_BLOCK_IDS_TAG;
import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.SECTION_Y_TAG;

import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.TagType;
import nl.rutgerkok.climatechanger.util.NibbleArray;

import java.util.List;

/**
 * Static utility methods to work with chunk sections of a chunk.
 *
 */
final class ChunkSection {
    private static final int SECTION_X_BITS = 4;
    private static final int SECTION_X_SIZE = Chunk.CHUNK_X_SIZE;
    private static final int SECTION_Y_BITS = 4;
    private static final int SECTION_Y_SIZE = 16;
    private static final int SECTION_Z_BITS = 4;
    private static final int SECTION_Z_SIZE = Chunk.CHUNK_Z_SIZE;

    /**
     * Gets the block id at the given position.
     *
     * @param chunkTag
     *            Chunk data tag.
     * @param x
     *            X in the chunk.
     * @param y
     *            Y in the chunk.
     * @param z
     *            Z in the chunk.
     * @return The block id.
     * @throws ArrayIndexOutOfBoundsException
     *             If the x, y or z are out of bounds.
     */
    static final short getMaterialId(CompoundTag chunkTag, int x, int y, int z) {
        CompoundTag section = getChunkSection(chunkTag, y);
        if (section == null) {
            return Material.AIR_ID;
        }

        int yInSection = y & (SECTION_Y_SIZE - 1);
        int position = getPositionInSectionArray(x, yInSection, z);

        byte[] blocks = section.getByteArray(SECTION_BLOCK_IDS_TAG);
        byte[] extBlocks = section.getByteArray(SECTION_EXT_BLOCK_IDS_TAG);

        int blockId = blocks[position] & 0xff;
        if (extBlocks.length != 0) {
            blockId |= NibbleArray.getInArray(extBlocks, position) << Byte.SIZE;
        }

        return (short) blockId;
    }

    private static CompoundTag getChunkSection(CompoundTag chunkTag, int y) {
        if (y < 0 || y >= Chunk.CHUNK_Y_SIZE) {
            return null;
        }
        List<CompoundTag> sections = chunkTag.getList(CHUNK_SECTIONS_TAG, TagType.COMPOUND);

        int sectionIndex = y >>> SECTION_Y_BITS;

        if (sectionIndex < sections.size()) {
            // Do a guess (correct only if no chunk sections are omitted at
            // and below this section index)
            CompoundTag section = sections.get(sectionIndex);
            if (section != null && section.getByte(SECTION_Y_TAG) == sectionIndex) {
                return section;
            }
        }

        // Search for section
        for (CompoundTag section : sections) {
            if (section != null && section.getByte(SECTION_Y_TAG) == sectionIndex) {
                return section;
            }
        }
        return null;
    }

    private static int getPositionInSectionArray(int xInSection, int yInSection, int zInSection) {
        return yInSection << (SECTION_X_BITS + SECTION_Z_BITS) | zInSection << SECTION_X_BITS | xInSection;
    }

    /**
     * Sets the material data at the given position. Silently fails when setting
     * a block in a non-existent section.
     *
     * @param chunkTag
     *            Chunk data tag.
     * @param x
     *            X in the chunk.
     * @param y
     *            Y in the chunk.
     * @param z
     *            Z in the chunk.
     * @param data
     *            The material data.
     */
    static void setMaterialData(CompoundTag chunkTag, int x, int y, int z, byte data) {
        CompoundTag section = getChunkSection(chunkTag, y);
        if (section == null) {
            // Silently fail
            return;
        }

        int yInSection = y & (SECTION_Y_SIZE - 1);
        int position = getPositionInSectionArray(x, yInSection, z);

        byte[] dataArray = section.getByteArray(SECTION_BLOCK_DATA_TAG);
        NibbleArray.setInArray(dataArray, position, data);
    }

    /**
     * Sets the material id at the given position. Silently fails when setting a
     * block in a non-existent section.
     *
     * @param chunkTag
     *            Chunk data tag.
     * @param x
     *            X in the chunk.
     * @param y
     *            Y in the chunk.
     * @param z
     *            Z in the chunk.
     * @param id
     *            The block id.
     */
    static void setMaterialId(CompoundTag chunkTag, int x, int y, int z, short id) {
        CompoundTag section = getChunkSection(chunkTag, y);
        if (section == null) {
            // Silently fail
            return;
        }

        int yInSection = y & (SECTION_Y_SIZE - 1);
        int position = getPositionInSectionArray(x, yInSection, z);

        // Set low id
        byte[] blocks = section.getByteArray(SECTION_BLOCK_IDS_TAG);
        blocks[position] = (byte) id;

        // Set high id
        if (id > 0xff) {
            int highId = id >>> Byte.SIZE;
            byte[] extBlocks = section.getByteArray(SECTION_EXT_BLOCK_IDS_TAG);
            if (extBlocks.length == 0) {
                // Create array for this high id
                extBlocks = new NibbleArray(SECTION_X_SIZE * SECTION_Y_SIZE * SECTION_Z_SIZE).getHandle();
                section.putByteArray(SECTION_EXT_BLOCK_IDS_TAG, extBlocks);
            }
            NibbleArray.setInArray(extBlocks, position, (byte) highId);
        }
    }

    private ChunkSection() {
        // Private
    }
}
