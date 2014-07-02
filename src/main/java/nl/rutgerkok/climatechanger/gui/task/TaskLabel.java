package nl.rutgerkok.climatechanger.gui.task;

import nl.rutgerkok.climatechanger.gui.GuiInformation;
import nl.rutgerkok.climatechanger.task.Task;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class TaskLabel extends JLabel {

    private Color color = Color.BLACK;
    private final GuiInformation information;
    private final Task task;

    public TaskLabel(final Task task, GuiInformation information) {
        super("• " + task.getDescription());

        this.task = task;
        this.information = information;

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                setSelected(!isSelected());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setColor(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setColor(Color.BLACK);
            }
        });
    }

    /**
     * Gets the task that this label represents.
     *
     * @return The task.
     */
    public Task getTask() {
        return task;
    }

    /**
     * Gets whether this option is currently selected.
     *
     * @return True if selected, false otherwise.
     */
    public boolean isSelected() {
        return information.isSelected(task);
    }

    private void setColor(Color color) {
        this.color = color;
        updateText();
    }

    /**
     * Sets the selected state of this task.
     *
     * @param selected
     *            The selected state.
     */
    public void setSelected(boolean selected) {
        if (selected == isSelected()) {
            // Already in correct state, do nothing.
            return;
        }

        // This will cause an event that redraws everything
        if (selected) {
            information.markSelected(task);
        } else {
            information.markUnselected(task);
        }
    }

    /**
     * Call this when the selection state is updated. Normally called when the
     * color is changed or when the model changes.
     */
    public void updateText() {
        String hexColor = Integer.toHexString(color.getRGB() & 0xFFFFFF);
        String fontWeight = isSelected() ? "bold" : "normal";
        setText("<html><span style=\"color:#" + hexColor + ";font-weight:" + fontWeight + "\">• "
                + task.getDescription() + "</span></html>");
        revalidate();
    }

}
