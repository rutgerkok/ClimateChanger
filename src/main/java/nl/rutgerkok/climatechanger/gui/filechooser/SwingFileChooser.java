package nl.rutgerkok.climatechanger.gui.filechooser;

import nl.rutgerkok.hammer.util.Consumer;

import java.nio.file.Path;

import javax.swing.JFileChooser;

/**
 * Fallback file chooser that is always available.
 *
 */
final class SwingFileChooser extends FileChooser {

    @Override
    public void chooseFile(Consumer<Path> callback) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            callback.accept(fileChooser.getSelectedFile().toPath());
        } else {
            callback.accept(null);
        }
    }

}
