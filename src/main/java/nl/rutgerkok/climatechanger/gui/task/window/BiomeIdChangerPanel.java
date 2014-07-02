package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.task.BiomeIdChanger;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;

import java.awt.FlowLayout;

public class BiomeIdChangerPanel extends TaskPanel {
    private final LabelWithField idFrom;
    private final LabelWithField idTo;

    public BiomeIdChangerPanel(MaterialMap map) {
        super(map);

        // Align right
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // From
        add(idFrom = new LabelWithField("Original biome id: (use -1 as wildcard)"));

        // To
        add(idTo = new LabelWithField("New biome id:"));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        try {
            return new BiomeIdChanger((byte) Short.parseShort(idFrom.getText()),
                    (byte) Short.parseShort(idTo.getText()));
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("Invalid id given");
        }
    }
}
