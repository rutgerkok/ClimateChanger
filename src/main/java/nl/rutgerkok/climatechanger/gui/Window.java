package nl.rutgerkok.climatechanger.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Window {

    public Window() {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create window
        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 150);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();

        // Field for directory name
        FileChooserPanel fileChooserPanel = new FileChooserPanel("Region directory:", "");
        mainPanel.add(fileChooserPanel);
        
        // Field for ids
        IdChooserPanel idChooserPanel = new IdChooserPanel();
        mainPanel.add(idChooserPanel);
        
        // Add main panel to window
        jFrame.add(mainPanel);
        
        // Loading bar and start button
        JPanel startButtonPanel = new JPanel();
        ProgressPanel progressBar = new ProgressPanel();
        startButtonPanel.add(new StartButton("Convert biome ids", fileChooserPanel, idChooserPanel, progressBar));
        startButtonPanel.add(progressBar);
        jFrame.add(startButtonPanel, BorderLayout.SOUTH);

        jFrame.setVisible(true);
    }
}
