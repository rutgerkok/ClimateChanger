package nl.rutgerkok.climatechanger.nbt;

/**
 * Represents a generic number tag.
 *
 */
public abstract class NumberTag extends Tag {

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NumberTag)) {
            return false;
        }

        // Check if difference is small enough
        NumberTag other = (NumberTag) obj;
        return Math.abs(other.getDouble() - this.getDouble()) < 0.00001;
    }

    /**
     * Gets the number as byte.
     *
     * @return The number.
     */
    public abstract byte getByte();

    /**
     * Gets the number as double.
     *
     * @return The number.
     */
    public abstract double getDouble();

    /**
     * Gets the number as float.
     *
     * @return The number.
     */
    public abstract float getFloat();

    /**
     * Gets the number as int.
     *
     * @return The number.
     */
    public abstract int getInt();

    public abstract long getLong();

    /**
     * Gets the number as short.
     *
     * @return The number.
     */
    public abstract short getShort();

    @Override
    public final int hashCode() {
        return getInt();
    }
}
