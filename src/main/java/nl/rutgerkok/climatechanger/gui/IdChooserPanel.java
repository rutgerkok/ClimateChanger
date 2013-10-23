package nl.rutgerkok.climatechanger.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IdChooserPanel extends JPanel {
    private final JTextField idFrom;
    private final JTextField idTo;

    public IdChooserPanel() {
        // Align right
        setLayout(new FlowLayout(FlowLayout.RIGHT));

        // From
        add(new JLabel("Original id"));
        this.idFrom = new JTextField("");
        this.idFrom.setPreferredSize(new Dimension(25, 22));
        add(this.idFrom);

        // To
        add(new JLabel("New id"));
        this.idTo = new JTextField("");
        this.idTo.setPreferredSize(new Dimension(25, 22));
        add(this.idTo);
    }
    

    public String getFrom() {
        return this.idFrom.getText();
    }
    
    public String getTo() {
        return this.idTo.getText();
    }
}
