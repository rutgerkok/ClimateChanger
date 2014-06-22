package nl.rutgerkok.climatechanger.util;

import java.text.ParseException;

public final class ParseUtil {
    /**
     * Parses a bounded int.
     *
     * @param string
     *            The string to parse.
     * @param min
     *            Minimum value of the int, inclusive.
     * @param max
     *            Maximum value of the int, inclusive.
     * @return The int.
     * @throws ParseException
     *             If the string could not be parsed, or if the parsed int falls
     *             outside the bounds.
     */
    public static final int parseInt(String string, int min, int max) throws ParseException {
        try {
            int value = Integer.parseInt(string);
            if (value < min) {
                throw new ParseException(value + " is lower than the min allowed value, " + min, 0);
            }
            if (value > max) {
                throw new ParseException(value + " is higher than the max allowed value, " + max, 0);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid number: " + string, 0);
        }
    }
}
