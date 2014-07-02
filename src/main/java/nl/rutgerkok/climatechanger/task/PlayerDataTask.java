package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.climatechanger.nbt.CompoundTag;

/**
 * Converter for the player data, both in the player files and for the
 * "Data/Player" tag in the level.dat (both tags have the same structure).
 *
 */
public interface PlayerDataTask extends Task {

    /**
     * Executes its action on the given player tag.
     *
     * @param tag
     *            The tag, with sub tags like Inventory, EnderItems, etc.
     * @return True if the tag was changed, false otherwise.
     */
    boolean convertPlayerFile(CompoundTag tag);

}
