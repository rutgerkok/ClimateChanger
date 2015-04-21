package nl.rutgerkok.climatechanger.action.menu;

import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.gui.filechooser.FileChooser;
import nl.rutgerkok.hammer.anvil.AnvilWorld;
import nl.rutgerkok.hammer.util.Consumer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.JOptionPane;

/**
 * Selects a world using a standard file-open dialog.
 *
 */
public final class SelectWorldAction implements ActionListener {

    private final GuiInformation guiInformation;

    public SelectWorldAction(GuiInformation guiInformation) {
        this.guiInformation = Objects.requireNonNull(guiInformation, "guiInformation");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Get file
        FileChooser.create().chooseFile(new Consumer<Path>() {

            @Override
            public void accept(Path path) {
                selectWorld(path);
            }
        });

    }

    private void selectWorld(Path worldFile) {
        if (worldFile == null) {
            return;
        }

        // Get world from file
        AnvilWorld world;
        try {
            world = new AnvilWorld(worldFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Cannot open world", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Set the world
        guiInformation.setWorld(world);
    }

}
