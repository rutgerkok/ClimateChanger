package nl.rutgerkok.climatechanger.gui.filechooser;

public class FileChoosers {

    /**
     * Creates a panel with a label, a text field and a browse button. The user
     * is able to modify the text field directly, but can also use the Browse
     * button.
     *
     * @param label
     *            The text for the label.
     * @return The panel.
     */
    public static FileChooserPanel createPanel(String label) {
        try {
            return new JavaFxFileChooser(label);
        } catch (ReflectiveOperationException e) {
            return new SwingFileChooser(label);
        }
    }
}
