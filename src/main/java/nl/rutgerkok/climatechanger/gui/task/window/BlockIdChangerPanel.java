package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.task.BlockIdChanger;
import nl.rutgerkok.climatechanger.task.ChunkTask;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;

import java.awt.FlowLayout;

public class BlockIdChangerPanel extends TaskPanel {
    private final LabelWithField dataFrom;
    private final LabelWithField dataTo;
    private final LabelWithField idFrom;

    private final LabelWithField idTo;

    public BlockIdChangerPanel() {
        // Everyting below other elements
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // From
        add(idFrom = new LabelWithField("Original id: (use -1 as wildcard)"));
        add(dataFrom = new LabelWithField("Original data: (use -1 as wildcard)"));

        // To
        add(idTo = new LabelWithField("New id:"));
        add(dataTo = new LabelWithField("New data:"));
    }

    @Override
    public ChunkTask getTask() throws InvalidTaskException {
        try {
            return new BlockIdChanger(Short.parseShort(idFrom.getText()),
                    (byte) Short.parseShort(dataFrom.getText()),
                    Short.parseShort(idTo.getText()),
                    (byte) Short.parseShort(dataTo.getText()));
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("Invalid id given");
        }
    }
}
