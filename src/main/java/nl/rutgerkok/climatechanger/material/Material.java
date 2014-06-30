package nl.rutgerkok.climatechanger.material;

/**
 * Represents one of Minecraft's materials.
 *
 */
public final class Material {
    private final short id;
    private final String name;

    /**
     * Creates a new material.
     *
     * @param name
     *            Name of the material, may not be null.
     * @param id
     *            Id of the material.
     */
    public Material(String name, int id) {
        this.name = name;
        this.id = (short) id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Material && ((Material) other).id == id;
    }

    /**
     * Gets the id of this material.
     *
     * @return The id.
     */
    public short getId() {
        return id;
    }

    /**
     * Gets the name of this material.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
