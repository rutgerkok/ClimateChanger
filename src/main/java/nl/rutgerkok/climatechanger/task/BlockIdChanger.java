package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.Chunk;
import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.util.NibbleArray;

public class BlockIdChanger implements ChunkTask {

    private final Material newBlock;
    private final byte newBlockData;
    private final byte newBlockIdHighestBytes;
    private final byte newBlockIdLowestBytes;

    private final Material oldBlock;
    private final byte oldBlockData;
    private final short oldBlockId;

    /**
     * Creates a new block id change task.
     *
     * @param oldBlockId
     *            Old block id, use -1 as a wildcard.
     * @param oldBlockData
     *            Old block data, use -1 as a wildcard.
     * @param newBlockId
     *            New block id.
     * @param newBlockData
     *            New block data.
     */
    public BlockIdChanger(Material oldBlock, byte oldBlockData, Material newBlock, byte newBlockData) {
        this.oldBlock = oldBlock;
        this.oldBlockId = oldBlock.getId();
        this.oldBlockData = oldBlockData;

        this.newBlock = newBlock;
        this.newBlockIdLowestBytes = (byte) newBlock.getId();
        this.newBlockIdHighestBytes = (byte) (newBlock.getId() >> 8);
        this.newBlockData = newBlockData;
    }

    @Override
    public boolean execute(Chunk chunk) {
        boolean changed = false;

        // Replace the blocks in all sections
        for (CompoundTag section : chunk.getChunkSections()) {
            if (replaceSection(section)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public String getDescription() {
        return "Change blocks with id " + oldBlock + ":" + oldBlockData + " into " + newBlock + ":" + newBlockData;
    }

    private boolean replaceSection(CompoundTag section) {
        boolean changed = false;

        byte[] blockIdsBase = section.getByteArray("Blocks");
        NibbleArray blockIdsExtended = section.contains("Add") ? new NibbleArray(section.getByteArray("Add")) : null;
        NibbleArray blockDatas = new NibbleArray(section.getByteArray("Data"));

        // Convert ids
        for (int i = 0; i < blockIdsBase.length; i++) {
            // Get block id
            int blockId = blockIdsBase[i] & 0xFF;
            if (blockIdsExtended != null) {
                blockId |= ((blockIdsExtended.get(i) & 0xFF) << 8);
            }

            byte blockData = blockDatas.get(i);

            if ((oldBlockId == -1 || blockId == oldBlockId) && (oldBlockData == -1 || blockData == oldBlockData)) {
                // Found match, replace block
                changed = true;
                blockIdsBase[i] = newBlockIdLowestBytes;
                blockDatas.set(i, newBlockData);

                // Set extended id
                if (blockIdsExtended != null) {
                    blockIdsExtended.set(i, newBlockIdHighestBytes);
                } else {
                    // Extra ids are not initialized
                    if (newBlockIdHighestBytes != 0) {
                        // It is necessary to do that now
                        blockIdsExtended = new NibbleArray(blockIdsBase.length);
                        section.putByteArray("Add", blockIdsExtended.getHandle());
                        blockIdsExtended.set(i, newBlockIdHighestBytes);
                    }
                }
            }
        }

        return changed;
    }

}
