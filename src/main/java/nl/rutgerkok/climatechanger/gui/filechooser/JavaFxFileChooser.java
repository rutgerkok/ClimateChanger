package nl.rutgerkok.climatechanger.gui.filechooser;

import nl.rutgerkok.hammer.util.Consumer;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;

import javax.swing.SwingUtilities;

/**
 * Uses JavaFx so that the modern, native file chooser is opened. Early Java 7
 * builds did not ship with JavaFx, so JavaFx cannot be treated as a compile
 * time or runtime dependency. To get around that, everything touching JavaFx in
 * this class uses reflection.
 *
 */
class JavaFxFileChooser extends FileChooser {

    private final Class<?> fileChooserClass;
    private final Class<?> platformClass;
    private final Method runLaterMethod;
    private final Method showOpenDialogMethod;
    private final Class<?> windowClass;

    JavaFxFileChooser() throws ReflectiveOperationException {

        // Initialize JavaFx
        Class.forName("javafx.embed.swing.JFXPanel").newInstance();

        // Reflection classes
        platformClass = Class.forName("javafx.application.Platform");
        runLaterMethod = platformClass.getMethod("runLater", Runnable.class);
        fileChooserClass = Class.forName("javafx.stage.FileChooser");
        windowClass = Class.forName("javafx.stage.Window");
        showOpenDialogMethod = fileChooserClass.getMethod("showOpenDialog", windowClass);
    }

    private void handleFileChooser(final Consumer<Path> callback) {
        final Path path = openFileChooser();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                callback.accept(path);
            }

        });
    }

    /**
     * Opens the file chooser. Must be called on the JavaFx thread.
     *
     * @return The path that was selected.
     */
    private Path openFileChooser() {
        try {
            Object fileChooser = fileChooserClass.newInstance();
            File file = (File) showOpenDialogMethod.invoke(fileChooser, (Object) null);
            return file == null ? null : file.toPath();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseFile(final Consumer<Path> callback) {
        try {
            runLaterMethod.invoke(null, new Runnable() {
                @Override
                public void run() {
                    handleFileChooser(callback);
                }
            });
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
