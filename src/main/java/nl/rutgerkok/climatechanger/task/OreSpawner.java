package nl.rutgerkok.climatechanger.task;

import java.util.Random;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.MathUtil;
import nl.rutgerkok.hammer.Chunk;
import nl.rutgerkok.hammer.anvil.AnvilChunk;
import nl.rutgerkok.hammer.material.GlobalMaterialMap;
import nl.rutgerkok.hammer.material.MaterialData;
import nl.rutgerkok.hammer.material.MaterialSet;
import nl.rutgerkok.hammer.util.Result;

/**
 * Spawns ores in a chunk.
 *
 */
public final class OreSpawner implements ChunkTask {

    public static final int MAX_ORE_FREQUENCY = 100;
    public static final int MAX_ORE_SIZE = 64;
    public static final int MIN_Y = -10000;
    public static final int MAX_Y = 10000;

    /**
     * Used to automatically change ores to the correct variant.
     */
    private static final BiMap<String, String> DEEPSLATE_ORES = ImmutableBiMap.<String, String>builder()
            .put("minecraft:coal_ore", "minecraft:deepslate_coal_ore")
            .put("minecraft:copper_ore", "minecraft:deepslate_copper_ore")
            .put("minecraft:lapis_ore", "minecraft:deepslate_lapis_ore")
            .put("minecraft:iron_ore", "minecraft:deepslate_iron_ore")
            .put("minecraft:gold_ore", "minecraft:deepslate_gold_ore")
            .put("minecraft:redstone_ore", "minecraft:deepslate_redstone_ore")
            .put("minecraft:diamond_ore", "minecraft:deepslate_diamond_ore")
            .put("minecraft:emerald_ore", "minecraft:deepslate_emerald_ore")
            .build();

    /**
     * Version of {@link #DEEPSLATE_ORES} that has the actual materials.
     */
    private final BiMap<MaterialData, MaterialData> deepslateOres;
    private final MaterialData deepslate;
    private final MaterialData stone;

    private int frequency;
    private MaterialData material;
    private int maxAltitude;
    private int maxSize;
    private int minAltitude;
    private Random random = new Random();
    private double rarity;

    private MaterialSet sourceBlocks;

    public OreSpawner(GlobalMaterialMap map, MaterialData material, int maxSize, int frequency, double rarity, int minAltitude, int maxAltitude, MaterialSet sourceBlocks) throws InvalidTaskException {
        this.material = material;
        this.maxSize = maxSize;
        this.frequency = frequency;
        this.rarity = rarity;
        this.minAltitude = minAltitude;
        this.maxAltitude = maxAltitude;
        this.sourceBlocks = sourceBlocks;

        if (maxSize <= 0) {
            throw new InvalidTaskException("Max size must be positive");
        }
        if (minAltitude >= maxAltitude) {
            throw new InvalidTaskException("Minimum height must be smaller than maximum height");
        }

        ImmutableBiMap.Builder<MaterialData, MaterialData> builder = ImmutableBiMap.builderWithExpectedSize(DEEPSLATE_ORES.size());
        DEEPSLATE_ORES.forEach((key, value) -> {
            builder.put(map.getMaterialByName(key), map.getMaterialByName(value));
        });
        this.deepslateOres = builder.build();
        this.deepslate = map.getMaterialByName("minecraft:deepslate[axis=y]");
        this.stone = map.getMaterialByName("minecraft:stone");
    }

    private void changeOreMaterial(Chunk chunk, int x, int y, int z) {
        MaterialData newMaterial = this.material;
        MaterialData oldMaterial = chunk.getMaterial(x, y, z);

        if (sourceBlocks.contains(stone) && newMaterial.getBaseName().equals("minecraft:deepslate")) {
            // Place deepslate ore instead of deepslate
            MaterialData foundMaterial = deepslateOres.get(oldMaterial);
            if (foundMaterial != null) {
                newMaterial = foundMaterial;
            }
        } else if (sourceBlocks.contains(deepslate) && newMaterial.getBaseName().equals("minecraft:stone")) {
            // Place stone ore instead of stone
            MaterialData foundMaterial = deepslateOres.inverse().get(oldMaterial);
            if (foundMaterial != null) {
                newMaterial = foundMaterial;
            }
        } else if (sourceBlocks.contains(deepslate) && oldMaterial.getBaseName().equals("minecraft:deepslate")) {
            // Place deepslate ore instead of normal ore
            MaterialData foundMaterial = deepslateOres.get(newMaterial);
            if (foundMaterial != null) {
                newMaterial = foundMaterial;
            }
        } else if (sourceBlocks.contains(oldMaterial)) {
            newMaterial = this.material;
        }

        chunk.setMaterial(x, y, z, newMaterial);
    }

