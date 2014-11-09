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
     * Creates a new material data of the given material and data.
     *
     * @param material
     *            The material.
     * @param data
     *            The data.
     * @return The material and data.
     * @throws NullPointerExcepion
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
     *
     * @param material
     *            The material.
     * @return The material data.
     * @throws NullPointerExcepion
     *             If the material is null.
     */
    public static MaterialData ofMaterial(Material material) {
        return new MaterialData(material, MIN_BLOCK_DATA);
    }

    private final byte data;

    private final Material material;

    private MaterialData(Material material, byte data) {
        this.material = Objects.requireNonNull(material);
        this.data = data;
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
     * Gets the block data value.
     *
     * @return The block data value.
     */
    public byte getData() {
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

    @Override
    public String toString() {
        if (data == 0) {
            return material.getName();
        }
        return material.getName() + ":" + data;
    }
}
