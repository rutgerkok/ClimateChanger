package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntArrayTag extends Tag {
    public final int[] data;

    IntArrayTag(DataInput dis) throws IOException {
        int length = dis.readInt();
        data = new int[length];
        for (int i = 0; i < length; i++) {
            data[i] = dis.readInt();
        }
    }

    public IntArrayTag(int[] data) {
        this.data = data;
    }

    @Override
    public Tag copy() {
        int[] cp = new int[data.length];
        System.arraycopy(data, 0, cp, 0, data.length);
        return new IntArrayTag(cp);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            IntArrayTag o = (IntArrayTag) obj;
            return ((data == null && o.data == null) || (data != null && data.equals(o.data)));
        }
        return false;
    }

    @Override
    public TagType<?> getType() {
        return TagType.INT_ARRAY;
    }

    @Override
    public String toString() {
        return "[" + data.length + " bytes]";
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeInt(data.length);
        for (int element : data) {
            dos.writeInt(element);
        }
    }
}
