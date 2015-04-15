package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.world.Chunk;
import nl.rutgerkok.climatechanger.world.ChunkFormatConstants;

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
    public Result convertChunk(Chunk chunk) {
        long time = chunk.getTag().getLong(ChunkFormatConstants.CHUNK_INHABITED_TIME_TAG);
        if (time < minimumTicksPlayed) {
            return Result.DELETE;
        }
        return Result.NO_CHANGES;
    }

}
