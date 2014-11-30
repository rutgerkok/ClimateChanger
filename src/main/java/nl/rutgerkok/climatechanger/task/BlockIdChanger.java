package nl.rutgerkok.climatechanger.task;

import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.TILE_ENTITY_ID_TAG;

import nl.rutgerkok.climatechanger.ItemStack;
import nl.rutgerkok.climatechanger.material.MaterialData;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.TagType;
import nl.rutgerkok.climatechanger.util.NibbleArray;
import nl.rutgerkok.climatechanger.world.Chunk;

import java.util.List;

public class BlockIdChanger implements ChunkTask, PlayerDataTask {

    private final MaterialData newBlock;
    private final short newBlockId;
    private final byte newBlockIdHighestBytes;
    private final byte newBlockIdLowestBytes;
    private final byte newBlockDataByte;

    private final MaterialData oldBlock;
    private final short oldBlockId;
    private final byte oldBlockDataByte;

    /**
     * Creates a new block id change task.
     *
     * @param oldBlockId
     *            Old block id, use -1 as a wildcard.
     * @param oldBlockDataByte
     *            Old block data, use -1 as a wildcard.
     * @param newBlockId
     *            New block id.
     * @param newBlockDataByte
     *            New block data.
     */
    public BlockIdChanger(MaterialData oldBlock, MaterialData newBlock) {
        this.oldBlock = oldBlock;
        this.oldBlockId = oldBlock.getMaterial().getId();
        this.oldBlockDataByte = oldBlock.getData();

        this.newBlock = newBlock;
        this.newBlockId = newBlock.getMaterial().getId();
        this.newBlockIdLowestBytes = (byte) this.newBlockId;
        this.newBlockIdHighestBytes = (byte) (this.newBlockId >> 8);
        this.newBlockDataByte = newBlock.getData();
    }

    @Override
    public boolean convertChunk(Chunk chunk) {
        boolean changed = false;

        // Replace the blocks in all sections
        for (CompoundTag section : chunk.getChunkSections()) {
            if (replaceSection(section)) {
                changed = true;
            }
        }

        // Replace the blocks in tile entities
        if (convertTileEntities(chunk.getTileEntities())) {
            changed = true;
        }

        // Replace the block in item frames
        if (convertEntities(chunk.getEntities())) {
            changed = true;
        }

        return changed;
    }

    /**
     * Converts a list of entities.
     *
     * @param entities
     *            The entities.
     * @return True if changes were made, false otherwise.
     */
    private boolean convertEntities(List<CompoundTag> entities) {
        boolean changed = false;
        for (CompoundTag entity : entities) {
            if (convertEntity(entity)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Converts a single entity
     *
     * @param entity
     *            The entity.
     * @return True if changes were made, false otherwise.
     */
    private boolean convertEntity(CompoundTag entity) {
        // Items on the ground, item frames
        if (entity.contains("Item")) {
            return convertItem(new ItemStack(entity.getCompound("Item")));
        }

        // Items in mine carts with chests/hoppers and in horses
        if (entity.contains("Items")) {
            return convertItemList(entity.getList("Items", TagType.COMPOUND));
        }

        return false;
    }

    /**
     * Converts a single item.
     *
     * @param stack
     *            The item to convert.
     * @return True if the item was changed, false otherwise.
     */
    private boolean convertItem(ItemStack stack) {
        if (stack.hasMaterialData(oldBlock)) {
            stack.setMaterialData(newBlock);
            return true;
        }
        return false;
    }

    /**
     * Converts a list of items.
     *
     * @param inventory
     *            The list of items to convert.
     * @return True if at least a single item was changed.
     */
    private boolean convertItemList(List<CompoundTag> inventory) {
        boolean changed = false;

        for (CompoundTag inventoryTag : inventory) {
            if (convertItem(new ItemStack(inventoryTag))) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean convertPlayerFile(CompoundTag tag) {
        boolean changed = false;

        if (convertItemList(tag.getList("Inventory", TagType.COMPOUND))) {
            changed = true;
        }
        if (convertItemList(tag.getList("EnderItems", TagType.COMPOUND))) {
            changed = true;
        }

        return changed;
    }

    private boolean convertTileEntities(List<CompoundTag> tileEntities) {
        boolean changed = false;
        for (CompoundTag tileEntity : tileEntities) {
            if (convertTileEntity(tileEntity)) {
                changed = true;
            }
        }
        return changed;
    }

    private boolean convertTileEntity(CompoundTag tileEntity) {
        if (tileEntity.contains("Items")) {
            // Most tile entities with an inventory in Minecraft currently have
            // an "Items" tag with the items
            return convertItemList(tileEntity.getList("Items", TagType.COMPOUND));
        }

        // Special case for FlowerPot (uses Item and Data tag)
        if (tileEntity.getString(TILE_ENTITY_ID_TAG).equalsIgnoreCase("FlowerPot")) {
            String blockId = tileEntity.getString("Item");
            short blockData = tileEntity.getShort("Data");
            if (oldBlock.materialNameEquals(blockId) && (oldBlock.blockDataMatches(blockData))) {
                tileEntity.putString("Item", newBlock.getMaterial().getName());
                tileEntity.putInt("Data", newBlockDataByte);
                return true;
            }
        }

        // Special case for Piston (like FlowerPot, but uses item ids instead
        // of names and different keys)
        if (tileEntity.getString(TILE_ENTITY_ID_TAG).equalsIgnoreCase("Piston")) {
            short blockId = tileEntity.getShort("blockId");
            short blockData = tileEntity.getShort("blockData");
            if (oldBlockId == blockId && (oldBlockDataByte == -1 || blockData == oldBlockDataByte)) {
                tileEntity.putInt("blockId", newBlockId);
                tileEntity.putInt("blockData", newBlockDataByte);
                return true;
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Change blocks with id " + oldBlock + " into " + newBlock;
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

            if (blockId == oldBlockId && (oldBlock.blockDataMatches(blockData))) {
                // Found match, replace block
                changed = true;
                blockIdsBase[i] = newBlockIdLowestBytes;
                blockDatas.set(i, newBlockDataByte);

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
