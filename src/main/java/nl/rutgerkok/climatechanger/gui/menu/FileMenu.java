package nl.rutgerkok.climatechanger.gui.menu;

import nl.rutgerkok.climatechanger.action.menu.CloseWorldAction;
import nl.rutgerkok.climatechanger.action.menu.QuitAction;
import nl.rutgerkok.climatechanger.action.menu.SelectWorldAction;
import nl.rutgerkok.climatechanger.gui.GuiInformation;

/**
 * The File menu in the main menu bar.
 *
 */
final class FileMenu extends AbstractMenu {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    FileMenu(GuiInformation guiInformation) {
        super("File");

        add(menuItem("Open World...", new SelectWorldAction(guiInformation)));
        add(menuItem("Close World", new CloseWorldAction(guiInformation)));

        addSeparator();
        add(menuItem("Exit", new QuitAction()));
    }


}
