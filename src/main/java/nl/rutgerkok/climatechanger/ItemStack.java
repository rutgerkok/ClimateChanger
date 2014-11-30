package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.material.MaterialData;
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
     * Gets the raw block data of this item.
     *
     * @return The block data.
     */
    public short getRawBlockDataValue() {
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
    public Material getMaterial(MaterialMap map) throws MaterialNotFoundException {
        if (isBlockIdInStringFormat()) {
            // Try as string
            return map.getByName(tag.getString(BLOCK_ID_TAG));
        } else {
            // Try as number
            return map.getById(tag.getShort(BLOCK_ID_TAG));
        }
    }

    /**
     * Gets the material and data represented as one object.
     * 
     * @param map
     *            The material map used to lookup the name or id.
     * @return The material and data.
     * @throws MaterialNotFoundException
     *             If no block material is present. Usually happens in the case
     *             of item materials.
     * @throws NullPointerException
     *             If the material map is null.
     */
    public MaterialData getMaterialData(MaterialMap map) throws MaterialNotFoundException {
        Material material = getMaterial(map);
        short blockData = getRawBlockDataValue();
        if (blockData < MaterialData.MIN_BLOCK_DATA || blockData > MaterialData.MAX_BLOCK_DATA) {
            throw new MaterialNotFoundException("Block data: " + blockData);
        }
        return MaterialData.of(material, (byte) blockData);
    }

    /**
     * Checks if this block has the given material.
     *
     * @param materialData
     *            The material to check.
     * @return True if the material and data match, false otherwise.
     * @throws NullPointerException
     *             If the given material is null.
     */
    public boolean hasMaterialData(MaterialData materialData) {
        // Check for block id
        if (isBlockIdInStringFormat()) {
            if (!tag.getString(BLOCK_ID_TAG).equals(materialData.getMaterial().getName())) {
                // Mismatched block name
                return false;
            }
        } else {
            if (tag.getShort(BLOCK_ID_TAG) != materialData.getMaterial().getId()) {
                // Mismatched block id
                return false;
            }
        }

        // Check for block data
        return materialData.blockDataMatches(getRawBlockDataValue());
    }

    private boolean isBlockIdInStringFormat() {
        return tag.get("id") instanceof StringTag;
    }

    /**
     * Sets the material of this stack.
     *
     * @param materialData
     *            The material.
     * @throws NullPointerException
     *             If the material is null.
     */
    public void setMaterialData(MaterialData materialData) {
        if (isBlockIdInStringFormat()) {
            tag.putString(BLOCK_ID_TAG, materialData.getMaterial().getName());
        } else {
            tag.putShort(BLOCK_ID_TAG, materialData.getMaterial().getId());
        }
        tag.putShort(BLOCK_DATA_TAG, materialData.getData());
    }
}
