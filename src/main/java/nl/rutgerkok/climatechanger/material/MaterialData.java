package nl.rutgerkok.climatechanger.material;

import java.util.Objects;

/**
 * Represents a Material + Data value as Bukkit calls it, or a Block State as
 * Minecraft 1.8 calls it.
 *
 * <p>
 * Instances of this class are expensive to create on a large scale, so only use
 * it for setting blocks.
 *
 */
public final class MaterialData {

    public static final byte MAX_BLOCK_DATA = 0xf;
    public static final byte MIN_BLOCK_DATA = 0x0;

    /**
     * Internally used to represent the unspecified state, never exposed, falls
     * outside the normal range of block data.
     */
    private static final byte UNSPECIFIED_BLOCK_DATA = -1;

    /**
     * Creates a new material data of the given material and data.
     *
     * @param material
     *            The material.
     * @param data
     *            The data.
     * @return The material and data.
     * @throws NullPointerException
     *             If the material is null.
     * @throws IllegalArgumentException
     *             If the block data is smaller than {@value #MIN_BLOCK_DATA} or
     *             larger than {@value #MAX_BLOCK_DATA}.
     */
    public static MaterialData of(Material material, byte data) {
        if (data < MIN_BLOCK_DATA || data > MAX_BLOCK_DATA) {
            throw new IllegalArgumentException("Block data out of bounds: " + data);
        }
        return new MaterialData(material, data);
    }

    /**
     * Creates a new material data of the default state of the given material.
     * However, {@link #blockDataMatches(short)} will return true for any
     * provided value.
     *
     * @param material
     *            The material.
     * @return The material data.
     * @throws NullPointerException
     *             If the material is null.
     */
    public static MaterialData ofAnyState(Material material) {
        return new MaterialData(material, UNSPECIFIED_BLOCK_DATA);
    }

    private final byte data;
    private final Material material;

    private MaterialData(Material material, byte data) {
        this.material = Objects.requireNonNull(material);
        this.data = data;
    }

    /**
     * Gets if the other block data matches the block data of this material
     * data. It matches if the block data of this material data
     * {@link #isBlockDataUnspecified() is unspecified}, or if the block data
     * has the same number.
     *
     * @param blockData
     *            The other block data.
     * @return True if they match, false otherwise.
     */
    public boolean blockDataMatches(short blockData) {
        return this.data == UNSPECIFIED_BLOCK_DATA
                || this.data == blockData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MaterialData)) {
            return false;
        }
        MaterialData other = (MaterialData) obj;
        if (data != other.data) {
            return false;
        }
        if (!material.equals(other.material)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the block data value. If the block data
     * {@link #isBlockDataUnspecified() is unspecified} this method returns 0.
     *
     * @return The block data value.
     */
    public byte getData() {
        if (data == UNSPECIFIED_BLOCK_DATA) {
            return MIN_BLOCK_DATA;
        }
        return data;
    }

    /**
     * Gets the material part of this MaterialData.
     *
     * @return The material.
     */
    public Material getMaterial() {
        return material;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + data;
        result = prime * result + ((material == null) ? 0 : material.hashCode());
        return result;
    }

    /**
     * Gets whether the block data of this object is a wildcard. If it is, it
     * will {@link #blockDataMatches(short) match} any other block data value.
     * 
     * @return True if this block data is a wildcard, false otherwise.
     */
    public boolean isBlockDataUnspecified() {
        return data == UNSPECIFIED_BLOCK_DATA;
    }

    @Override
    public String toString() {
        if (data == UNSPECIFIED_BLOCK_DATA) {
            return material.getName();
        }
        return material.getName() + ":" + data;
    }

    /**
     * Gets whether the given block name matches the name of
     * {@link #getMaterial() the material part} of this material data. Case
     * sensitive.
     * 
     * @param blockName
     *            Name of the block to check.
     * @return True if the name matches, false otherwise.
     */
    public boolean materialNameEquals(String blockName) {
        return material.getName().equals(blockName);
    }
}
