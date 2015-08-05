package nl.rutgerkok.climatechanger.util;

import nl.rutgerkok.hammer.material.GlobalMaterialMap;
import nl.rutgerkok.hammer.material.MaterialData;
import nl.rutgerkok.hammer.material.MaterialSet;
import nl.rutgerkok.hammer.util.MaterialNotFoundException;

import java.text.ParseException;

public final class ParseUtil {
    /**
     * Parses a bounded double.
     *
     * @param string
     *            The string to parse.
     * @param min
     *            Minimum value of the double, inclusive.
     * @param max
     *            Maximum value of the double, inclusive.
     * @return The double.
     * @throws ParseException
     *             If the string could not be parsed, or if the parsed double
     *             falls outside the bounds.
     */
    public static double parseDouble(String string, double min, double max) throws ParseException {
        try {
            double value = Double.parseDouble(string);
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
        int value = parseIntUnbounded(string);
        if (value < min) {
            throw new ParseException(value + " is lower than the min allowed value, " + min, 0);
        }
        if (value > max) {
            throw new ParseException(value + " is higher than the max allowed value, " + max, 0);
        }
        return value;
    }

    private static final int parseIntUnbounded(String string) throws ParseException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid number: " + string, 0);
        }
    }

    /**
     * Parses material data.
     *
     * @param string
     *            Can be in the form "material", "namespace:material",
     *            "material:data", "namespace:material:data".
     * @param materialMap
     *            The material map, for looking up materials.
     * @return The material data.
     * @throws ParseException
     *             If the material could not be parsed.
     */
    public static MaterialData parseMaterialData(String string, GlobalMaterialMap materialMap)
            throws ParseException {
        try {
            return materialMap.getMaterialByName(string);
        } catch (MaterialNotFoundException e) {
            throw new ParseException(e.getLocalizedMessage(), 0);
        }
    }

    /**
     * Parses a comma-separated set of materials.
     *
     * @param string
     *            The string to parse.
     * @param materials
     *            The material map.
     * @return The set of materials.
     * @throws ParseException
     *             If no materials are given or if the set is empty.
     */
    public static MaterialSet parseMaterialSet(String string, GlobalMaterialMap materials)
            throws ParseException {
        MaterialSet materialSet = new MaterialSet();
        String[] split = string.split(",");
        for (String part : split) {
            materialSet.add(parseMaterialData(part.trim(), materials));
        }
        if (materialSet.isEmpty()) {
            throw new ParseException("Material list may not be empty", 0);
        }
        return materialSet;
    }
}
