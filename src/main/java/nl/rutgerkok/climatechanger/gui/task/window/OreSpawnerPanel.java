package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.Chunk;
import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.material.MaterialData;
import nl.rutgerkok.climatechanger.material.MaterialMap;
import nl.rutgerkok.climatechanger.material.MaterialSet;
import nl.rutgerkok.climatechanger.task.OreSpawner;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.ParseUtil;

import java.awt.FlowLayout;
import java.text.ParseException;

final class OreSpawnerPanel extends TaskPanel {

    private final LabelWithField frequencyField;

    private final LabelWithField materialField;
    private final MaterialMap materialMap;
    private final LabelWithField maxHeightField;
    private final LabelWithField maxSizeField;
    private final LabelWithField minHeightField;
    private final LabelWithField rarityField;
    private final LabelWithField sourceBlocksField;

    OreSpawnerPanel(MaterialMap materialMap) {
        this.materialMap = materialMap;

        setLayout(new FlowLayout(FlowLayout.LEADING));

        add(materialField = new LabelWithField("Ore material (block:data)"));
        add(maxSizeField = new LabelWithField("Maximum ore radius"));
        add(frequencyField = new LabelWithField("Attempts per chunk"));
        add(rarityField = new LabelWithField("Chance per attempt"));
        add(minHeightField = new LabelWithField("Minimum height"));
        add(maxHeightField = new LabelWithField("Maximum height"));
        add(sourceBlocksField = new LabelWithField("Spawn in (block,block,...)"));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        try {
            MaterialData material = ParseUtil.parseMaterialData(materialField.getText(), materialMap);
            int maxSize = ParseUtil.parseInt(maxSizeField.getText(), 1, 64);
            int frequency = ParseUtil.parseInt(frequencyField.getText(), 1, 100);
            double rarity = ParseUtil.parseDouble(rarityField.getText(), 0.0001, 100.0);
            int minHeight = ParseUtil.parseInt(minHeightField.getText(), 0, Chunk.CHUNK_Y_SIZE);
            int maxHeight = ParseUtil.parseInt(maxHeightField.getText(), 0, Chunk.CHUNK_Y_SIZE);
            MaterialSet sourceBlocks = ParseUtil.parseMaterialSet(sourceBlocksField.getText(), materialMap);
            return new OreSpawner(material, maxSize, frequency, rarity, minHeight, maxHeight, sourceBlocks);
        } catch (ParseException e) {
            throw new InvalidTaskException(e.getLocalizedMessage());
        }
    }

}
