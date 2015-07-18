package nl.rutgerkok.climatechanger.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Label with a text field below. Clicking on the label will focus the text
 * field.
 */
public class LabelWithField extends JPanel {
    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    private static final int TEXT_FIELD_HEIGHT = 22;
    private static final int TEXT_FIELD_WIDTH = 250;

    private final JTextField textField;

    public LabelWithField(String label) {
        this(label, "");
    }

    public LabelWithField(String label, String defaultValue) {
        // Two rows, one column
        setLayout(new GridLayout(2, 1));

        // Remove background
        setBackground(new Color(0, 0, 0, 0));

        // Label
        JLabel jLabel = new JLabel(label);
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.requestFocusInWindow();
            }
        });
        add(jLabel);

        // Field
        textField = new JTextField(defaultValue);
        textField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));
        add(textField);
    }

    /**
     * Gets the text in the text field.
     *
     * @return The text.
     */
    public String getText() {
        return textField.getText();
    }
}