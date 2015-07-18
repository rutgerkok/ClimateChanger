package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.Window;
import nl.rutgerkok.climatechanger.task.SignFixer;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class SignFixerPanel extends TaskPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    private final JTextArea textArea;

    public SignFixerPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        add(new JLabel("<html>Minecraft 1.8 doesn't import signs from 1.7 correctly:"
                + " brackets will be missing."
                + " <p>It is almost always possible to detect whether a sign is"
                + " (A) in the 1.7 format, (B) created in the 1.8 format or (C) now in the 1.8"
                + " format, but used to be in the 1.7 format. Signs in (A) will be converted"
                + " correctly by this program and signs in (B) don't need this program."
                + " <p>For signs in (C) this program needs a little help. In the text field"
                + " below put on each line a value that needs to be enclosed by [ and ]. By default,"
                + " 'Private' has been placed in the text area, so that when a line on a sign"
                + " of type (C) reads 'Private' (case insensitive) it will be replaced with"
                + " '[Private]'."), BorderLayout.NORTH);

        textArea = new JTextArea("Private\nMore Users\nEveryone");
        textArea.setBorder(Window.SIMPLE_BORDER);
        add(textArea, BorderLayout.CENTER);
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        String[] stringsNeedingBrackets = textArea.getText().split("\n");
        return new SignFixer(stringsNeedingBrackets);
    }
}
