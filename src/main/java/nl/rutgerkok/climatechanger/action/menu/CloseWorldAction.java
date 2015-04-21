package nl.rutgerkok.climatechanger.action.menu;

import nl.rutgerkok.climatechanger.gui.GuiInformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Clears the currently selected world.
 *
 */
public final class CloseWorldAction implements ActionListener {

    private final GuiInformation guiInformation;

    public CloseWorldAction(GuiInformation guiInformation) {
        this.guiInformation = Objects.requireNonNull(guiInformation, "guiInformation");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        guiInformation.setWorld(null);
    }

}
