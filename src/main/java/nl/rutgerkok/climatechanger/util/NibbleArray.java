package nl.rutgerkok.climatechanger.util;

import java.util.Objects;

/**
 * An array of 4-bit values, called nibbles. Two nibbles are stored in one byte.
 *
 * The order in which the nibbles are stored is rather interesting: instead of
 * doing it like this:
 *
 * <pre>
 * byte nr:    --1--  --2--  --3--
 * nibble nr:  1---2  3---4  5---6
 * </pre>
 *
 * <p>
 * it is stored like this:
 *
 * <pre>
 * byte nr:    --1--  --2--  --3--
 * nibble nr:  2---1  4---3  6---5
 * </pre>
 *
 * This has been done to remain compatible with Minecraft
 */
public class NibbleArray {
    private final byte[] bytes;
    private final int length;

    /**
     * Creates a new nibble array.
     *
     * @param bytes
     *            The bytes, used as storage.
     */
    public NibbleArray(byte[] bytes) {
        this.bytes = Objects.requireNonNull(bytes);
        this.length = bytes.length * 2;
    }

    public NibbleArray(int length) {
        this.bytes = new byte[(length + 1) / 2];
        this.length = length;
    }

    /**
     * Gets the byte at the given position. Only the lowest four bits of the
     * byte will be used.
     *
     * @param position
     *            The position.
     * @return The byte.
     * @throws ArrayIndexOutOfBoundsException
     *             If {@code position < 0 || position >= length()}.
     */
    public byte get(int position) throws ArrayIndexOutOfBoundsException {
        // Check for negative positions, because (int) (-1 / 2) == 0, so it
        // would otherwise validate silently
        if (position < 0 || position >= length) {
            throw new ArrayIndexOutOfBoundsException("Position is " + position + ", array size is " + length);
        }

        int arrayPos = position / 2;
        boolean getOnLeftFourBits = (position % 2) != 0;
        if (getOnLeftFourBits) {
            return (byte) ((bytes[arrayPos] >> 4) & 0b0000__1111);
        } else {
            return (byte) (bytes[arrayPos] & 0b0000__1111);
        }
    }

    /**
     * Gets the underlying byte array.
     *
     * @return The underlying byte array.
     */
    public byte[] getHandle() {
        return bytes;
    }

    /**
     * Gets how many 4-bit values will fit into this array.
     *
     * @return The length.
     */
    public int length() {
        return length;
    }

    /**
     * Sets the byte at the given index to the contents of the given byte. The
     * highest four bits of the byte will silently be discarded.
     *
     * @param position
     *            The position.
     * @param value
     *            The new value.
     * @throws ArrayIndexOutOfBoundsException
     *             If {@code position < 0 || position > length()}.
     */
    public void set(int position, byte value) throws ArrayIndexOutOfBoundsException {
        // Check for negative positions, because (int) (-1 / 2) == 0, so it
        // would otherwise validate silently
        if (position < 0 || position >= length) {
            throw new ArrayIndexOutOfBoundsException("Position is " + position + ", array size is " + length);
        }

        int arrayPos = position / 2;
        boolean setOnLeftFourBits = (position % 2) != 0;

        byte previous = bytes[arrayPos];
        if (setOnLeftFourBits) {
            bytes[arrayPos] = (byte) ((previous & 0b0000__1111) | ((value << 4) & 0b1111__0000));
        } else {
            bytes[arrayPos] = (byte) ((previous & 0b1111__0000) | (value & 0b0000__1111));
        }
    }
}
