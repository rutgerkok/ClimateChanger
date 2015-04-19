package nl.rutgerkok.climatechanger.task;

import nl.rutgerkok.hammer.PlayerFile;
import nl.rutgerkok.hammer.util.Result;

/**
 * Converter for the player data, both in the player files and for the
 * "Data/Player" tag in the level.dat (both tags have the same structure).
 *
 */
public interface PlayerDataTask extends Task {

    /**
     * Executes its action on the given player tag.
     *
     * @param playerFile
     *            The player file.
     * @return The result of the task.
     */
    Result convertPlayerFile(PlayerFile playerFile);

}
