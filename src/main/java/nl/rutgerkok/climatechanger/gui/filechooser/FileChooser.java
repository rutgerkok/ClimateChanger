package nl.rutgerkok.climatechanger.gui.filechooser;

import nl.rutgerkok.hammer.util.Consumer;

import java.nio.file.Path;

/**
 * A simple file chooser. Backed by a Java Swing file chooser, or a native file
 * chooser if available.
 *
 */
public abstract class FileChooser {

    public static FileChooser create() {
        try {
            return new JavaFxFileChooser();
        } catch (ReflectiveOperationException e) {
            return new SwingFileChooser();
        }
    }

    FileChooser() {

    }

    /**
     * Opens a file chooser. Returns the selected file. Returns null when no
     * file was selected.
     *
     * @param callback
     *            Called with the file, or with null if nothing was selected.
     */
    public abstract void chooseFile(Consumer<Path> callback);

}
