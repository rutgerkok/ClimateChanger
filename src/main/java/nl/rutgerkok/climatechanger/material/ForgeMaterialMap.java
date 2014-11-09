package nl.rutgerkok.climatechanger.material;

import nl.rutgerkok.climatechanger.nbt.CompoundTag;
import nl.rutgerkok.climatechanger.nbt.ListTag;
import nl.rutgerkok.climatechanger.util.MaterialNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Uses the mappings as written by Forge.
 *
 */
public class ForgeMaterialMap implements MaterialMap {

    private static final String AIR_NAME = "air";

    private Material air = null;
    private Map<Integer, Material> byId = new HashMap<>();
    private Map<String, Material> byName = new HashMap<>();

    public ForgeMaterialMap(ListTag<CompoundTag> itemDataTag) {
        for (CompoundTag mapping : itemDataTag) {
            // Forge seems to a strange character before the key
            register(mapping.getString("K").replaceAll("[^\\w\\-:]", ""), mapping.getInt("V"));
        }
    }

    @Override
    public Material getAir() {
        if (this.air == null) {
            try {
                this.air = getByName(AIR_NAME);
            } catch (MaterialNotFoundException e) {
                throw new RuntimeException("No material for " + AIR_NAME);
            }
        }
        return this.air;
    }

    @Override
    public Material getById(int id) throws MaterialNotFoundException {
        Material material = byId.get(id);
        if (material == null) {
            throw new MaterialNotFoundException(id);
        }
        return material;
    }

    @Override
    public Material getByName(String name) throws MaterialNotFoundException {
        if (!name.contains(":")) {
            // Missing separator, assume default namespace
            name = MINECRAFT_PREFIX + name;
        }

        Material material = byName.get(name);
        if (material == null) {
            throw new MaterialNotFoundException(name);
        }
        return material;
    }

    @Override
    public Material getByNameOrId(String nameOrId) throws MaterialNotFoundException {
        try {
            return getById(Integer.parseInt(nameOrId));
        } catch (NumberFormatException e) {
            return getByName(nameOrId);
        }
    }

    private void register(String name, int id) {
        Material material = new Material(name, id);
        byName.put(name, material);
        byId.put(id, material);
    }

}
