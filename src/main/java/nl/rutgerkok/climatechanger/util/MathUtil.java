package nl.rutgerkok.climatechanger.util;

/**
 * Methods for performing mathematicial calculations.
 *
 */
public final class MathUtil {
    private static float[] sinCache = new float[65536];
    static {
        for (int i = 0; i < 65536; i++) {
            sinCache[i] = (float) Math.sin(i * Math.PI * 2.0 / sinCache.length);
        }
    }

    public static float cos(float radians) {
        return sinCache[((int) (radians * 10430.378F + 16384.0F) & 0xFFFF)];
    }

    /**
     * Returns the largest (closest to positive infinity) double value that is
     * less than or equal to the argument and is equal to a mathematical
     * integer. So 2.999 -> 2, 0.123 -> 0, -0.14235 -> -1.
     * <p>
     * Special cases:
     *
     * <ul>
     * <li>If the argument value is already equal to a mathematical integer,
     * then the result is the same as the argument.</li>
     * <li>If the argument is NaN or an infinity or positive zero or negative
     * zero, then the result is the same as the argument.</li>
     * </ul>
     *
     * @param num
     *            The number to floor.
     * @return The floored number.
     */
    public static int floor(double num) {
        if (num >= 0) {
            // 10.1 -> 10
            // 10.0 -> 10
            // 9.9 -> 9
            return (int) num;
        } else {
            // Subtracting 0.999999 is more reliable than subtracting 1:
            // -3.1 -> -4.099999 -> -4
            // -3.0 -> -3.999999 -> -3
            // -2.9 -> -3.899999 -> -3
            return (int) (num - 0.999999);
        }
    }

    public static float sin(float radians) {
        return sinCache[((int) (radians * 10430.378F) & 0xFFFF)];
    }

    private MathUtil() {
        // No instances
    }
}
