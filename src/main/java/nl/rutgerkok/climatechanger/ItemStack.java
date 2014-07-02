package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.StringTag;
import nl.rutgerkok.climatechanger.util.MaterialNotFoundException;

import java.util.Objects;

public final class ItemStack {
    private static final String BLOCK_DATA_TAG = "Damage";
    private static final String BLOCK_ID_TAG = "id";

    private final CompoundTag tag;

    public ItemStack(CompoundTag tag) {
        this.tag = Objects.requireNonNull(tag);
    }

    /**
     * Gets the block data of this item.
     *
     * @return The block data.
     */
    public final short getBlockData() {
        return tag.getShort(BLOCK_DATA_TAG);
    }

    /**
     * Gets the material of this tag.
     *
     * @param map
     *            The material map used to lookup the name or id.
     * @return The material.
     * @throws MaterialNotFoundException
     *             If no block material is present. Usually happens in the case
     *             of item materials.
     * @throws NullPointerException
     *             If the material map is null.
     */
    public final Material getMaterial(MaterialMap map) throws MaterialNotFoundException {
        if (isBlockIdInStringFormat()) {
            // Try as string
            return map.getByName(tag.getString(BLOCK_ID_TAG));
        } else {
            // Try as number
            return map.getById(tag.getShort(BLOCK_ID_TAG));
        }
    }

    /**
     * Checks if this block has the given material.
     *
     * @param material
     *            The material to check.
     * @param data
     *            The data value to check. Use -1 as a wildcard.
     * @return True if the material and data match, false otherwise.
     * @throws NullPointerException
     *             If the given material is null.
     */
    public final boolean hasMaterial(Material material, short data) {
        // Check for block id
        if (isBlockIdInStringFormat()) {
            if (!tag.getString(BLOCK_ID_TAG).equals(material.getName())) {
                // Mismatched block name
                return false;
            }
        } else {
            if (tag.getShort(BLOCK_ID_TAG) != material.getId()) {
                // Mismatched block id
                return false;
            }
        }

        return data == -1 || data == getBlockData();
    }

    private boolean isBlockIdInStringFormat() {
        return tag.get("id") instanceof StringTag;
    }

    /**
     * Sets the material of this stack.
     *
     * @param material
     *            The material.
     * @param newBlockData
     *            The block data.
     * @throws NullPointerException
     *             If the material is null.
     */
    public void setMaterial(Material material, short newBlockData) {
        if (isBlockIdInStringFormat()) {
            tag.putString(BLOCK_ID_TAG, material.getName());
        } else {
            tag.putShort(BLOCK_ID_TAG, material.getId());
        }
        tag.putShort(BLOCK_DATA_TAG, newBlockData);
    }
}
