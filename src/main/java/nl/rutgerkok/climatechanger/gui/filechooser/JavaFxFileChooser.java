package nl.rutgerkok.climatechanger.gui.filechooser;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;

import javax.swing.SwingUtilities;

/**
 * Uses JavaFx so that the modern, native file chooser is opened. Early Java 7
 * builds did not ship with JavaFx, so JavaFx cannot be treated as a compile
 * time or runtime dependency. To get around that, everything touching JavaFx
 * in this class uses reflection.
 *
 */
class JavaFxFileChooser extends FileChooserPanel {

    private final Class<?> platformClass;
    private final Method runLaterMethod;
    private final Class<?> fileChooserClass;
    private final Class<?> windowClass;
    private final Method showOpenDialogMethod;

    JavaFxFileChooser(String label) throws ReflectiveOperationException {
        super(label);

        // Initialize JavaFx
        Class.forName("javafx.embed.swing.JFXPanel").newInstance();

        // Reflection classes
        platformClass = Class.forName("javafx.application.Platform");
        runLaterMethod = platformClass.getMethod("runLater", Runnable.class);
        fileChooserClass = Class.forName("javafx.stage.FileChooser");
        windowClass = Class.forName("javafx.stage.Window");
        showOpenDialogMethod = fileChooserClass.getMethod("showOpenDialog", windowClass);
    }

    /**
     * Opens the file chooser. Must be called on the JavaFx thread.
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

    private void handleFileChooser() {
        final Path path = openFileChooser();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                textUpdated(path == null ? "" : path.toString());
            }

        });
    }

    /**
     * Called when the browse button is clicked. Opens the file chooser. Can be
     * called from any thread.
     */
    protected void onBrowseClick() {
        try {
            runLaterMethod.invoke(null, new Runnable() {
                @Override
                public void run() {
                    handleFileChooser();
                }
            });
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
