package nl.rutgerkok.climatechanger.gui.menu;

import nl.rutgerkok.climatechanger.gui.GuiInformation;

import java.util.Objects;

import javax.swing.JMenuBar;

/**
 * The main menu bar of the application.
 *
 */
public final class MenuBar extends JMenuBar {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    public MenuBar(GuiInformation applicationState) {
        Objects.requireNonNull(applicationState);

        this.add(new FileMenu(applicationState));
    }
}
