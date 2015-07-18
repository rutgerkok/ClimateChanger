package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.task.BlockIdChanger;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.ParseUtil;
import nl.rutgerkok.hammer.material.MaterialMap;

import java.awt.FlowLayout;
import java.text.ParseException;

public class BlockIdChangerPanel extends TaskPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    private final LabelWithField from;
    private final LabelWithField to;

    private final MaterialMap materialMap;

    public BlockIdChangerPanel(MaterialMap map) {
        this.materialMap = map;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        // From
        add(from = new LabelWithField("Original id or name:"));

        // To
        add(to = new LabelWithField("New id or name:"));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        try {
            return new BlockIdChanger(ParseUtil.parseMaterialData(from.getText(), materialMap),
                    ParseUtil.parseMaterialData(to.getText(), materialMap));
        } catch (ParseException e) {
            throw new InvalidTaskException("Invalid material name or id given: " + e.getMessage());
        }
    }
}
