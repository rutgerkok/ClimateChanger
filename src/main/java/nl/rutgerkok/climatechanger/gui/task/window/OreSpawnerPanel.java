package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.task.OreSpawner;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.ParseUtil;
import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.material.GlobalMaterialMap;
import nl.rutgerkok.hammer.material.MaterialData;
import nl.rutgerkok.hammer.material.MaterialSet;

import java.awt.FlowLayout;
import java.text.ParseException;

final class OreSpawnerPanel extends TaskPanel {

    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 0L;

    private final LabelWithField frequencyField;

    private final LabelWithField materialField;
    private final GlobalMaterialMap materialMap;
    private final LabelWithField maxHeightField;
    private final LabelWithField maxSizeField;
    private final LabelWithField minHeightField;
    private final LabelWithField rarityField;
    private final LabelWithField sourceBlocksField;

    OreSpawnerPanel(GlobalMaterialMap materialMap) {
        this.materialMap = materialMap;

        setLayout(new FlowLayout(FlowLayout.LEADING));

        add(materialField = new LabelWithField("Ore material (block:data)"));
        add(maxSizeField = new LabelWithField("Maximum ore radius", "33"));
        add(frequencyField = new LabelWithField("Attempts per chunk", "10"));
        add(rarityField = new LabelWithField("Chance per attempt", "100"));
        add(minHeightField = new LabelWithField("Minimum height", "0"));
        add(maxHeightField = new LabelWithField("Maximum height", "80"));
        add(sourceBlocksField = new LabelWithField("Spawn in (block,block,...)", "stone"));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        try {
            MaterialData material = ParseUtil.parseMaterialData(materialField.getText(), materialMap);
            int maxSize = ParseUtil.parseInt(maxSizeField.getText(), 1, OreSpawner.MAX_ORE_SIZE);
            int frequency = ParseUtil.parseInt(frequencyField.getText(), 1, OreSpawner.MAX_ORE_FREQUENCY);
            double rarity = ParseUtil.parseDouble(rarityField.getText(), 0.0001, 100.0);
            int minHeight = ParseUtil.parseInt(minHeightField.getText(), 0, AnvilChunk.CHUNK_Y_SIZE);
            int maxHeight = ParseUtil.parseInt(maxHeightField.getText(), 0, AnvilChunk.CHUNK_Y_SIZE);
            MaterialSet sourceBlocks = ParseUtil.parseMaterialSet(sourceBlocksField.getText(), materialMap);
            return new OreSpawner(material, maxSize, frequency, rarity, minHeight, maxHeight, sourceBlocks);
        } catch (ParseException e) {
            throw new InvalidTaskException(e.getLocalizedMessage());
        }
    }

}
