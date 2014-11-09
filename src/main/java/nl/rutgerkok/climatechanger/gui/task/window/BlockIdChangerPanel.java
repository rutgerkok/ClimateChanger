package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.task.BlockIdChanger;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.MaterialNotFoundException;

import java.awt.FlowLayout;

public class BlockIdChangerPanel extends TaskPanel {
    private final LabelWithField dataFrom;

    private final LabelWithField dataTo;
    private final LabelWithField idFrom;
    private final LabelWithField idTo;

    private final MaterialMap materialMap;

    public BlockIdChangerPanel(MaterialMap map) {
        this.materialMap = map;

        // Everyting below other elements
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // From
        add(idFrom = new LabelWithField("Original id or name:"));
        add(dataFrom = new LabelWithField("Original data: (use -1 as wildcard)"));

        // To
        add(idTo = new LabelWithField("New id or name:"));
        add(dataTo = new LabelWithField("New data:"));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        try {
            return new BlockIdChanger(materialMap.getByNameOrId(idFrom.getText()),
                    (byte) Short.parseShort(dataFrom.getText()),
                    materialMap.getByNameOrId(idTo.getText()),
                    (byte) Short.parseShort(dataTo.getText()));
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("Invalid data value given");
        } catch (MaterialNotFoundException e) {
            throw new InvalidTaskException("Invalid material name or id given: " + e.getMaterial());
        }
    }
}
