package nl.rutgerkok.climatechanger.gui.task.window;

import java.awt.FlowLayout;
import java.text.ParseException;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.task.BlockIdChanger;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.ParseUtil;
import nl.rutgerkok.hammer.material.GlobalMaterialMap;

public class BlockIdChangerPanel extends TaskPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    private final LabelWithField from;
    private final GlobalMaterialMap materialMap;

    private final LabelWithField to;

    public BlockIdChangerPanel(GlobalMaterialMap map) {
        this.materialMap = map;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        // From
        add(from = new LabelWithField("Original namespaced id:"));

        // To
        add(to = new LabelWithField("New namespaced id:"));
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
