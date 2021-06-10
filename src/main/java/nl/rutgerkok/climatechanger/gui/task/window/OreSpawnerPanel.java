package nl.rutgerkok.climatechanger.gui.task.window;

import java.awt.FlowLayout;
import java.text.ParseException;

import javax.swing.JLabel;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.gui.LabelWithSelectionBox;
import nl.rutgerkok.climatechanger.task.OreSpawner;
import nl.rutgerkok.climatechanger.task.OreSpawner.HeightDistribution;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.ParseUtil;
import nl.rutgerkok.hammer.material.GlobalMaterialMap;
import nl.rutgerkok.hammer.material.MaterialData;
import nl.rutgerkok.hammer.material.MaterialSet;

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
    private final LabelWithSelectionBox<HeightDistribution> heightDistributionField;

    OreSpawnerPanel(GlobalMaterialMap materialMap) {
        this.materialMap = materialMap;

        setLayout(new FlowLayout(FlowLayout.LEADING));

        add(materialField = new LabelWithField("Ore material (namespaced id)"));
        add(maxSizeField = new LabelWithField("Maximum ore radius", "33"));
        add(frequencyField = new LabelWithField("Attempts per chunk", "10"));
        add(rarityField = new LabelWithField("Chance per attempt", "100"));
        add(minHeightField = new LabelWithField("Minimum height", "0"));
        add(maxHeightField = new LabelWithField("Maximum height", "80"));
        add(heightDistributionField = new LabelWithSelectionBox<>("Height distribution", HeightDistribution.values()));
        add(sourceBlocksField = new LabelWithField("Spawn in (block;block,...)",
                "stone;deepslate[axis=y];tuff;granite;diorite;andesite"));
        add(new JLabel("Note: deepslate ore variants are handled automatically."));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        try {
            MaterialData material = ParseUtil.parseMaterialData(materialField.getText(), materialMap);
            int maxSize = ParseUtil.parseInt(maxSizeField.getText(), 1, OreSpawner.MAX_ORE_SIZE);
            int frequency = ParseUtil.parseInt(frequencyField.getText(), 1, OreSpawner.MAX_ORE_FREQUENCY);
            double rarity = ParseUtil.parseDouble(rarityField.getText(), 0.0001, 100.0);
            int minHeight = ParseUtil.parseInt(minHeightField.getText(), OreSpawner.MIN_Y, OreSpawner.MAX_Y);
            int maxHeight = ParseUtil.parseInt(maxHeightField.getText(), minHeight, OreSpawner.MAX_Y);
            MaterialSet sourceBlocks = ParseUtil.parseMaterialSet(sourceBlocksField.getText(), materialMap);
            HeightDistribution heightDistribution = heightDistributionField.getValue();
            return new OreSpawner(materialMap, material, maxSize, frequency, rarity, minHeight, maxHeight,
                    heightDistribution, sourceBlocks);
        } catch (ParseException e) {
            throw new InvalidTaskException(e.getLocalizedMessage());
        }
    }

}
