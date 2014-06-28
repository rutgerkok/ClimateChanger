package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.Chunk;

/**
 * Acts on a single chunk.
 *
 */
public interface ChunkTask {
    /**
     * Executes its action on the given chunk.
     *
     * @param chunk
     *            The chunk.
     * @return True if the chunk was changed, false otherwise.
     */
    boolean execute(Chunk chunk);
    
    /**
     * Gets a description of this task.
     * @return A description.
     */
    String getDescription();
}
