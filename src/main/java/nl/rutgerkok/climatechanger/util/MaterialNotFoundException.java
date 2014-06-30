package nl.rutgerkok.climatechanger.util;

/**
 * Thrown when a material is not found by the material map.
 *
 */
public class MaterialNotFoundException extends Exception {

    /**
     * Constructs a new exception with the given material name.
     * 
     * @param materialName
     *            The material name.
     */
    public MaterialNotFoundException(String materialName) {
        super(materialName);
    }

    /**
     * Constructs a new exception with the given material id.
     * 
     * @param materialId
     *            The material id.
     */
    public MaterialNotFoundException(int materialId) {
        super(Integer.toString(materialId));
    }

    /**
     * Gets the name of the material that was not found.
     * 
     * @return The name.
     */
    public String getMaterial() {
        return getMessage();
    }
}
