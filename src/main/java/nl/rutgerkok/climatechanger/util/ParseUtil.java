package nl.rutgerkok.climatechanger.util;

import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.material.MaterialMap;

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

    /**
     * Parses a material.
     * 
     * @param string
     *            The string to parse.
     * @param materials
     *            The material map.
     * @return The material.
     * @throws ParseException
     *             If the given string is not a valid material.
     */
    public static Material parseMaterial(String string, MaterialMap materials) throws ParseException {
        try {
            return materials.getByNameOrId(string);
        } catch (MaterialNotFoundException e) {
            throw new ParseException(string + " is not a valid material name or block id", 0);
        }
    }
}
