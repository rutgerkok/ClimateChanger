package nl.rutgerkok.climatechanger.gui;

import nl.rutgerkok.climatechanger.Startup;
import nl.rutgerkok.climatechanger.gui.menu.MenuBar;
import nl.rutgerkok.climatechanger.gui.task.GridBagRules;
import nl.rutgerkok.climatechanger.gui.task.GridBagRules.ResizeBehavior;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
        jFrame.setSize(900, 700);
        jFrame.setMinimumSize(new Dimension(490, 220));
        jFrame.setTitle(Startup.NAME);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        MainArea mainArea = new MainArea(information);
        jFrame.add(mainArea, BorderLayout.CENTER);

        // Menu bar
        jFrame.setJMenuBar(new MenuBar(information));

        // Loading bar and start button
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setLayout(new GridBagLayout());
        startButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 2, 10));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(100, 25));
        JLabel progressField = new JLabel();
        ProgressPanel progressPanel = new ProgressPanel(progressBar, progressField);
        StartButton startButton = new StartButton("Execute tasks", information, progressPanel);

        startButtonPanel.add(startButton, GridBagRules.cellPos(0, 0).resizeBehavior(ResizeBehavior.DONT_GROW));
        startButtonPanel.add(progressBar, GridBagRules.cellPos(1, 0).resizeBehavior(ResizeBehavior.GROW_HORIZONTAL));
        startButtonPanel.add(progressField, GridBagRules.cellPos(2, 0).resizeBehavior(ResizeBehavior.DONT_GROW));

        jFrame.add(startButtonPanel, BorderLayout.SOUTH);

        jFrame.setVisible(true);
    }
}
