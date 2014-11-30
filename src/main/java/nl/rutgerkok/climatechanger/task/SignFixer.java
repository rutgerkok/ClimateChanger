package nl.rutgerkok.climatechanger.task;

import static nl.rutgerkok.climatechanger.world.ChunkFormatConstants.TILE_ENTITY_ID_TAG;

import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.world.Chunk;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONValue;

/**
 * Converts 1.7 signs correctly to the 1.8 format; attempts to fix incorrectly
 * imported signs.
 *
 */
public class SignFixer implements ChunkTask {

    private static final String[] LINE_NAMES = { "Text1", "Text2", "Text3", "Text4" };

    private final Set<String> fixBrackets;

    /**
     * Creates a sign fixer.
     * 
     * @param linesNeedingBrackets
     *            When a string in this array is found on a sign that has been
     *            converted by Minecraft, it is enclosed in brackets. Case
     *            insensitive and lines with only whitespace are ignored.
     */
    public SignFixer(String... linesNeedingBrackets) {
        this.fixBrackets = new HashSet<String>();
        for (String lineNeedingBrackets : linesNeedingBrackets) {
            if (lineNeedingBrackets.trim().isEmpty()) {
                // Ignore empty lines
                continue;
            }
            this.fixBrackets.add(lineNeedingBrackets.toLowerCase());
        }
    }

    @Override
    public String getDescription() {
        return "Migrate signs from the 1.7 to the 1.8 format";
    }

    @Override
    public boolean convertChunk(Chunk chunk) {
        boolean changed = false;
        for (CompoundTag tileEntity : chunk.getTileEntities()) {
            if (tileEntity.getString(TILE_ENTITY_ID_TAG).equalsIgnoreCase("Sign")) {
                if (convertSign(tileEntity)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    private boolean convertSign(CompoundTag sign) {
        boolean changed = false;
        for (String lineName : LINE_NAMES) {
            String line = sign.getString(lineName);

            String changedLine = convertLine(line);
            if (line.equals(changedLine)) {
                continue;
            }

            changed = true;
            sign.putString(lineName, changedLine);
        }
        return changed;
    }

    private String convertLine(String line) {
        if (line.equals("null") || line.startsWith("{") || line.equals("\"\"")) {
            // Is JSON-null, is in JSON-map format or is empty
            // Doesn't need conversion
            return line;
        } else if (line.startsWith("\"") && line.endsWith("\"") && line.length() > 2) {
            // JSON-string format (=quoted line), this means sign is converted
            // from the classic format.
            String parsed = String.valueOf(JSONValue.parse(line));

            // Attempt to fix conversion
            if (fixBrackets.contains(parsed.toLowerCase())) {
                parsed = "[" + parsed + "]";
            }

            // Convert to JSON-map format
            return toJsonString(parsed);
        } else {
            // Classic sign, convert to JSON-map format
            return toJsonString(line);
        }
    }

    /**
     * New signs have a really strange format. This method converts a classic
     * line to a modern line in the new, strange format.
     * 
     * @param classicLine
     *            The classic line.
     * @return A string consisting of two double quotes if classicLine is empty,
     *         otherwise the JSON representation of a map containing the
     *         classicLine.
     */
    private String toJsonString(String classicLine) {
        if (classicLine.isEmpty()) {
            return "\"\"";
        }
        Map<String, Object> textComponent = new LinkedHashMap<>(2);
        textComponent.put("extra", Arrays.asList(classicLine));
        textComponent.put("text", "");
        return JSONValue.toJSONString(textComponent);
    }

}
