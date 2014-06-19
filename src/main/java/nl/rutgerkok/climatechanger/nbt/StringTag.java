package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class StringTag extends Tag {
    public final String data;

    StringTag(DataInput dis) throws IOException {
        data = dis.readUTF();
    }

    public StringTag(String data) {
        this.data = data;
        if (data == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    @Override
    public Tag copy() {
        return new StringTag(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            StringTag o = (StringTag) obj;
            return Objects.equals(data, o.data);
        }
        return false;
    }

    @Override
    public TagType<?> getType() {
        return TagType.STRING;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ data.hashCode();
    }

    @Override
    public String toString() {
        return data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeUTF(data);
    }

}
