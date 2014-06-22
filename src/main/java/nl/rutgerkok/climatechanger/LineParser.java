package nl.rutgerkok.climatechanger;

import nl.rutgerkok.climatechanger.task.BlockIdChanger;
import nl.rutgerkok.climatechanger.task.BiomeIdChanger;
import nl.rutgerkok.climatechanger.util.ParseUtil;
import nl.rutgerkok.climatechanger.task.ChunkTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineParser {
    public List<ChunkTask> parse(List<String> args) throws ParseException {
        List<ChunkTask> parsed = new ArrayList<>();
        List<String> currentParts = new ArrayList<>();

        for (String arg : args) {
            if (arg.equalsIgnoreCase("and")) {
                // New section
                if (parsed.isEmpty()) {
                    throw new ParseException("\"and\" on wrong position", 0);
                }
                parsed.add(parseChunkTask(currentParts));
                currentParts.clear();
                continue;
            }

            // Add to current parts
            currentParts.add(arg);
        }
        
        // Add remaining args
        if (currentParts.isEmpty()) {
            // No args at the end, this is an error
            if (args.isEmpty()) {
                // It's because no args were given at all
                throw new ParseException("No actions given", 0);
            } else {
                // It's because the args ended with "and"
                throw new ParseException("\"and\" may not be at the end of the line", 0);
            }
        }
        parsed.add(parseChunkTask(currentParts));

        return parsed;
    }

    public List<String> getActionsHelp() {
        return Arrays.asList(
                "changeBiome <fromId> <toId>",
                "changeBlock <fromId> <fromData> <toId> <toData>"
                );
    }

    private ChunkTask parseChunkTask(List<String> parts) throws ParseException {
        if (parts.isEmpty()) {
            throw new ParseException("No parameters given", 0);
        }
        String taskName = parts.get(0).toLowerCase();
        switch (taskName) {
            case "changebiome":
                assureSize(parts, 3);
                // -1 is used as a wildcard
                int fromId = ParseUtil.parseInt(parts.get(1), -1, Chunk.MAX_BIOME_ID);
                int toId = ParseUtil.parseInt(parts.get(2), 0, Chunk.MAX_BIOME_ID);
                return new BiomeIdChanger((byte) fromId, (byte) toId);
            case "changeblock":
                assureSize(parts, 5);
                int fromBlockId = ParseUtil.parseInt(parts.get(1), -1, Chunk.MAX_BLOCK_ID);
                int fromBlockData = ParseUtil.parseInt(parts.get(2), -1, Chunk.MAX_BLOCK_DATA);
                int toBlockId = ParseUtil.parseInt(parts.get(3), 0, Chunk.MAX_BLOCK_ID);
                int toBlockData = ParseUtil.parseInt(parts.get(4), 0, Chunk.MAX_BLOCK_DATA);
                return new BlockIdChanger((short) fromBlockId, (byte) fromBlockData, (short) toBlockId, (byte) toBlockData);
        }
        throw new ParseException("Unknown task: " + taskName, 0);
    }

    private void assureSize(List<?> args, int minSize) throws ParseException {
        if (args.size() != minSize) {
            throw new ParseException("Expected " + minSize + " args, found " + args.size() + " args.", 0);
        }
    }

}
