package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.anvil.tag.AnvilFormat.OldChunkTag;
import nl.rutgerkok.hammer.tag.CompoundTag;
import nl.rutgerkok.hammer.util.Result;

public class LightPopulatedSetter implements ChunkTask {

    @Override
    public Result convertChunk(AnvilChunk chunk) {
        CompoundTag tag = chunk.getTag();
        if (!tag.getBoolean(OldChunkTag.LIGHT_POPULATED)) {
            chunk.getTag().setBoolean(OldChunkTag.LIGHT_POPULATED, true);
            return Result.CHANGED;
        }
        return Result.NO_CHANGES;
    }

    @Override
    public String getDescription() {
        return "Sets LightPopulated=true for all chunks in the world.";
    }

}
