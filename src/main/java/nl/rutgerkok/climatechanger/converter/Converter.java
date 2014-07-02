package nl.rutgerkok.climatechanger.converter;

import nl.rutgerkok.climatechanger.ProgressUpdater;

import java.io.IOException;

/**
 * Represents a converter. Multiple converters can be chained together.
 *
 * <p>
 * Each converter converts one or more units (region files, inventories, etc.).
 * At the beginning of the conversion {@link #getUnitsToConvert()} is called.
 * For each converted unit, the converter must call
 * {@link ProgressUpdater#incrementProgress()}, incrementing the progress bar.
 */
public interface Converter {

    /**
     * Attempts to convert everything.
     *
     * @param updater
     *            Call {@link ProgressUpdater#incrementProgress()} after a
     *            single unit is converted.
     */
    void convert(ProgressUpdater updater) throws IOException;

    /**
     * Gets the amount of units that need to be converted.
     *
     * @return The amount of units.
     * @throws IOException
     *             When an IO error occurs.
     */
    int getUnitsToConvert() throws IOException;
}
