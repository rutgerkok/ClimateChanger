package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayTag extends Tag {
    public final byte[] data;

    public ByteArrayTag(byte[] data) {
        this.data = data;
    }

    ByteArrayTag(DataInput dis) throws IOException {
        int length = dis.readInt();
        data = new byte[length];
        dis.readFully(data);
    }

    @Override
    public Tag copy() {
        byte[] cp = new byte[data.length];
        System.arraycopy(data, 0, cp, 0, data.length);
        return new ByteArrayTag(cp);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            ByteArrayTag o = (ByteArrayTag) obj;
            return Arrays.equals(data, o.data);
        }
        return false;
    }

    @Override
    public TagType<?> getType() {
        return TagType.BYTE_ARRAY;
    }

    @Override
    public String toString() {
        return "[" + data.length + " bytes]";
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeInt(data.length);
        dos.write(data);
    }
}
