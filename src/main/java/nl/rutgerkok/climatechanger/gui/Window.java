package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.Startup;
import nl.rutgerkok.climatechanger.gui.task.TaskPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class Window {

    public static final Border SIMPLE_BORDER = BorderFactory.createLineBorder(new Color(172, 172, 172));

    public Window(GuiInformation information) {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create window
        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 350);
        jFrame.setMinimumSize(new Dimension(490, 220));
        jFrame.setTitle(Startup.NAME);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        jFrame.add(new TaskPanel(information), BorderLayout.CENTER);

        // Loading bar and start button
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setLayout(new BoxLayout(startButtonPanel, BoxLayout.X_AXIS));
        startButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 2, 10));
        ProgressPanel progressBar = new ProgressPanel();
        startButtonPanel.add(new StartButton("Execute tasks", information, progressBar));
        startButtonPanel.add(progressBar);
        jFrame.add(startButtonPanel, BorderLayout.SOUTH);

        jFrame.setVisible(true);
    }
}
