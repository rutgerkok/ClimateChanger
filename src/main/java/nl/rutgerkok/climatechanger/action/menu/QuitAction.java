package nl.rutgerkok.climatechanger.action.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action that simply shuts down the application.
 */
public final class QuitAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

}
