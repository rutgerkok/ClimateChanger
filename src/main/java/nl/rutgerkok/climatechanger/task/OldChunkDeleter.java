package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.anvil.tag.AnvilFormat.ChunkTag;
import nl.rutgerkok.hammer.util.Result;

public class OldChunkDeleter implements ChunkTask {

    private final int minimumMinutesPlayed;
    private final long minimumTicksPlayed;

    public OldChunkDeleter(int minutesPlayed) {
        this.minimumMinutesPlayed = minutesPlayed;
        this.minimumTicksPlayed = minutesPlayed * 60 * 20;
    }

    @Override
    public String getDescription() {
        return "Delete chunks that have been loaded less than " + minimumMinutesPlayed + " minutes.";
    }

    @Override
    public Result convertChunk(AnvilChunk chunk) {
        long time = chunk.getTag().getLong(ChunkTag.INHABITED_TIME);
        if (time < minimumTicksPlayed) {
            return Result.DELETE;
        }
        return Result.NO_CHANGES;
    }

}
