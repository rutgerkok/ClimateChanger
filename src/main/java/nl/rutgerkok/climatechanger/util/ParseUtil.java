package nl.rutgerkok.climatechanger.util;

import nl.rutgerkok.climatechanger.material.Material;
import nl.rutgerkok.climatechanger.material.MaterialData;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.material.MaterialSet;

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
    public static MaterialData parseMaterialData(String string, MaterialMap materialMap) throws ParseException {
        int colonPosition = string.lastIndexOf(':');
        if (colonPosition != -1) {
            try {
                // Found delimiter, try to parse second part as block data
                String firstPart = string.substring(0, colonPosition);
                String secondPart = string.substring(colonPosition + 1);

                Material material = parseMaterial(firstPart, materialMap);
                int materialData = parseInt(secondPart, MaterialData.MIN_BLOCK_DATA, MaterialData.MAX_BLOCK_DATA);

                return MaterialData.of(material, (byte) materialData);
            } catch (ParseException e) {
                // Ignore, block below tries again without assuming second
                // part is block data
            }
        }

        // No material data
        Material material = parseMaterial(string, materialMap);
        return MaterialData.ofAnyState(material);
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
    public static MaterialSet parseMaterialSet(String string, MaterialMap materials) throws ParseException {
        MaterialSet materialSet = new MaterialSet();
        String[] split = string.split(",");
        for (String part : split) {
            materialSet.add(parseMaterial(part.trim(), materials));
        }
        if (materialSet.isEmpty()) {
            throw new ParseException("Material list may not be empty", 0);
        }
        return materialSet;
    }
}
