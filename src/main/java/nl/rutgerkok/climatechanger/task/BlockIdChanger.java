package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.Chunk;
import nl.rutgerkok.climatechanger.ItemStack;
import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.TagType;
import nl.rutgerkok.climatechanger.util.NibbleArray;

import java.util.List;

public class BlockIdChanger implements ChunkTask, PlayerDataTask {

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
        if (stack.hasMaterial(oldBlock, oldBlockData)) {
            stack.setMaterial(newBlock, newBlockData);
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
        if (tileEntity.getString("id").equalsIgnoreCase("FlowerPot")) {
            String blockId = tileEntity.getString("Item");
            short blockData = tileEntity.getShort("Data");
            if (oldBlock.getName().equals(blockId) && (oldBlockData == -1 || blockData == oldBlockData)) {
                tileEntity.putString("Item", newBlock.getName());
                tileEntity.putInt("Data", newBlockData);
                return true;
            }
        }

        // Special case for Piston (like FlowerPot, but uses item ids instead
        // of names and different keys)
        if (tileEntity.getString("id").equalsIgnoreCase("Piston")) {
            short blockId = tileEntity.getShort("blockId");
            short blockData = tileEntity.getShort("blockData");
            if (oldBlock.getId() == blockId && (oldBlockData == -1 || blockData == oldBlockData)) {
                tileEntity.putInt("blockId", newBlock.getId());
                tileEntity.putInt("blockData", newBlockData);
                return true;
            }
        }

        return false;
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

            if (blockId == oldBlockId && (oldBlockData == -1 || blockData == oldBlockData)) {
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