    @Override
    public Result convertChunk(AnvilChunk chunk) {
        Result result = Result.NO_CHANGES;
        for (int t = 0; t < frequency; t++)
        {
            if (random.nextDouble() * 100.0 > rarity) {
                continue;
            }
            int x = random.nextInt(chunk.getSizeX());
            int z = random.nextInt(chunk.getSizeZ());
            if (spawn(chunk, x, z)) {
                result = Result.CHANGED;
            }
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "place ore of type " + material;
    }

    /**
     * Spawns this ore.
     *
     * @param chunk
     *            Chunk to spawn in.
     * @param xOrigin
     *            X coord to spawn, should be between 0 and 16.
     * @param zOrigin
     *            Z coord to spawn, should be between 0 and 16.
     * @return True if the chunk was changed, false otherwise.
     */
    private boolean spawn(Chunk chunk, int xOrigin, int zOrigin) {
        boolean changed = false;
        int yOrigin = random.nextInt(maxAltitude - minAltitude) + minAltitude;

        float f = random.nextFloat() * (float) Math.PI;

        double d1 = xOrigin + 8 + MathUtil.sin(f) * maxSize / 8.0F;
        double d2 = xOrigin + 8 - MathUtil.sin(f) * maxSize / 8.0F;
        double d3 = zOrigin + 8 + MathUtil.cos(f) * maxSize / 8.0F;
        double d4 = zOrigin + 8 - MathUtil.cos(f) * maxSize / 8.0F;

        double d5 = yOrigin + random.nextInt(3) - 2;
        double d6 = yOrigin + random.nextInt(3) - 2;

        for (int i = 0; i < maxSize; i++) {
            float iFactor = (float) i / (float) maxSize;
            double d7 = d1 + (d2 - d1) * iFactor;
            double d8 = d5 + (d6 - d5) * iFactor;
            double d9 = d3 + (d4 - d3) * iFactor;

            double d10 = random.nextDouble() * maxSize / 16.0D;
            double d11 = (MathUtil.sin((float) Math.PI * iFactor) + 1.0) * d10 + 1.0;
            double d12 = (MathUtil.sin((float) Math.PI * iFactor) + 1.0) * d10 + 1.0;

            int j = MathUtil.floor(d7 - d11 / 2.0D);
            int k = MathUtil.floor(d8 - d12 / 2.0D);
            int m = MathUtil.floor(d9 - d11 / 2.0D);

            int n = MathUtil.floor(d7 + d11 / 2.0D);
            int i1 = MathUtil.floor(d8 + d12 / 2.0D);
            int i2 = MathUtil.floor(d9 + d11 / 2.0D);

            for (int x = j; x <= n; x++) {
                double d13 = (x + 0.5D - d7) / (d11 / 2.0D);
                if (d13 * d13 < 1.0D) {
                    for (int y = k; y <= i1; y++) {
                        double d14 = (y + 0.5D - d8) / (d12 / 2.0D);
                        if (d13 * d13 + d14 * d14 < 1.0D) {
                            for (int z = m; z <= i2; z++) {
                                double d15 = (z + 0.5D - d9) / (d11 / 2.0D);
                                if (chunk.isOutOfBounds(x, y, z)) {
                                    continue;
                                }
                                if ((d13 * d13 + d14 * d14 + d15 * d15 < 1.0D)) {
                                    changeOreMaterial(chunk, x, y, z);
                                    changed = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }

}
