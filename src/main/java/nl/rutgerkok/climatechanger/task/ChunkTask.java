package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.util.Result;

public interface ChunkTask extends Task {

    /**
     * Executes its action on the given chunk.
     *
     * @param chunk
     *            The chunk.
     * @return True if the chunk was changed, false otherwise.
     */
    Result convertChunk(AnvilChunk chunk);

}
