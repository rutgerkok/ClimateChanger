package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.Chunk;

public interface ChunkTask extends Task {

    /**
     * Executes its action on the given chunk.
     *
     * @param chunk
     *            The chunk.
     * @return True if the chunk was changed, false otherwise.
     */
    boolean convertChunk(Chunk chunk);

}
