package nl.rutgerkok.climatechanger.gui.filechooser;

import java.nio.file.Path;

import javax.swing.JFileChooser;

/**
 * Fallback file chooser that is always available.
 *
 */
class SwingFileChooser extends FileChooserPanel {


    SwingFileChooser(String label) {
        super(label);
    }

    /**
     * Opens a file chooser. Returns the selected file. Returns null when no
     * file was selected.
     *
     * @return The file, or null if nothing was selected.
     */
    private Path chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        } else {
            return null;
        }
    }

    @Override
    protected void onBrowseClick() {
        Path opened = chooseFile();
        if (opened == null) {
            // Cancelled
            return;
        }

        textUpdated(opened.toString());
    }

}
