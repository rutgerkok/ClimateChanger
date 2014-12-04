package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.world.Chunk;

public interface ChunkTask extends Task {

    /**
     * The result of running this task.
     *
     */
    public enum Result {
        /**
         * No changes were made, no need to save the chunk.
         */
        NO_CHANGES {
            @Override
            public Result getCombined(Result update) {
                return update;
            }
        },
        /**
         * Changes were made, chunk needs to be saved.
         */
        CHANGED {
            @Override
            public Result getCombined(Result update) {
                if (update == Result.DELETE) {
                    return Result.DELETE;
                }
                return this;
            }
        },
        /**
         * Chunk needs to be deleted.
         */
        DELETE {
            @Override
            public Result getCombined(Result update) {
                return this;
            }
        };

        /**
         * Returns a new result that is a combination of this result and the
         * given result. For example, {@link #NO_CHANGES} and {@link #CHANGED}
         * combine to {@link #CHANGED}. {@link #CHANGED} and {@link #DELETE}
         * combine to {@link #DELETE}.
         * 
         * @param update
         *            The result to combine with this result.
         * @return The combined result.
         */
        public abstract Result getCombined(Result update);
    }

    /**
     * Executes its action on the given chunk.
     *
     * @param chunk
     *            The chunk.
     * @return True if the chunk was changed, false otherwise.
     */
    Result convertChunk(Chunk chunk);

}
