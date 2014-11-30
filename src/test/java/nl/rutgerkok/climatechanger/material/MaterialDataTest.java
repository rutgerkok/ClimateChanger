package nl.rutgerkok.climatechanger.material;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MaterialDataTest {

    @Test
    public void testBlockDataMatch() {
        Material testMaterial = new Material("test", 1);
        MaterialData specifiedBlockData = MaterialData.of(testMaterial, (byte) 0);
        MaterialData unspecifiedBlockData = MaterialData.ofAnyState(testMaterial);

        assertFalse(specifiedBlockData.isBlockDataUnspecified());
        assertTrue(unspecifiedBlockData.isBlockDataUnspecified());

        assertNotEquals(specifiedBlockData, unspecifiedBlockData);

        assertFalse(specifiedBlockData.blockDataMatches((short) 2));
        assertTrue(unspecifiedBlockData.blockDataMatches((short) 2));
    }
}
