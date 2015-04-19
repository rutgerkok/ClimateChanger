package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.hammer.GameFactory;
import nl.rutgerkok.hammer.ItemStack;
import nl.rutgerkok.hammer.PlayerFile;
import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.anvil.tag.AnvilFormat.EntityTag;
import nl.rutgerkok.hammer.anvil.tag.AnvilFormat.PlayerTag;
import nl.rutgerkok.hammer.anvil.tag.AnvilFormat.SectionTag;
import nl.rutgerkok.hammer.anvil.tag.AnvilFormat.TileEntityTag;
import nl.rutgerkok.hammer.material.MaterialData;
import nl.rutgerkok.hammer.tag.CompoundTag;
import nl.rutgerkok.hammer.tag.TagType;
import nl.rutgerkok.hammer.util.NibbleArray;
import nl.rutgerkok.hammer.util.Result;

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
     * @param oldBlock
     *            Old block.
     * @param newBlock
     *            New block.
     */
    public BlockIdChanger(MaterialData oldBlock, MaterialData newBlock) {
        this.oldBlock = oldBlock;
        this.oldBlockId = oldBlock.getMaterial().getId();
        this.oldBlockDataByte = oldBlock.isBlockDataUnspecified() ? -1 : oldBlock.getData();

        this.newBlock = newBlock;
        this.newBlockId = newBlock.getMaterial().getId();
        this.newBlockIdLowestBytes = (byte) this.newBlockId;
        this.newBlockIdHighestBytes = (byte) (this.newBlockId >> 8);
        this.newBlockDataByte = newBlock.getData();
    }

    @Override
    public Result convertChunk(AnvilChunk chunk) {
        Result result = Result.NO_CHANGES;
        GameFactory gameFactory = chunk.getGameFactory();

        // Replace the blocks in all sections
        for (CompoundTag section : chunk.getChunkSections()) {
            if (replaceSection(section)) {
                result = Result.CHANGED;
            }
        }

        // Replace the blocks in tile entities
        if (convertTileEntities(gameFactory, chunk.getTileEntities())) {
            result = Result.CHANGED;
        }

        // Replace the block in item frames
        if (convertEntities(gameFactory, chunk.getEntities())) {
            result = Result.CHANGED;
        }

        return result;
    }

    /**
     * Converts a list of entities.
     *
     * @param gameFactory
     *            The game factory.
     * @param entities
     *            The entities.
     * @return True if changes were made, false otherwise.
     */
    private boolean convertEntities(GameFactory gameFactory, List<CompoundTag> entities) {
        boolean changed = false;
        for (CompoundTag entity : entities) {
            if (convertEntity(gameFactory, entity)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Converts a single entity
     *
     * @param gameFactory
     *            The game factory.
     * @param entity
     *            The entity.
     * @return True if changes were made, false otherwise.
     */
    private boolean convertEntity(GameFactory gameFactory, CompoundTag entity) {
        // Items on the ground, item frames
        if (entity.containsKey(EntityTag.ITEM)) {
            return convertItem(gameFactory.createItemStack(entity.getCompound(EntityTag.ITEM)));
        }

        // Items in mine carts with chests/hoppers and in horses
        if (entity.containsKey(EntityTag.ITEMS)) {
            return convertItemList(gameFactory, entity.getList(EntityTag.ITEM, TagType.COMPOUND));
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
     * @param gameFactory
     *            The game factory.
     * @param inventory
     *            The list of items to convert.
     * @return True if at least a single item was changed.
     */
    private boolean convertItemList(GameFactory gameFactory, List<CompoundTag> inventory) {
        boolean changed = false;

        for (CompoundTag inventoryTag : inventory) {
            if (convertItem(gameFactory.createItemStack(inventoryTag))) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public Result convertPlayerFile(PlayerFile playerFile) {
        Result result = Result.NO_CHANGES;
        CompoundTag tag = playerFile.getTag();
        GameFactory gameFactory = playerFile.getGameFactory();

        if (convertItemList(gameFactory, tag.getList(PlayerTag.INVENTORY, TagType.COMPOUND))) {
            result = Result.CHANGED;
        }
        if (convertItemList(gameFactory, tag.getList(PlayerTag.ENDER_INVENTORY, TagType.COMPOUND))) {
            result = Result.CHANGED;
        }

        return result;
    }

    private boolean convertTileEntities(GameFactory gameFactory, List<CompoundTag> tileEntities) {
        boolean changed = false;
        for (CompoundTag tileEntity : tileEntities) {
            if (convertTileEntity(gameFactory, tileEntity)) {
                changed = true;
            }
        }
        return changed;
    }

    private boolean convertTileEntity(GameFactory gameFactory, CompoundTag tileEntity) {
        if (tileEntity.containsKey(TileEntityTag.ITEMS)) {
            // Most tile entities with an inventory in Minecraft currently have
            // an "Items" tag with the items
            return convertItemList(gameFactory, tileEntity.getList(TileEntityTag.ITEMS, TagType.COMPOUND));
        }

        // Special case for FlowerPot (uses Item and Data tag)
        if (tileEntity.getString(TileEntityTag.ID).equalsIgnoreCase("FlowerPot")) {
            String blockId = tileEntity.getString(TileEntityTag.FLOWER_POT_BLOCK_NAME);
            byte blockData = (byte) tileEntity.getShort(TileEntityTag.FLOWER_POT_BLOCK_DATA);
            if (oldBlock.materialNameEquals(blockId) && blockDataMatches(oldBlockDataByte, blockData)) {
                tileEntity.setString(TileEntityTag.FLOWER_POT_BLOCK_NAME, newBlock.getMaterial().getName());
                tileEntity.setShort(TileEntityTag.FLOWER_POT_BLOCK_DATA, newBlockDataByte);
                return true;
            }
        }

        // Special case for Piston (like FlowerPot, but uses item ids instead
        // of names and different keys)
        if (tileEntity.getString(TileEntityTag.ID).equalsIgnoreCase("Piston")) {
            short blockId = tileEntity.getShort(TileEntityTag.PISTON_BLOCK_ID);
            short blockData = tileEntity.getShort(TileEntityTag.PISTON_BLOCK_DATA);
            if (oldBlockId == blockId && (oldBlockDataByte == -1 || blockData == oldBlockDataByte)) {
                tileEntity.setShort(TileEntityTag.PISTON_BLOCK_ID, newBlockId);
                tileEntity.setShort(TileEntityTag.PISTON_BLOCK_DATA, newBlockDataByte);
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

        byte[] blockIdsBase = section.getByteArray(SectionTag.BLOCK_IDS, 4096);
        NibbleArray blockIdsExtended = section.containsKey(SectionTag.EXT_BLOCK_IDS) ? new NibbleArray(section.getByteArray(SectionTag.EXT_BLOCK_IDS, 2048)) : null;
        NibbleArray blockDatas = new NibbleArray(section.getByteArray(SectionTag.BLOCK_DATA, 2048));

        // Convert ids
        for (int i = 0; i < blockIdsBase.length; i++) {
            // Get block id
            int blockId = blockIdsBase[i] & 0xFF;
            if (blockIdsExtended != null) {
                blockId |= ((blockIdsExtended.get(i) & 0xFF) << 8);
            }

            byte blockData = blockDatas.get(i);

            if (blockId == oldBlockId && blockDataMatches(oldBlockDataByte, blockData)) {
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
                        section.setByteArray(SectionTag.EXT_BLOCK_IDS, blockIdsExtended.getHandle());
                        blockIdsExtended.set(i, newBlockIdHighestBytes);
                    }
                }
            }
        }

        return changed;
    }

    private boolean blockDataMatches(byte oldBlockData, byte newBlockData) {
        if (oldBlockData == -1) {
            return true;
        }
        return oldBlockData == newBlockData;
    }

}
