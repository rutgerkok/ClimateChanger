package nl.rutgerkok.climatechanger.gui.menu;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Base class for menus.
 *
 */
abstract class AbstractMenu extends JMenu {

    AbstractMenu(String label) {
        super(label);
    }

    JMenuItem menuItem(String label, ActionListener action) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(action);
        return item;
    }
}
